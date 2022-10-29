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


import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Locale.ROOT;

public interface Injection {

    void injectInto(final Object instance) throws ReflectiveOperationException;

    // FIXME: pull these out into a util
    static Optional<Field> findField(final Class<?> inClass, final String name) {
        try {
            val field = inClass.getDeclaredField(name);
            field.setAccessible(true);
            return Optional.of(field);
        } catch (final NoSuchFieldException nsfe) {
            return inClass.getSuperclass() != Object.class ? findField(inClass.getSuperclass(), name) : Optional.empty();
        }
    }

    static Optional<Method> findSetter(final Class<?> inClass, final String propertyName, final Class<?> valueType) {
        val methodName = "set" + propertyName.substring(0, 1).toUpperCase(ROOT) + propertyName.substring(1);

        try {
            val method = inClass.getDeclaredMethod(methodName, valueType);
            method.setAccessible(true);
            return Optional.of(method);
        } catch (final NoSuchMethodException e) {
            return inClass.getSuperclass() != Object.class
                ? findSetter(inClass.getSuperclass(), propertyName, valueType)
                : Optional.empty();
        }
    }

    static Optional<Method> findGetter(final Class<?> inClass, final String propertyName) {
        val methodName = "get" + propertyName.substring(0, 1).toUpperCase(ROOT) + propertyName.substring(1);

        try {
            val method = inClass.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return Optional.of(method);
        } catch (final NoSuchMethodException e) {
            return inClass.getSuperclass() != Object.class
                ? findGetter(inClass.getSuperclass(), propertyName)
                : Optional.empty();
        }
    }
}