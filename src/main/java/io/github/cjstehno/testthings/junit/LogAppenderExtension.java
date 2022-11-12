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

import io.github.cjstehno.testthings.slf4j.AppenderConfig;
import io.github.cjstehno.testthings.slf4j.InMemoryLogAppender;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;

/**
 * JUnit 5 extension used to provide and manage the {@link InMemoryLogAppender} for testing. It will setup and tear down
 * the logging configuration, based on a default {@link AppenderConfig} field, or one specified by an {@link ApplyLogging}
 * annotation on the test method.
 * <p>
 * If a test method has a parameter of type {@link InMemoryLogAppender} it will be populated with the instance of the
 * appender for use in the test (for verification purposes).
 */
@Slf4j
public class LogAppenderExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final String APPENDER_CONFIG_NAME = "APPENDER_CONFIG";
    private static final String APPENDER = "appender";
    private static final Namespace NAMESPACE = create("test-things", "log-appender");

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        val appenderConfig = resolveAppenderConfig(context);
        val appender = new InMemoryLogAppender(appenderConfig);

        context.getStore(NAMESPACE).put(APPENDER, appender);

        appender.attach();
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        ((InMemoryLogAppender) context.getStore(NAMESPACE).remove(APPENDER)).detach();
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getRequiredTestMethod().isAnnotationPresent(Test.class)
            && parameterContext.getParameter().getType().equals(InMemoryLogAppender.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(APPENDER);
    }

    private static AppenderConfig resolveAppenderConfig(final ExtensionContext context) throws Exception {
        // use the method annotation value if present
        if (context.getRequiredTestMethod().isAnnotationPresent(ApplyLogging.class)) {
            val anno = context.getRequiredTestMethod().getAnnotation(ApplyLogging.class);
            return extractConfigValue(context, anno.value());
        }

        // resolve the default configuration
        return extractConfigValue(context, APPENDER_CONFIG_NAME);
    }

    private static AppenderConfig extractConfigValue(final ExtensionContext context, final String fieldName) {
        return findFields(
            context.getRequiredTestClass(),
            f -> AppenderConfig.class.isAssignableFrom(f.getType()) && f.getName().equals(fieldName),
            TOP_DOWN
        ).stream()
            .findFirst()
            .map(f -> {
                try {
                    f.setAccessible(true);
                    return (AppenderConfig) f.get(context.getRequiredTestInstance());
                } catch (IllegalAccessException e) {
                    log.error("Unable to access field ({}): {}", f.getName(), e.getMessage(), e);
                    return null;
                }
            })
            .orElseThrow();
    }
}
