/**
 * Copyright (C) 2022 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.testthings.rando;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.lang.Character.toLowerCase;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

/**
 * Randomizer used to generate randomized complex objects.
 * <p>
 * TODO: more details
 *
 * @param <T> the type of randomized object
 */
@RequiredArgsConstructor(access = PRIVATE)
public final class ObjectRandomizer<T> implements Randomizer<T> {

    // FIXME: could create a simple extension for this similar to the logger

    private final Class<T> type;
    private final ObjectRandomizerConfigImpl randomizerConfig;

    /**
     * Used to configure a Randomizer which will produce random objects built using the specified `RandomizerConfig`.
     *
     * @param type   the type of object to be created and randomly populated.
     * @param config the randomizer configuration
     * @param <T>    the type of the randomized object
     * @return the Randomizer which will produce random instances of the target class.
     */
    public static <T> Randomizer<T> randomize(final Class<T> type, final Consumer<ObjectRandomizerConfig> config) {
        final ObjectRandomizerConfigImpl randomizerConfig = new ObjectRandomizerConfigImpl();
        config.accept(randomizerConfig);
        return new ObjectRandomizer<>(type, randomizerConfig);
    }

    /**
     * Used to configure a Randomizer which will produce random objects built using the specified `RandomizerConfig`.
     *
     * @param type   the type of object to be created and randomly populated.
     * @param config the randomizer configuration
     * @param <T>    the type of the randomized object
     * @return the Randomizer which will produce random instances of the target class.
     */
    public static <T> Randomizer<T> randomize(final Class<T> type, ObjectRandomizerConfig config) {
        return new ObjectRandomizer<>(type, (ObjectRandomizerConfigImpl) config);
    }

    @Override public T one() {
        try {
            val ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            val instance = ctor.newInstance();

            // FIXME: consider the interplay of setters and fields?

            for (val setter : findSetters(type)) {
                final String propName = propertyName(setter);

                final Optional<Randomizer<?>> propRandomizer = randomizerConfig.propertyRandomizer(propName);
                final Optional<Randomizer<?>> propTypeRandomizer = randomizerConfig.propertyTypeRandomizer(setter.getParameterTypes()[0]);

                if (propRandomizer.isPresent()) {
                    setter.invoke(instance, propRandomizer.get().one());
                } else if (propTypeRandomizer.isPresent()) {
                    setter.invoke(instance, propTypeRandomizer.get().one());
                }
            }

            for (val field : findFields(type)) {
                final Optional<Randomizer<?>> fieldRandomizer = randomizerConfig.fieldRandomizer(field.getName());
                final Optional<Randomizer<?>> fieldTypeRandomizer = randomizerConfig.fieldTypeRandomizer(field.getType());

                if (fieldRandomizer.isPresent()) {
                    field.setAccessible(true);
                    field.set(instance, fieldRandomizer.get().one());
                } else if (fieldTypeRandomizer.isPresent()) {
                    field.setAccessible(true);
                    field.set(instance, fieldTypeRandomizer.get().one());
                }
            }

            return instance;

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String propertyName(final Method method) {
        final String name = method.getName().substring(3);
        return toLowerCase(name.charAt(0)) + name.substring(1);
    }

    // FIXME: these should be moved to a utility
    private static List<Method> findSetters(final Class<?> inClass) {
        final List<Method> setters = Arrays.stream(inClass.getDeclaredMethods())
            .filter(method -> method.getName().startsWith("set") && method.getParameterCount() == 1)
            .collect(toList());

        Class<?> superclass = inClass.getSuperclass();
        while (superclass != Object.class) {
            Arrays.stream(superclass.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set") && method.getParameterCount() == 1)
                .forEach(setters::add);

            superclass = superclass.getSuperclass();
        }

        // FIXME: roll this in
        setters.forEach(s -> s.setAccessible(true));

        return setters;
    }

    private static List<Field> findFields(final Class<?> inClass) {
        // FIXME: consider using ReflectionSupport - its in junit but its here

        final List<Field> fields = Arrays.stream(inClass.getDeclaredFields()).collect(toList());

        Class<?> superclass = inClass.getSuperclass();
        while (superclass != Object.class) {
            fields.addAll(asList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }

        return fields;
    }
}
