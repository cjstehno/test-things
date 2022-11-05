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
package io.github.cjstehno.testthings.junit;

import io.github.cjstehno.testthings.rando.SharedRandom;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
class SharedRandomExtensionCustomTest {

    @SuppressWarnings("unused")
    private static final long KNOWN_SEED = 1234123512342L;

    @Test void testing() {
        val rand = SharedRandom.current();

        assertEquals(KNOWN_SEED, ((SharedRandom) rand).getSeed());
        assertEquals(285594675, rand.nextInt());
        assertEquals(-1649795800, rand.nextInt());
        assertEquals(-1558589534, rand.nextInt());
    }

    @Test @ApplySeed(1234567890L)
    void testingApplied() {
        val rand = SharedRandom.current();

        assertEquals(1234567890L, ((SharedRandom) rand).getSeed());
        assertEquals(-1210225943, rand.nextInt());
        assertEquals(-106137655, rand.nextInt());
        assertEquals(-235120566, rand.nextInt());
    }
}