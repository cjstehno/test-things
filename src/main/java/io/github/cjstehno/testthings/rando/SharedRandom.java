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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Random;
import java.util.random.RandomGenerator;

import static java.lang.Long.parseLong;
import static java.lang.System.getProperty;
import static java.lang.System.nanoTime;

// TODO: document
@Slf4j
public class SharedRandom implements RandomGenerator {

    // TODO: document
    public static final String SEED_PROPERTY = "test-things.rando.seed";

    private static ThreadLocal<SharedRandom> SHARED = ThreadLocal.withInitial(() -> new SharedRandom(null));

    @Getter private final long seed;
    private Random random;

    private SharedRandom(final Long seed) {
        this.seed = resolveSeed(seed);
        this.random = new Random(this.seed);
        log.info("Using seed (set '{}' to pin): {}", SEED_PROPERTY, this.seed);
    }

    public static RandomGenerator current() {
        return SHARED.get();
    }

    @Override public long nextLong() {
        return random.nextLong();
    }

    public void reseed(final long newSeed) {
        random = new Random(newSeed);
        log.info("Resetting seed to {}", newSeed);
    }

    public static SharedRandom generator(final Long seed) {
        return new SharedRandom(seed);
    }

    public static SharedRandom generator() {
        return generator(null);
    }

    // if a seed is passed, use it, otherwise use configured if exists, then default to current time
    private static long resolveSeed(final Long value) {
        // TODO: is a negative or 0 seed valid?
        if (value == null) {
            val seedProperty = getProperty(SEED_PROPERTY);
            return seedProperty != null && !seedProperty.isBlank() ? parseLong(seedProperty) : nanoTime();
        }
        return value;
    }
}
