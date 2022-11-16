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

import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static io.github.cjstehno.testthings.rando.NumberRandomizers.aBigDecimal;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aBigInteger;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aBoolean;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aByte;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aDouble;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aDoubleBetween;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aFloat;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aFloatBetween;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aLong;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aLongBetween;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.aShort;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anInt;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.byteArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
public class NumberRandomizersTest {

    @Test void bytes() {
        assertValues(aByte().many(3), (byte) 35, (byte) -63, (byte) -58);
    }

    @Test void floats() {
        assertValues(aFloat().many(3), 0.35367137f, 0.06558967f, 0.16690838f);
    }

    @Test void floatsBetween() {
        assertValues(aFloatBetween(0.5f, 0.75f).many(3), 0.5884178f, 0.5163974f, 0.54172707f);
    }

    @Test void doubles() {
        assertValues(aDouble().many(3), 0.3536714293364629d, 0.06558968759209105d, 0.16690841624174801d);
    }

    @Test void doubleBetween(){
        assertValues(aDoubleBetween(0.5d, 0.75d).many(3), 0.5884178573341157d, 0.5163974218980227d, 0.541727104060437d);
    }

    @Test void longs() {
        assertValues(aLong().many(3), 6524086343152784675l, 1209916180885866689l, 3078916838159714246l);
    }

    @Test void longsBetween() {
        assertValues(aLongBetween(10,1000).many(3), 507l, 204l, 803l);
    }

    @Test void shorts() {
        assertValues(aShort().many(3), (short)13814, (short)31835, (short)-32115);
    }

    @Test void ints() {
        assertValues(anInt().many(3), 1519007222, 281705563, 716866189);
    }

    @Test void bools(){
        assertValues(aBoolean().many(3), false, false, false);
    }

    @Test void bigInts() {
        assertValues(aBigInteger().many(3), BigInteger.valueOf(1519007222), BigInteger.valueOf(281705563), BigInteger.valueOf(716866189));
    }

    @Test void bigDecimal() {
        assertValues(aBigDecimal().many(3), BigDecimal.valueOf(0.3536714293364629), BigDecimal.valueOf(0.06558968759209105), BigDecimal.valueOf(0.16690841624174801));
    }

    @Test void byteArrays(){
        val actuals = byteArray(3).many(3);
        assertEquals(3, actuals.size());
        assertArrayEquals(new byte[]{35,85,-103}, actuals.get(0));
        assertArrayEquals(new byte[]{-63,8,-77}, actuals.get(1));
        assertArrayEquals(new byte[]{-58,-121,122}, actuals.get(2));
    }

    // FIXME: move to util
    public static <V> void assertValues(final List<V> actual, final V... expected) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected[0], actual.get(0));
        assertEquals(expected[1], actual.get(1));
        assertEquals(expected[2], actual.get(2));
    }
}
