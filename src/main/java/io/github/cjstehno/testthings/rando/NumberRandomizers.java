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

import lombok.NoArgsConstructor;
import lombok.val;

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static lombok.AccessLevel.PRIVATE;

/**
 * A collection of randomizers for generating numbers.
 */
@NoArgsConstructor(access = PRIVATE)
public final class NumberRandomizers {

    // FIXME: other ranges too: short, long, byte, float, double

    /**
     * Builds a randomizer which will generate a random int between the min (inclusive) and max (exclusive) bounds.
     *
     * @param min the min value (inclusive)
     * @param max the max value (exclusive)
     * @return the randomizer
     */
    public static Randomizer<Integer> anIntBetween(final int min, final int max) {
        return () -> current().nextInt(min, max);
    }

    /**
     * A randomizer which will generate a random boolean value.
     *
     * @return the randomizer
     */
    public static Randomizer<Boolean> bool() {
        return () -> current().nextBoolean();
    }

    /**
     * A randomizer which will generate a random int value.
     *
     * @return the randomizer
     */
    public static Randomizer<Integer> anInt() {
        return () -> current().nextInt();
    }

    /**
     * A randomizer which will generate a random short value.
     *
     * @return the randomizer
     */
    public static Randomizer<Short> aShort() {
        return () -> Integer.valueOf(current().nextInt()).shortValue();
    }

    /**
     * A randomizer which will generate a random long value.
     *
     * @return the randomizer
     */
    public static Randomizer<Long> aLong() {
        return () -> current().nextLong();
    }

    /**
     * A randomizer which will generate a random double value.
     *
     * @return the randomizer
     */
    public static Randomizer<Double> aDouble() {
        return () -> current().nextDouble();
    }

    /**
     * A randomizer which will generate a random float value.
     *
     * @return the randomizer
     */
    public static Randomizer<Float> aFloat() {
        return () -> current().nextFloat();
    }

    /**
     * A randomizer which will generate a random byte value.
     *
     * @return the randomizer
     */
    public static Randomizer<Byte> aByte() {
        return () -> {
            val bytes = new byte[1];
            current().nextBytes(bytes);
            return bytes[0];
        };
    }

    /**
     * A randomizer which will generate a random byte array value.
     *
     * @param length the length of the byte array
     * @return the randomizer
     */
    public static Randomizer<byte[]> byteArray(int length) {
        return () -> {
            val bytes = new byte[length];
            current().nextBytes(bytes);
            return bytes;
        };
    }
}
