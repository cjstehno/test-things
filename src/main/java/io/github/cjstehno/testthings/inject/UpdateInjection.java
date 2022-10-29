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

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

import static io.github.cjstehno.testthings.inject.Injection.findField;
import static io.github.cjstehno.testthings.inject.Injection.findGetter;

@RequiredArgsConstructor
public class UpdateInjection implements Injection {

    private final String name;
    private final Function<Object, Object> updater;
    private final boolean preferProps;

    @Override
    public void injectInto(final Object instance) throws ReflectiveOperationException {
        val field = findField(instance.getClass(), name);

        // get the current value
        val currentValue = extractCurrentValue(instance, field);

        // transform the value
        final Object updatedValue = updater.apply(currentValue);

        // update the value
        new SetInjection(name, updatedValue, preferProps).injectInto(instance);
    }

    private Object extractCurrentValue(final Object instance, final Optional<Field> field) throws ReflectiveOperationException {
        final Object currentValue;

        val getter = findGetter(instance.getClass(), name);
        if (preferProps && getter.isPresent()) {
            currentValue = getter.get().invoke(instance);
        } else {
            //  fetch direct
            currentValue = field.get().get(instance);
        }

        return currentValue;
    }

}