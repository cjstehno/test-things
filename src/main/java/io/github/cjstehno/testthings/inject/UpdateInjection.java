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

import java.util.function.Function;

import static java.util.Locale.ROOT;
import static lombok.AccessLevel.PACKAGE;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isNotStatic;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;
import static org.junit.platform.commons.support.ReflectionSupport.findMethods;

/**
 * An Injection implementation which injects the value returned by a function, which is passed the current field
 * value. Optionally, the setter and getter methods may be used rather than the direct field access.
 */
@RequiredArgsConstructor(access = PACKAGE)
class UpdateInjection implements Injection {

    private final String name;
    private final Function<Object, Object> updater;
    private final boolean preferSetter;
    private final boolean preferGetter;

    /**
     * Injects the configured value into the given instance.
     *
     * @param instance the instance the value is being injected into
     * @throws ReflectiveOperationException if there is a problem injecting the value
     */
    @Override
    public void injectInto(final Object instance) throws ReflectiveOperationException {
        val currentValue = resolveCurrentValue(instance, name, preferGetter);

        // transform the value
        val updatedValue = updater.apply(currentValue);

        // update the value
        new SetInjection(name, updatedValue, preferSetter).injectInto(instance);
    }

    protected final static Object resolveCurrentValue(final Object instance, final String name, final boolean preferGetter) throws ReflectiveOperationException {
        if (preferGetter) {
            // try to use the getter (if it exists)
            val methodName = "get" + name.substring(0, 1).toUpperCase(ROOT) + name.substring(1);
            val methods = findMethods(
                instance.getClass(),
                m -> m.getName().equals(methodName) && m.getParameterCount() == 0 && isNotStatic(m),
                TOP_DOWN
            );

            if (!methods.isEmpty()) {
                return methods.get(0).invoke(instance);
            }
        }

        // try to use the field
        return findFields(instance.getClass(), f -> f.getName().equals(name) && isNotStatic(f), TOP_DOWN).stream()
            .peek(f -> f.setAccessible(true))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Unable to resolve current value for '" + name + "'"))
            .get(instance);
    }
}