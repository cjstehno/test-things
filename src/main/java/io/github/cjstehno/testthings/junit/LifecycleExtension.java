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

import io.github.cjstehno.testthings.junit.Lifecycle.LifecyclePoint;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.ModifierSupport;

import static io.github.cjstehno.testthings.junit.Lifecycle.LifecyclePoint.*;
import static io.github.cjstehno.testthings.util.Reflections.invokeMethod;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotatedMethods;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;

/**
 * JUnit 5 extension used to provide a bit more control of the before/after lifecycle methods.
 *
 * Extensions run before the local test instance before/after methods - this can lead to chicken-and-egg issues from
 * time to time. This extension allow you to define methods that will be run before and after, based on where this
 * extension is defined in the list of extensions.
 *
 * For example, if you want to execute a specific before and after method before all of the extensions and before all
 * the other local lifecycle methods, you can configure this extension as the first one in the list, and then the
 * annotated method will execute before all of the other extensions, and then after all of the others at the end.
 */
@Slf4j
public class LifecycleExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    @Override public void beforeAll(final ExtensionContext context) throws Exception {
        invokeMatchingStaticMethods(context.getRequiredTestClass(), BEFORE_ALL);
    }

    /**
     * Finds all the methods annotated with the <code>Lifecycle(BEFORE)</code> annotation and executes them.
     *
     * @param context the current extension context; never {@code null}
     * @throws Exception if there is a problem
     */
    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        invokeMatchingMethods(context.getRequiredTestInstance(), BEFORE_EACH);
    }

    /**
     * Finds all the methods annotated with the <code>Lifecycle(AFTER)</code> annotation, and executes them.
     *
     * @param context the current extension context; never {@code null}
     * @throws Exception if there is a problem
     */
    @Override public void afterEach(final ExtensionContext context) throws Exception {
        invokeMatchingMethods(context.getRequiredTestInstance(), AFTER_EACH);
    }

    @Override public void afterAll(final ExtensionContext context) throws Exception {
        invokeMatchingStaticMethods(context.getRequiredTestClass(), AFTER_ALL);
    }

    private static void invokeMatchingMethods(final Object testInstance, final LifecyclePoint point) {
        findAnnotatedMethods(testInstance.getClass(), Lifecycle.class, TOP_DOWN).stream()
            .filter(m -> m.getAnnotationsByType(Lifecycle.class)[0].value() == point)
            .findAny()
            .ifPresent(m -> invokeMethod(testInstance, m));
    }

    private static void invokeMatchingStaticMethods(final Class<?> testClass, final LifecyclePoint point) {
        findAnnotatedMethods(testClass, Lifecycle.class, TOP_DOWN).stream()
            .filter(ModifierSupport::isStatic)
            .filter(m -> m.getAnnotationsByType(Lifecycle.class)[0].value() == point)
            .findAny()
            .ifPresent(m -> invokeMethod(testClass, m));
    }
}
