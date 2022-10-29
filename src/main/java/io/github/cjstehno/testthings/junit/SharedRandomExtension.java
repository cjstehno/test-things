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
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static java.lang.System.nanoTime;
import static java.util.Arrays.stream;

// FIXME: document - only works for classes using SharedRandom
public class SharedRandomExtension implements BeforeEachCallback, AfterEachCallback {

    // FIXME: needs a test

    public static long DEFAULT_KNOWN_SEED = 4242424242L;

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        // set the seed to the default or configured value
        ((SharedRandom) current()).reseed(resolveKnownSeed(context.getRequiredTestInstance()));
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        // reset it to standard "random" behavior
        ((SharedRandom) current()).reseed(nanoTime());
    }

    private static long resolveKnownSeed(final Object testInstance) throws Exception {
        return stream(testInstance.getClass().getDeclaredFields())
            .filter(f -> f.getType().equals(long.class)) // FIXME: change to use name (optional)
            .findFirst()
            .map(f -> {
                try {
                    return (long) f.get(testInstance);
                } catch (Exception ex) {
                    return DEFAULT_KNOWN_SEED;
                }
            })
            .orElse(DEFAULT_KNOWN_SEED);
    }
}
