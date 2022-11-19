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

import java.util.function.Consumer;

import static io.github.cjstehno.testthings.inject.UpdateInjection.resolveCurrentValue;
import static lombok.AccessLevel.PACKAGE;

/**
 * An Injection that allows the modification of an existing mutable object value. The actual object cannot be changed
 * by this injection.
 */
@RequiredArgsConstructor(access = PACKAGE)
class ModifyInjection implements Injection {

    private final String name;
    private final Consumer<Object> modifier;
    private final boolean preferGetter;

    /**
     * Performs the modification of the existing object content.
     *
     * @param instance the target instance
     * @throws ReflectiveOperationException if there is a problem
     */
    @Override public void injectInto(final Object instance) throws ReflectiveOperationException {
        modifier.accept(resolveCurrentValue(instance, name, preferGetter));
    }
}