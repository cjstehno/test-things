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


import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Consumer;

import static lombok.AccessLevel.PRIVATE;

/**
 * The entry point for the injection framework, providing a simple configurable means of injecting values into an
 * instance via reflection.
 */
@RequiredArgsConstructor(access = PRIVATE)
public final class Injector {

    private final InjectionsImpl injections;

    /**
     * Creates an injector with the provided configured injections.
     *
     * @param config the configured injections
     * @return the configured injector
     */
    public static Injector injector(final Consumer<Injections> config) {
        val injections = new InjectionsImpl();
        config.accept(injections);
        return new Injector(injections);
    }

    /**
     * Creates a new Injector with the provided injections configured.
     *
     * @param injections the injections
     * @return the injector
     */
    public static Injector injector(final Injections injections) {
        return new Injector((InjectionsImpl) injections);
    }

    /**
     * Applies the configured injections on the object instance.
     *
     * @param instance the object instance
     * @param config the configured injections
     * @param <T> the type of target instance
     * @throws ReflectiveOperationException if there is a problem with the reflection operations
     * @return the instance of the object populated with the injected values
     */
    public static <T> T inject(final T instance, final Consumer<Injections> config) throws ReflectiveOperationException {
        return injector(config).inject(instance);
    }

    /**
     * Injects the configured injections into the provided object instance.
     *
     * @param instance the object instance
     * @param <T> the type of the target instance
     * @return the instance of the object populated with the injected values
     * @throws ReflectiveOperationException if there is a problem with the reflection operations
     */
    public <T> T inject(final T instance) throws ReflectiveOperationException {
        return injections.apply(instance);
    }
}