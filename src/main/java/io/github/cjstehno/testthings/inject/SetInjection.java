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

import static io.github.cjstehno.testthings.inject.Injection.findField;
import static java.util.Locale.ROOT;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isNotStatic;
import static org.junit.platform.commons.support.ReflectionSupport.findMethod;
import static org.junit.platform.commons.support.ReflectionSupport.findMethods;

/**
 * Injection instance used to inject a value into a field, optionally using an existing setter method.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SetInjection implements Injection {

    // TODO: could use some refactoring

    private final String name;
    private final Object value;
    private final boolean preferSetter;

    /**
     * FIXME: document
     */
    @Override
    public void injectInto(final Object instance) throws ReflectiveOperationException {
        val valueType = resolveValueType(instance.getClass());
        val injectedValue = value instanceof Randomizer<?> rando ? rando.one() : value;

        if (preferSetter) {
            // use the setter, if there is one
            val setter = findMethod(instance.getClass(), getSetterName(), valueType);
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

    public String getSetterName() {
        return "set" + name.substring(0, 1).toUpperCase(ROOT) + name.substring(1);
    }

    private Class<?> resolveValueType(final Class<?> instanceType) {
        if (preferSetter) {
            val methodName = getSetterName();
            val methods = findMethods(
                instanceType,
                m -> m.getName().equals(methodName) && m.getParameterCount() == 1 && isNotStatic(m),
                TOP_DOWN
            );

            if (!methods.isEmpty()) {
                return methods.get(0).getParameterTypes()[0];
            }
        }

        val field = findField(instanceType, name);
        if (field.isPresent()) {
            return field.get().getType();
        } else {
            throw new IllegalArgumentException("Unable to resolve the value type");
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