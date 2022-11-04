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
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static java.lang.System.nanoTime;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;

/**
 * A JUnit test extension used to test randomized scenarios in a way that removes the randomness. This extension will
 * only work with classes that use the <code>SharedRandom</code> class to provide their randomization.
 * <p>
 * The extension will set a known seed value on the random generator so that it is no longer random. By default, a shared
 * known seed will be used (see DEFAULT_KNOWN_SEED); however, this may be overridden by a configured value in your test
 * class.
 * <p>
 * You can specify your own seed value by adding a field to your class with the signature
 * <code>private static final long KNOWN_SEED = &lt;your-value&gt;</code> to your test class. This provided value will
 * be used instead of the default (it does not have to be "private").
 * <p>
 * The random generator is reset after each test by setting the seed to the current <code>nanoTime()</code> value (i.e.
 * making it "random" again).
 *
 * <strong>Note:</strong> In case you are not aware, the seed-based random number generation is not really random - if
 * you use the same seed, you get the same "random" values in the same order, which is the basis for this method of
 * testing.
 */
@Slf4j
public class SharedRandomExtension implements BeforeEachCallback, AfterEachCallback {

    /**
     * The default known seed value - should be good enough for most cases. You can override this default by providing
     * a <code>static final long KNOWN_SEED</code> field with your value.
     */
    public static long DEFAULT_KNOWN_SEED = 4242424242L;
    private static final String KNOWN_SEED = "KNOWN_SEED";

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        // set the seed to the default or configured value
        ((SharedRandom) current()).reseed(resolveKnownSeed(context.getRequiredTestClass()));
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        // reset it to standard "random" behavior
        ((SharedRandom) current()).reseed(nanoTime());
    }

    private static long resolveKnownSeed(final Class<?> testClass) throws Exception {
        return stream(testClass.getDeclaredFields())
            .filter(f -> isStatic(f.getModifiers()))
            .filter(f -> f.getName().equals(KNOWN_SEED))
            .filter(f -> f.getType().equals(long.class))
            .findFirst()
            .map(f -> {
                try {
                    f.setAccessible(true);
                    return (long) f.get(testClass);
                } catch (Exception ex) {
                    log.error("Unable to resolve known seed - returning default value.", ex);
                    return DEFAULT_KNOWN_SEED;
                }
            })
            .orElse(DEFAULT_KNOWN_SEED);
    }
}
