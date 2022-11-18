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

/**
 * A thread-safe "random" number generated similar to the {@link java.util.concurrent.ThreadLocalRandom}, but with the
 * ability to have its seed changed after construction.
 *
 * The ability to know and change the seed at runtime is very useful in allowing for repeatable testing of "random"-based
 * utilities. If you use the same seed, the same values will be generated in the same order.
 *
 * The seed may be injected programmatically, or a system property may be set ("test-tings.rando.seed") which will
 * specify the seed for the whole JVM.
 *
 * <strong>NOTE:</strong> Setting the seed to a known value should ONLY be used for development and testing purposes.
 */
@Slf4j
public class SharedRandom implements RandomGenerator {

    /**
     * The System property which may be used to specify a custom seed value (e.g "test-things.rando.seed").
     */
    public static final String SEED_PROPERTY = "test-things.rando.seed";

    private static ThreadLocal<SharedRandom> SHARED = ThreadLocal.withInitial(() -> {
        log.info("Creating a new random.");
        return new SharedRandom(null);
    });

    @Getter private long seed;
    private Random random;

    private SharedRandom(final Long seed) {
        reseed(resolveSeed(seed));
    }

    /**
     * Retrieves the singleton instance of the RandomGenerator for the current thread.
     *
     * @return the random generator instance
     */
    public static RandomGenerator current() {
        return SHARED.get();
    }

    @Override public long nextLong() {
        return random.nextLong();
    }

    /**
     * Updates the seed value and rebuilds the internal random generator.
     *
     * @param newSeed the new seed value
     */
    public void reseed(final long newSeed) {
        seed = newSeed;
        random = new Random(newSeed);
        log.info("Updating seed to {}", newSeed);
    }

    /**
     * Builds a SharedRandom with the specified seed.
     *
     * @param seed the seed
     * @return the SharedRandom instance
     */
    public static SharedRandom generator(final Long seed) {
        return new SharedRandom(seed);
    }

    /**
     * Builds a SharedRandom with the default seed.
     *
     * @return the SharedRandom instance
     */
    public static SharedRandom generator() {
        return generator(null);
    }

    // if a seed is passed, use it, otherwise use configured if exists, then default to current time
    private static long resolveSeed(final Long value) {
        if (value == null) {
            val seedProperty = getProperty(SEED_PROPERTY);
            return seedProperty != null && !seedProperty.isBlank() ? parseLong(seedProperty) : nanoTime();
        }

        if (value < 1) {
            throw new IllegalArgumentException("The seed value must be greater than 0.");
        }

        return value;
    }
}
