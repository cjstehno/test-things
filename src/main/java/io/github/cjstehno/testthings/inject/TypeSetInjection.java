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
package io.github.cjstehno.testthings.inject;

import io.github.cjstehno.testthings.rando.Randomizer;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashSet;

import static java.util.Locale.ROOT;
import static lombok.AccessLevel.PACKAGE;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isNotStatic;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;
import static org.junit.platform.commons.support.ReflectionSupport.findMethods;

/**
 * Provides injection of values based on the field/property type rather than the name.
 */
@RequiredArgsConstructor(access = PACKAGE)
public class TypeSetInjection implements Injection {
    private final Class<?> type;
    private final Object value;
    private final boolean preferSetter;

    /**
     * Injects the value into all properties/fields of the configured type.
     *
     * @param instance the instance where things should be injected.
     * @throws ReflectiveOperationException if there is a problem
     */
    @Override public void injectInto(final Object instance) throws ReflectiveOperationException {
        val injectedNames = new HashSet<String>();

        // inject via setter if requested
        if (preferSetter) {
            for (val setter : findMethods(
                instance.getClass(),
                m -> isNotStatic(m) && m.getParameterCount() == 1 && type.isAssignableFrom(m.getParameterTypes()[0]) && m.getName().startsWith("set"),
                TOP_DOWN
            )) {
                setter.setAccessible(true);
                setter.invoke(instance, injectedValue());

                // make sure we don't inject twice
                injectedNames.add(setter.getName().substring(3).toLowerCase(ROOT));
            }
        }

        // inject via field otherwise
        for (val field : findFields(
            instance.getClass(),
            f -> isNotStatic(f) && type.isAssignableFrom(f.getType()),
            TOP_DOWN
        )) {
            val name = field.getName().toLowerCase(ROOT);
            if (!injectedNames.contains(name)) {
                field.setAccessible(true);
                field.set(instance, injectedValue());
            }
        }
    }

    private Object injectedValue() {
        return value instanceof Randomizer<?> rando ? rando.one() : value;
    }
}
