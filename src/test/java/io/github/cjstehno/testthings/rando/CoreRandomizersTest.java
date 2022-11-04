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

import io.github.cjstehno.testthings.fixtures.PhoneticAlphabet;
import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.cjstehno.testthings.fixtures.PhoneticAlphabet.*;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anIntBetween;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.arrayOf;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.oneOf;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
class CoreRandomizersTest {

    // FIXME: test all

    @Test @SuppressWarnings("RedundantArrayCreation")
    void oneOfArray() {
        val rando = CoreRandomizers.oneOf(new String[]{"alpha", "bravo", "charlie"});
        assertEquals("bravo", rando.one());
        assertEquals("alpha", rando.one());
        assertEquals("alpha", rando.one());
    }

    @Test void oneOfVarargs() {
        val rando = CoreRandomizers.oneOf("alpha", "bravo", "charlie");
        assertEquals("bravo", rando.one());
        assertEquals("alpha", rando.one());
        assertEquals("alpha", rando.one());
    }

    @Test void oneOfEnum() {
        val rando = CoreRandomizers.oneOf(PhoneticAlphabet.class);
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
}