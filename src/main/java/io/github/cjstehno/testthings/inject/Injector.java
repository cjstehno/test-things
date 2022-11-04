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
 * FIXME: document
 */
@RequiredArgsConstructor(access = PRIVATE)
public final class Injector {

    private final InjectionsImpl injections;

    /**
     * FIXME: document
     */
    public static Injector injector(final Consumer<Injections> prescription) {
        val injections = new InjectionsImpl();
        prescription.accept(injections);
        return new Injector(injections);
    }

    /**
     * FIXME: document
     */
    public static <T> T inject(final T instance, final Consumer<Injections> prescription) throws ReflectiveOperationException {
        return injector(prescription).inject(instance);
    }

    /**
     * FIXME: document
     */
    public <T> T inject(final T instance) throws ReflectiveOperationException {
        // TODO: better excepotion?
        return injections.apply(instance);
    }
}