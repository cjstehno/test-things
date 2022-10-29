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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
class RandomizersTest {

    // FIXME: test all

    @Test @SuppressWarnings("RedundantArrayCreation")
    void oneOfArray() {
        val rando = Randomizers.oneOf(new String[]{"alpha", "bravo", "charlie"});
        assertEquals("bravo", rando.one());
        assertEquals("alpha", rando.one());
        assertEquals("alpha", rando.one());
    }

    @Test void oneOfVarargs() {
        val rando = Randomizers.oneOf("alpha", "bravo", "charlie");
        assertEquals("bravo", rando.one());
        assertEquals("alpha", rando.one());
        assertEquals("alpha", rando.one());
    }

    @Test void oneOfEnum() {
        val rando = Randomizers.oneOf(PhoneticAlphabet.class);
        assertEquals(PhoneticAlphabet.JULIET, rando.one());
        assertEquals(PhoneticAlphabet.ROMEO, rando.one());
        assertEquals(PhoneticAlphabet.GOLF, rando.one());
    }
}