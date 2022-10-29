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

import lombok.val;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.testthings.rando.SharedRandom.SEED_PROPERTY;
import static io.github.cjstehno.testthings.rando.SharedRandom.generator;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SharedRandomTest {

    @Test void prototype() {
        val n = 20;

        val r0 = generator();
        val seed = r0.getSeed();

        val g0 = genX(r0, n);

        val r1 = generator(seed);
        val g1 = genX(r1, n);

        assertArrayEquals(g0, g1);

        val rdiff = generator();
        val gdiff = genX(rdiff, n);
        assertArraysNotEqual(g0, gdiff);
    }

    @Test void propertyConfig(){
        System.setProperty(SEED_PROPERTY, "8675309");

        val r = generator();
        assertEquals(8675309L, r.getSeed());

        val r1 = generator(987654L);
        assertEquals(987654L, r1.getSeed());

        System.setProperty(SEED_PROPERTY, "");
    }

    private void assertArraysNotEqual(final long[] a, final long[] b) {
        // should be same length
        assertEquals(a.length, b.length);

        // values should be different
        for (int x = 0; x < a.length; x++) {
            assertNotEquals(a[x], b[x]);
        }
    }

    private long[] genX(final SharedRandom r, final int count) {
        val values = new long[count];

        for (int v = 0; v < count; v++) {
            values[v] = r.nextLong();
        }

        return values;
    }
}
