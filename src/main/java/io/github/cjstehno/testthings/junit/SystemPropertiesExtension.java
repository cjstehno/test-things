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
import static java.util.Arrays.stream;

/**
 * FIXME: document - properties configured as system properties using
 * - Properties object
 * - Map<String, String>
 * <p>
 * This is a before/after ALL extension - the properties are considered read-only and should not change
 * between test methods
 * <p>
 * The tests run with this extension are locked so taht only one should execute at a time - still be careful
 * of overlapping execution.
 */
public class SystemPropertiesExtension implements BeforeEachCallback, AfterEachCallback {

    private final Map<String, String> originals = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        lock.lock();

        resolveConfiguredProperties(context.getRequiredTestInstance()).forEach((k, v) -> {
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

    public static Properties asProperties(final Map<String, String> map) {
        val props = new Properties();
        props.putAll(map);
        return props;
    }

    @SuppressWarnings("unchecked")
    private static Properties resolveConfiguredProperties(final Object testInstance) {
        return stream(testInstance.getClass().getDeclaredFields())
            .filter(f -> f.getType().equals(Properties.class) || f.getType().equals(Map.class))
            .findAny()
            .map(f -> {
                try {
                    if (f.getType().equals(Properties.class)) {
                        return (Properties) f.get(testInstance);
                    } else {
                        return asProperties((Map<String, String>) f.get(testInstance));
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Unable to resolve configured properties: " + ex.getMessage(), ex);
                }
            })
            .orElseThrow(() -> new IllegalArgumentException(
                "A Properties or Map<String,String> must be defined to configure the properties."
            ));
    }
}
