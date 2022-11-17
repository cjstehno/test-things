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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;

import static io.github.cjstehno.testthings.Resources.resourceToString;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ModifierSupport.isStatic;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;
import static org.junit.platform.commons.support.ReflectionSupport.findMethod;

/**
 * A JUnit 5 extension used to setup and tear down a database using a provided {@link DataSource}.
 *
 * The {@link PrepareDatabase} annotation may be applied at the class or test method level to append or override the
 * setup and teardown methods.
 *
 * If a test method is given a {@link DataSource} parameter, it will be populated with the current data source for that
 * method for use in the test.
 *
 * See the User Guide for more details and examples.
 */
@Slf4j
public class DatabaseExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final Namespace NAMESPACE = create("test-things", "database");
    private static final String DATA_SOURCE = "data-source";
    private static final String DEFAULT_CREATOR = "createDataSource";
    private static final String DEFAULT_DESTROYER = "destroyDataSource";

    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        val dataSource = createDataSource(context).orElseThrow();

        context.getStore(NAMESPACE).put(DATA_SOURCE, dataSource);

        runSetupScripts(context, dataSource);

        log.info("The database is set-up.");
    }

    @Override public void afterEach(final ExtensionContext context) throws Exception {
        val dataSource = (DataSource) context.getStore(NAMESPACE).remove(DATA_SOURCE);

        runTeardownScripts(context, dataSource);
        destroyDataSource(context, dataSource);

        log.info("The database was torn-down.");
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getRequiredTestMethod().isAnnotationPresent(Test.class)
            && DataSource.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(DATA_SOURCE);
    }

    private static Optional<DataSource> invokeDataSourceCreator(final ExtensionContext context, final String methodName) {
        val dataSourceMethod = findMethod(context.getRequiredTestClass(), methodName).orElseThrow();
        val target = isStatic(dataSourceMethod) ? context.getRequiredTestClass() : context.getRequiredTestInstance();
        try {
            return Optional.ofNullable((DataSource) dataSourceMethod.invoke(target));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<DataSource> createDataSource(final ExtensionContext context) {
        // check test method annotation (on test method)
        if (isTestMethodAnnotated(context)) {
            val anno = getMethodAnnotation(context);
            if (!anno.creator().isBlank()) {
                return invokeDataSourceCreator(context, anno.creator());
            }
        }

        // check class method annotation (on test class)
        if (isTestClassAnnotated(context)) {
            val anno = getClassAnnotation(context);
            if (!anno.creator().isBlank()) {
                return invokeDataSourceCreator(context, anno.creator());
            }
        }

        // check defined provider method
        if (findMethod(context.getRequiredTestClass(), DEFAULT_CREATOR).isPresent()) {
            return invokeDataSourceCreator(context, DEFAULT_CREATOR);
        }

        // check field of DataSource type (static or not)
        val dataSourceField = findFields(context.getRequiredTestClass(), f -> DataSource.class.isAssignableFrom(f.getType()), TOP_DOWN);
        if (!dataSourceField.isEmpty()) {
            val target = isStatic(dataSourceField.get(0)) ? context.getRequiredTestClass() : context.getRequiredTestInstance();
            try {
                return Optional.ofNullable((DataSource) dataSourceField.get(0).get(target));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    private static void invokeDataSourceDestroyer(final ExtensionContext context, final String methodName, final DataSource dataSource) {
        val dataSourceMethod = findMethod(context.getRequiredTestClass(), methodName, DataSource.class).orElseThrow();
        val target = isStatic(dataSourceMethod) ? context.getRequiredTestClass() : context.getRequiredTestInstance();
        try {
            dataSourceMethod.invoke(target, dataSource);
        } catch (Exception e) {
            log.error("Unable to destroy DataSource using ({}): {}", methodName, e.getMessage(), e);
        }
    }

    private static void destroyDataSource(final ExtensionContext context, final DataSource dataSource) {
        log.info("Destroying data source.");

        // check test method annotation (on test method)
        if (isTestMethodAnnotated(context)) {
            val anno = getMethodAnnotation(context);
            if (!anno.destroyer().isBlank()) {
                invokeDataSourceDestroyer(context, anno.destroyer(), dataSource);
            }
        }

        // check class method annotation (on test class)
        if (isTestClassAnnotated(context)) {
            val anno = getClassAnnotation(context);
            if (!anno.destroyer().isBlank()) {
                invokeDataSourceDestroyer(context, anno.destroyer(), dataSource);
            }
        }

        // check defined provider method
        if (findMethod(context.getRequiredTestClass(), DEFAULT_DESTROYER, DataSource.class).isPresent()) {
            invokeDataSourceDestroyer(context, DEFAULT_DESTROYER, dataSource);
        }

        // check field of DataSource type (static or not)
        val dataSourceField = findFields(context.getRequiredTestClass(), f -> DataSource.class.isAssignableFrom(f.getType()), TOP_DOWN);
        if (!dataSourceField.isEmpty()) {
            val target = isStatic(dataSourceField.get(0)) ? context.getRequiredTestClass() : context.getRequiredTestInstance();
            try {
                dataSourceField.get(0).set(target, null);
            } catch (IllegalAccessException e) {
                log.error("Unable to destroy DataSource (field): {}", e.getMessage(), e);
            }
        }
    }

    private static void runSetupScripts(final ExtensionContext context, final DataSource dataSource) {
        if (isTestMethodAnnotated(context)) {
            val methodAnno = getMethodAnnotation(context);

            if (methodAnno.additive()) {
                // run any scripts from the class annotation
                findClassPrepareDatabase(context).ifPresent(classAnno -> {
                    runScripts(dataSource, classAnno.setup());
                });
            }

            // run any scripts from the method annotation
            runScripts(dataSource, methodAnno.setup());

        } else {
            findClassPrepareDatabase(context).ifPresent(classAnno -> {
                runScripts(dataSource, classAnno.setup());
            });
        }
    }

    private static void runTeardownScripts(final ExtensionContext context, final DataSource dataSource) {
        if (isTestMethodAnnotated(context)) {
            val methodAnno = getMethodAnnotation(context);

            if (methodAnno.additive()) {
                // run any scripts from the class annotation
                findClassPrepareDatabase(context).ifPresent(classAnno -> {
                    runScripts(dataSource, classAnno.teardown());
                });
            }

            // run any scripts from the method annotation
            runScripts(dataSource, methodAnno.teardown());

        } else {
            findClassPrepareDatabase(context).ifPresent(classAnno -> {
                runScripts(dataSource, classAnno.teardown());
            });
        }
    }

    private static Optional<PrepareDatabase> findClassPrepareDatabase(final ExtensionContext context) {
        if (isTestClassAnnotated(context)) {
            return Optional.of(getClassAnnotation(context));
        }
        return Optional.empty();
    }

    private static void runScripts(final DataSource dataSource, final String[] scripts) {
        if (scripts != null) {
            try (val conn = dataSource.getConnection()) {
                for (val scriptPath : scripts) {
                    runScript(conn, scriptPath);
                }
                log.info("Done running scripts.");

            } catch (Exception ex) {
                log.error("Connection problem while running scripts: {}", ex.getMessage(), ex);
            }
        }
    }

    private static void runScript(final Connection conn, final String scriptPath) {
        if (!scriptPath.isBlank()) {
            try (val stmt = conn.createStatement()) {
                stmt.execute(resourceToString(scriptPath));
                conn.commit();
                log.info("Executed database script ({}).", scriptPath);

            } catch (Exception se) {
                log.error("Problem executing database script ({}): {}", scriptPath, se.getMessage(), se);
            }
        }
    }

    private static PrepareDatabase getMethodAnnotation(final ExtensionContext context) {
        return context.getRequiredTestMethod().getAnnotation(PrepareDatabase.class);
    }

    private static PrepareDatabase getClassAnnotation(final ExtensionContext context) {
        return context.getRequiredTestClass().getAnnotation(PrepareDatabase.class);
    }

    private static boolean isTestMethodAnnotated(final ExtensionContext context) {
        return context.getRequiredTestMethod().isAnnotationPresent(PrepareDatabase.class);
    }

    private static boolean isTestClassAnnotated(final ExtensionContext context) {
        return context.getRequiredTestClass().isAnnotationPresent(PrepareDatabase.class);
    }

}
