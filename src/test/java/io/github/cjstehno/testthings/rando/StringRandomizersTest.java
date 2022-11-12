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

import static io.github.cjstehno.testthings.rando.CoreRandomizers.constant;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anIntBetween;
import static io.github.cjstehno.testthings.rando.StringRandomizers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SharedRandomExtension.class)
class StringRandomizersTest {

    @Test void randomOneFrom(){
        val three = oneFrom("by the pricking of my thumb something wicked this way comes").many(3);
        assertEquals(3, three.size());
        assertEquals("H", three.get(0));
        assertEquals("K", three.get(1));
        assertEquals("M", three.get(2));
    }

    @Test void randomLetter(){
        val three = letter().many(3);
        assertEquals(3, three.size());
        assertEquals("J", three.get(0));
        assertEquals("G", three.get(1));
        assertEquals("A", three.get(2));
    }

    @Test void randomNumber(){
        val three = number().many(3);
        assertEquals(3, three.size());
        assertEquals("1", three.get(0));
        assertEquals("4", three.get(1));
        assertEquals("6", three.get(2));
    }

    @Test void randomAlphabetic(){
        val three = alphabetic(anIntBetween(3,6)).many(3);
        assertEquals(3, three.size());
        assertEquals("RLGX", three.get(0));
        assertEquals("MToyp", three.get(1));
        assertEquals("pIR", three.get(2));
    }

    @Test void randomNumeric(){
        val three = numeric(anIntBetween(3,6)).many(3);
        assertEquals(3, three.size());
        assertEquals("1105", three.get(0));
        assertEquals("01427", three.get(1));
        assertEquals("381", three.get(2));
    }

    @Test void randomAlphanumeric(){
        val three = alphanumeric(anIntBetween(3,6)).many(3);
        assertEquals(3, three.size());
        assertEquals("JDI9", three.get(0));
        assertEquals("8L22n", three.get(1));
        assertEquals("bGT", three.get(2));
    }

    @Test void randomChar(){
        val three = aChar().many(3);
        assertEquals(3, three.size());
        assertEquals('J', three.get(0));
        assertEquals('G', three.get(1));
        assertEquals('A', three.get(2));
    }

    @Test void randomCharArray(){
        val three = charArray(anIntBetween(3,6)).many(3);
        assertEquals(3, three.size());
        assertArrayEquals("JDI9".toCharArray(), three.get(0));
        assertArrayEquals("8L22n".toCharArray(), three.get(1));
        assertArrayEquals("bGT".toCharArray(), three.get(2));
    }

    @Test void randomWords(){
        val three = words(constant(3), anIntBetween(3,6)).many(3);
        assertEquals(3, three.size());
        assertArrayEquals(new String[]{"RLGX","MToyp","pIR"}, three.get(0));
        assertArrayEquals(new String[]{"vTx","NRbt","VNrh"}, three.get(1));
        assertArrayEquals(new String[]{"kqxYU","aum","kDcF"}, three.get(2));
    }

    @Test void randomWordFromSentence(){
        val three = wordFrom("by the pricking of my thumb something wicked this way comes").many(3);
        assertEquals(3, three.size());
        assertEquals("way", three.get(0));
        assertEquals("of", three.get(1));
        assertEquals("this", three.get(2));
    }

    @Test void randomWordFromArray(){
        val three = wordFrom(new String[]{"how", "much", "wood", "could", "a", "woodchuck", "chuck"}).many(3);
        assertEquals(3, three.size());
        assertEquals("chuck", three.get(0));
        assertEquals("chuck", three.get(1));
        assertEquals("woodchuck", three.get(2));
    }
}