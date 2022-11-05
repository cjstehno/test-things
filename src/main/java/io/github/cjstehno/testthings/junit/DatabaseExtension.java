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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.cjstehno.testthings.Resources.resourceToString;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.support.ReflectionSupport.findFields;

/**
 * A JUnit 5 extension used to setup and tear down a database using a <code>DataSource</code>. A <code>DataSource</code>
 * field will be located and used to open connections to the database.
 * <p>
 * Use the {@link PrepareDatabase} annotation on test methods to provide the setup and teardown SQL scripts (from text
 * files on the classpath).
 * <p>
 * The database will be setup before each test method and torn down after each test method.
 *
 * <strong>Note:</strong> Often the {@link LifecycleExtension} is useful to aid in setting up the <code>DataSource</code>
 * instance before the extension executes.
 */
@Slf4j
public class DatabaseExtension implements BeforeEachCallback, AfterEachCallback {

    // FIXME: add a method lookup so you can provide a factory method for the dataSourc - how to tear down?
    // - maybe just by name - if setupDataSource and tearDownDataSource exist, they are used
    // - ma6ybe add to PrepareDatabase (init, destroy) for DS manage (or default to below, --> then configured
    //  this would allow DS config per test method, which could be useful
    // TEST - make sure that having PrepareDatabase is allowed on type - should apply to all test methods
    /*
        DataSource someName(){} - setup

        void someName(DataSource){} - destroy
     */

    /**
     * Before each test method annotated with the {@link PrepareDatabase} annotation it will run the "setup" scripts,
     * in order.
     *
     * @param context the current extension context; never {@code null}
     * @throws Exception if there is a problem
     */
    @Override public void beforeEach(final ExtensionContext context) throws Exception {
        findAnnotation(context.getRequiredTestMethod(), PrepareDatabase.class).ifPresent(anno -> {
            executeScripts(context.getRequiredTestInstance(), anno.setup());
        });

        log.info("The database is set-up.");
    }

    /**
     * A helper method to provide a connection on the DataSource, closing it after the operations.
     *
     * @param ds                   the data source
     * @param connectionOperations the operations to be performed
     * @throws SQLException if there is a problem
     */
    public static void withConnection(final DataSource ds, Consumer<Connection> connectionOperations) throws SQLException {
        try (val conn = ds.getConnection()) {
            connectionOperations.accept(conn);
        }
    }

    /**
     * A helper method to provide a connection and statement on the DataSource, closing it when done.
     *
     * @param ds                  the data source
     * @param statementOperations the operations
     * @param <R>                 the type of the return value
     * @return an optional return value
     * @throws SQLException if there is a problem
     */
    public static <R> R withStatement(final DataSource ds, Function<Statement, R> statementOperations) throws SQLException {
        try (val conn = ds.getConnection()) {
            try (val stmt = conn.createStatement()) {
                return statementOperations.apply(stmt);
            }
        }
    }

    /**
     * After each test method annotated with the {@link PrepareDatabase} annotation, it will run the scripts defined
     * in the "teardown" property, in order.
     *
     * @param context the current extension context; never {@code null}
     * @throws Exception if there is a problem
     */
    @Override public void afterEach(final ExtensionContext context) throws Exception {
        findAnnotation(context.getRequiredTestMethod(), PrepareDatabase.class).ifPresent(anno -> {
            executeScripts(context.getRequiredTestInstance(), anno.teardown());
        });

        log.info("The database was torn-down.");
    }

    private static void executeScripts(final Object testInstance, final String[] scripts) {
        for (val script : scripts) {
            executeScript(testInstance, script);
        }
    }

    private static void executeScript(final Object testInstance, final String script) {
        try (val conn = findDataSource(testInstance).getConnection()) {
            try (val stmt = conn.createStatement()) {
                stmt.execute(resourceToString(script));
                conn.commit();
                log.info("Executed database script ({}).", script);
            }
        } catch (final Exception e) {
            log.error("Unable to execute script ({}): {}", script, e.getMessage(), e);
        }
    }

    private static DataSource findDataSource(final Object testInstance) {
        return findFields(
            testInstance.getClass(),
            f -> DataSource.class.isAssignableFrom(f.getType()),
            TOP_DOWN
        ).stream().findFirst().map(f -> {
            try {
                f.setAccessible(true);
                return (DataSource) f.get(testInstance);
            } catch (IllegalAccessException e) {
                log.error("Unable to find DataSource implementation field.");
                return null;
            }
        }).orElseThrow();
    }
}
