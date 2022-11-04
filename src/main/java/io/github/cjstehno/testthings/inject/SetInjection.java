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

import static io.github.cjstehno.testthings.inject.Injection.findField;
import static io.github.cjstehno.testthings.inject.Injection.findSetter;

/**
 * FIXME: document
 */
@RequiredArgsConstructor
public class SetInjection implements Injection {

    private final String name;
    private final Object value;
    private final boolean preferSetter;

    /**
     * FIXME: document
     */
    @Override
    public void injectInto(final Object instance) throws ReflectiveOperationException {
        Class<?> valueType = value.getClass();
        Object injectedValue = value;

        if (value instanceof Randomizer<?> rando) {
            injectedValue = rando.one();
            valueType = rando.getClass();
        }

        if (preferSetter) {
            // use the setter, if there is one
            val setter = findSetter(instance.getClass(), name, valueType);
            if (setter.isPresent()) {
                setter.get().invoke(instance, injectedValue);
            } else {
                // fallback to direct access
                injectDirectly(instance, injectedValue);
            }

        } else {
            // use the direct field access
            injectDirectly(instance, injectedValue);
        }
    }

    private void injectDirectly(final Object instance, final Object injectedValue) throws ReflectiveOperationException {
        val field = findField(instance.getClass(), name);
        if (field.isPresent()) {
            field.get().set(instance, injectedValue);
        } else {
            // FIXME: error - no setter or field
            throw new IllegalArgumentException();
        }
    }
}