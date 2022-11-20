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
package io.github.cjstehno.testthings.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Some utility methods for working with reflection. These are mostly meant for internal use.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Reflections {

    /**
     * Extracts the value from the field of the specified instance or class with the given type. Any exception is wrapped
     * in a {@link RuntimeException}.
     *
     * @param classOrInstance the class or instance where the field is defined
     * @param field the field
     * @param valueType the value type of the field
     * @return the extracted field value
     * @param <R> the value type of the field
     */
    @SuppressWarnings("unchecked")
    public static <R> R extractValue(final Object classOrInstance, final Field field, final Class<? extends R> valueType) {
        try {
            field.setAccessible(true);
            return (R) field.get(classOrInstance);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invokes a non-returning method on the specified class or instance. Any exception is wrapped in a {@link RuntimeException}.
     *
     * @param classOrInstance the class or instance where the method is defined
     * @param method the method
     */
    public static void invokeMethod(final Object classOrInstance, final Method method) {
        try {
            method.setAccessible(true);
            method.invoke(classOrInstance);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
