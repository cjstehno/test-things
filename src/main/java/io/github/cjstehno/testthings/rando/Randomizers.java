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
package io.github.cjstehno.testthings.rando;


import lombok.RequiredArgsConstructor;
import lombok.val;

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static lombok.AccessLevel.PRIVATE;

public final class Randomizers {

    @SafeVarargs public static <V> Randomizer<V> oneOf(final V... options) {
        return new OneOfRandomizer<>(options);
    }

    @SuppressWarnings("unchecked")
    public static <V extends Enum<V>> Randomizer<V> oneOf(final Class<V> enumType) {
        try {
            val valuesMethod = enumType.getMethod("values");
            val values = (V[]) valuesMethod.invoke(enumType);
            return new OneOfRandomizer<>(values);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // FIXME: one where each value is popped off when selected

    public static Randomizer<Integer> intRange(final int min, final int max) {
        return new IntRangeRandomizer(min, max);
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class OneOfRandomizer<T> implements Randomizer<T> {

        private final T[] values;

        @Override public T one() {
            return values[current().nextInt(values.length)];
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class IntRangeRandomizer implements Randomizer<Integer> {

        private final int min, max;

        @Override public Integer one() {
            return current().nextInt(min, max);
        }
    }
}