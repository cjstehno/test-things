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

import lombok.val;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;

/**
 * A test extension used to update the System properties with a configured set of properties, resetting it back to the
 * original values after each test.
 * <p>
 * In order to provide the property values to be injected, you must provide either a <code>Properties</code> or
 * <code>Map&lt;String,String&gt;</code> object named "SYSTEM_PROPERTIES" on the test class as a static field.
 * <p>
 * Before each test method is executed, the configured properties will be injected into the System properties; however,
 * the original values will be stored and replaced after the test method has finished.
 *
 * <strong>NOTE:</strong> Due to the global nature of the System properties, the test methods under this extension
 * are locked so that only one should run at a time - that being said, if you run into odd issues, try executing these
 * tests in a single-threaded manner (and/or report a bug if you feel the functionality could be improved).
 */
public class SystemPropertiesExtension implements BeforeEachCallback, AfterEachCallback {

    private static final String SYSTEM_PROPERTIES = "SYSTEM_PROPERTIES";
    private final Map<String, String> originals = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        lock.lock();

        resolveConfiguredProperties(context.getRequiredTestClass()).forEach((k, v) -> {
            val key = k.toString();
            val value = v.toString();

            // store the original value
            originals.put(key, getProperty(key, value));

            // replace the value
            setProperty(key, value);
        });
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        // reset to the original values
        originals.forEach(System::setProperty);
        originals.clear();

        lock.unlock();
    }

    /**
     * A helper method that may be used to convert a <code>Map</code> to a <code>Properties</code> object containing
     * the same data.
     *
     * @param map the map of property data
     * @return a Properties object containing the map data
     */
    public static Properties asProperties(final Map<String, String> map) {
        val props = new Properties();
        props.putAll(map);
        return props;
    }

    // FIXME: these resolvers I use would make a good extension helper toolkit
    @SuppressWarnings("unchecked")
    private static Properties resolveConfiguredProperties(final Class<?> testClass) {
        return stream(testClass.getDeclaredFields())
            .filter(f -> isStatic(f.getModifiers()))
            .filter(f -> f.getName().equals(SYSTEM_PROPERTIES))
            .filter(f -> f.getType().equals(Properties.class) || f.getType().equals(Map.class))
            .findAny()
            .map(f -> {
                try {
                    return f.getType().equals(Properties.class) ? (Properties) f.get(testClass) : asProperties((Map<String, String>) f.get(testClass));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Unable to resolve configured properties: " + ex.getMessage(), ex);
                }
            })
            .orElseThrow(() -> new IllegalArgumentException(
                "A Properties or Map<String,String> must be defined to configure the properties."
            ));
    }
}
