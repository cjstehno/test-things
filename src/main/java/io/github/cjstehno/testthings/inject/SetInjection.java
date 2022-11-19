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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static java.util.Locale.ROOT;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isNotStatic;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;
import static org.junit.platform.commons.support.ReflectionSupport.findMethods;

/**
 * Injection instance used to inject a value into a field, optionally using an existing setter method.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SetInjection implements Injection {

    private final String name;
    private final Object value;
    private final boolean preferSetter;

    /**
     * Injects the configured value into the given instance.
     *
     * @param instance the instance the value is being injected into
     * @throws ReflectiveOperationException if there is a problem injecting the value
     */
    @Override
    public void injectInto(final Object instance) throws ReflectiveOperationException {
        injectValue(
            instance,
            value instanceof Randomizer<?> rando ? rando.one() : value
        );
    }

    private void injectValue(final Object instance, final Object injectedValue) throws ReflectiveOperationException {
        if (preferSetter) {
            // try to use the setter method (if exists)
            val methodName = "set" + name.substring(0, 1).toUpperCase(ROOT) + name.substring(1);
            val methods = findMethods(
                instance.getClass(),
                m -> m.getName().equals(methodName) && m.getParameterCount() == 1 && isNotStatic(m),
                TOP_DOWN
            );

            if (!methods.isEmpty()) {
                methods.get(0).setAccessible(true);
                methods.get(0).invoke(instance, injectedValue);
                return;
            }
        }

        // try to use the field if it exists
        findFields(instance.getClass(), f -> f.getName().equals(name) && isNotStatic(f), TOP_DOWN).stream()
            .peek(f -> f.setAccessible(true))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Unable to inject value for '" + name + "'"))
            .set(instance, injectedValue);
    }
}