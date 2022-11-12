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
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static java.lang.System.nanoTime;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;

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
 * Alternately, if your test method is annotated with the {@link ApplySeed} annotation, its value will be used as the
 * seed for that test method.
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
        ((SharedRandom) current()).reseed(resolveKnownSeed(context.getRequiredTestClass(), context.getRequiredTestMethod()));
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        // reset it to standard "random" behavior
        ((SharedRandom) current()).reseed(nanoTime());
    }

    private static long resolveKnownSeed(final Class<?> testClass, final Method testMethod) throws Exception {
        var seed = findAnnotation(testMethod, ApplySeed.class).map(ApplySeed::value);

        if (seed.isEmpty()) {
            seed = firstField(testClass, KNOWN_SEED, Long.TYPE).map(f -> {
                try {
                    f.setAccessible(true);
                    return (long) f.get(testClass);
                } catch (final IllegalAccessException e) {
                    log.warn("Unable to access field ({}) - using default: {}", f.getName(), e.getMessage(), e);
                    return DEFAULT_KNOWN_SEED;
                }
            });
        }

        return seed.orElse(DEFAULT_KNOWN_SEED);
    }

    private static Optional<Field> firstField(final Class<?> testClass, final String fieldName, final Class<?> fieldType) {
        return findFields(
            testClass,
            f -> ModifierSupport.isStatic(f) && f.getType().equals(fieldType) && f.getName().equals(fieldName),
            TOP_DOWN
        ).stream().findFirst();
    }
}
