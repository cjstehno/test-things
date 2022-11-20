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

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static io.github.cjstehno.testthings.util.Reflections.extractValue;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isStatic;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;

/**
 * A test extension used to update the System properties with a configured set of properties, resetting it back to the
 * original values after each test.
 * <p>
 * In order to provide the property values to be injected, you must provide either a <code>Properties</code> or
 * <code>Map&lt;String,String&gt;</code> object named "SYSTEM_PROPERTIES" on the test class as a static field.
 * <p>
 * Alternately, you may specify the field name containing your properties using the {@link ApplyProperties} annotation
 * on the test method.
 * <p>
 * Before each test method is executed, the configured properties will be injected into the System properties; however,
 * the original values will be stored and replaced after the test method has finished.
 * <p>
 * <strong>NOTE:</strong> Due to the global nature of the System properties, the test methods under this extension
 * are locked so that only one should run at a time - that being said, if you run into odd issues, try executing these
 * tests in a single-threaded manner (and/or report a bug if you feel the functionality could be improved).
 */
@Slf4j
public class SystemPropertiesExtension implements BeforeEachCallback, AfterEachCallback {

    private static final String SYSTEM_PROPERTIES = "SYSTEM_PROPERTIES";
    private static final Namespace NAMESPACE = create("test-things", "system-props");
    private final Lock lock = new ReentrantLock();

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        lock.lock();

        val originals = new HashMap<String, String>();

        resolveConfiguredProperties(context.getRequiredTestClass(), context.getRequiredTestMethod()).forEach((k, v) -> {
            val key = k.toString();
            val value = v.toString();

            // store the original value
            originals.put(key, getProperty(key, value));

            // replace the value
            setProperty(key, value);
        });

        context.getStore(NAMESPACE).put(Thread.currentThread().getName(), originals);
    }

    @Override @SuppressWarnings("unchecked")
    public void afterEach(final ExtensionContext context) throws Exception {
        val originals = (Map<String, String>) context.getStore(NAMESPACE).get(Thread.currentThread().getName());

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

    private static Properties resolveConfiguredProperties(final Class<?> testClass, final Method testMethod) {
        var properties = resolveAppliedProperties(testClass, testMethod);

        if (properties.isEmpty()) {
            properties = resolvePropertiesField(testClass, SYSTEM_PROPERTIES);

            if (properties.isEmpty()) {
                properties = resolveMapField(testClass, SYSTEM_PROPERTIES);
            }
        }

        return properties.orElseThrow(() -> new IllegalArgumentException("No Properties have been configured."));
    }

    private static Optional<Properties> resolveAppliedProperties(final Class<?> testClass, final Method testMethod) {
        val applyPropertiesAnno = findAnnotation(testMethod, ApplyProperties.class);
        if (applyPropertiesAnno.isPresent()) {
            val fieldName = applyPropertiesAnno.get().value();
            val props = resolvePropertiesField(testClass, fieldName);
            return props.isEmpty() ? resolveMapField(testClass, fieldName) : props;
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Properties> resolvePropertiesField(final Class<?> testClass, final String fieldName) {
        return resolveField(testClass, fieldName, Properties.class);
    }

    @SuppressWarnings("unchecked")
    private static Optional<Properties> resolveMapField(final Class<?> testClass, final String fieldName) {
        return resolveField(testClass, fieldName, Map.class).map(m -> asProperties((Map<String, String>) m));
    }

    private static <F> Optional<F> resolveField(final Class<?> testClass, final String fieldName, final Class<? extends F> fieldType) {
        return findFields(
            testClass,
            f1 -> isStatic(f1) && f1.getType().equals(fieldType) && f1.getName().equals(fieldName),
            TOP_DOWN
        ).stream()
            .findFirst()
            .map(f -> extractValue(testClass, f, fieldType));
    }

}
