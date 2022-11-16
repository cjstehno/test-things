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

import io.github.cjstehno.testthings.fixtures.BirthGender;
import io.github.cjstehno.testthings.fixtures.PhoneticAlphabet;
import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static io.github.cjstehno.testthings.fixtures.BirthGender.FEMALE;
import static io.github.cjstehno.testthings.fixtures.BirthGender.MALE;
import static io.github.cjstehno.testthings.fixtures.PhoneticAlphabet.*;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.listOf;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.mapOf;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.onceEachOf;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.setOf;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.streamOf;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anIntBetween;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.arrayOf;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.constant;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.oneOf;
import static io.github.cjstehno.testthings.rando.NumberRandomizersTest.assertValues;
import static io.github.cjstehno.testthings.rando.StringRandomizers.alphabetic;
import static io.github.cjstehno.testthings.rando.StringRandomizers.number;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SharedRandomExtension.class)
class CoreRandomizersTest {

    // FIXME: test all
    // TODO: change see change logging to DEBUG

    @Test @SuppressWarnings("RedundantArrayCreation")
    void oneOfArray() {
        val rando = oneOf(new String[]{"alpha", "bravo", "charlie"});
        assertEquals("bravo", rando.one());
        assertEquals("alpha", rando.one());
        assertEquals("alpha", rando.one());
    }

    @Test void oneOfVarargs() {
        val rando = oneOf("alpha", "bravo", "charlie");
        assertEquals("bravo", rando.one());
        assertEquals("alpha", rando.one());
        assertEquals("alpha", rando.one());
    }

    @Test void oneOfEnum() {
        val rando = oneOf(PhoneticAlphabet.class);
        assertEquals(PhoneticAlphabet.JULIET, rando.one());
        assertEquals(ROMEO, rando.one());
        assertEquals(PhoneticAlphabet.GOLF, rando.one());
    }

    @Test void array() {
        val rando = arrayOf(anIntBetween(1, 3), oneOf(PhoneticAlphabet.class));
        assertArrayEquals(new PhoneticAlphabet[]{ROMEO}, rando.one());
        assertArrayEquals(new PhoneticAlphabet[]{LIMA, ALPHA}, rando.one());
        assertArrayEquals(new PhoneticAlphabet[]{ALPHA, XRAY}, rando.one());
    }

    @Test void constantValue(){
        assertValues(constant(42).many(3), 42, 42, 42);
    }

    @Test void setOfValues(){
        assertValues(
            setOf(constant(3), anIntBetween(1,100)).many(3),
            Set.of(65, 37, 31), Set.of(54, 92, 31), Set.of(2, 10, 63)
        );
    }

    @Test void listOfValues(){
        assertValues(
            listOf(constant(3), anIntBetween(1,100)).many(3),
            List.of(65, 37, 31), List.of(31, 92, 54), List.of(10, 63, 2)
        );
    }

    @Test void streamOfValues(){
        val streams = streamOf(constant(3), anIntBetween(1,100)).many(3);
        assertEquals(3, streams.size());
        assertEquals(List.of(65, 37, 31), streams.get(0).toList());
        assertEquals(List.of(31, 92, 54), streams.get(1).toList());
        assertEquals(List.of(10, 63, 2), streams.get(2).toList());
    }

    @Test void mapOfValues(){
        assertValues(
            mapOf(constant(3), alphabetic(constant(5)), number()).many(3),
            Map.of(
                "JGAAM", "0",
                "Toypb", "6",
                "sEvTx", "0"
            ),
            Map.of(
                "NRcdV", "1",
                "rhyOr", "7",
                "seaum", "2"
            ),
            Map.of(
                "LHQCI", "0",
                "sRPan", "8",
                "svmdk", "2"
            )
        );
    }

    @Test void oneEachOfCollection(){
        assertValues(onceEachOf(List.of("FIRST", "SECOND", "THIRD")).many(3), "SECOND", "THIRD", "FIRST");
    }

    @Test void oneEachOfCollectionWithNotEnough(){
        assertValues(onceEachOf(List.of("FIRST", "SECOND")).many(3), "FIRST", "SECOND", null);
    }
}