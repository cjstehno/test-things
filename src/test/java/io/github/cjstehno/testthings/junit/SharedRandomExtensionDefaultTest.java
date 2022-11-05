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
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.cjstehno.testthings.junit.SharedRandomExtension.DEFAULT_KNOWN_SEED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class) @Slf4j
class SharedRandomExtensionDefaultTest {

    @Test void testing() {
        val rand = SharedRandom.current();

        assertEquals(DEFAULT_KNOWN_SEED, ((SharedRandom) rand).getSeed());
        assertEquals(1519007222, rand.nextInt());
        assertEquals(281705563, rand.nextInt());
        assertEquals(716866189, rand.nextInt());
    }

    @Test @ApplySeed(987654321L)
    void testingApplied() {
        val rand = SharedRandom.current();

        assertEquals(987654321L, ((SharedRandom) rand).getSeed());
        assertEquals(1314001071, rand.nextInt());
        assertEquals(-778345979, rand.nextInt());
        assertEquals(196174297, rand.nextInt());
    }
}