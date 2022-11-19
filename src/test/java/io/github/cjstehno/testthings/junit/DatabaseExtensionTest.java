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
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.cjstehno.testthings.junit.Lifecycle.LifecyclePoint.BEFORE_EACH;
import static io.github.cjstehno.testthings.match.AtomicMatcher.atomicIntIs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({LifecycleExtension.class, DatabaseExtension.class})
@PrepareDatabase(
    creator = "create",
    setup = {"/db-setup.sql", "/db-populate.sql"},
    teardown = {"/db-truncate.sql", "/db-teardown.sql"},
    destroyer = "destroy"
)
@Slf4j
public class DatabaseExtensionTest {

    private AtomicInteger createCounter;
    private AtomicInteger otherCreateCounter;

    @Lifecycle(BEFORE_EACH) void beforeEach() {
        createCounter = new AtomicInteger(0);
        otherCreateCounter = new AtomicInteger(0);
    }

    DataSource create() {
        val ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
        createCounter.incrementAndGet();
        log.info("Created DataSource.");
        return ds;
    }

    void destroy(final DataSource dataSource) {
        ((JdbcConnectionPool) dataSource).dispose();
        log.info("Destroyed DataSource.");
    }

    DataSource createOther() {
        val ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
        otherCreateCounter.incrementAndGet();
        log.info("Created other DataSource.");
        return ds;
    }

    void destroyOther(final DataSource dataSource) {
        ((JdbcConnectionPool) dataSource).dispose();
        log.info("Destroyed other DataSource.");
    }

    @Test @DisplayName("All preparation in class annotation.")
    void scenariosAandE(final DataSource ds) {
        assertThat(createCounter, atomicIntIs(equalTo(1)));
        assertThat(otherCreateCounter, atomicIntIs(equalTo(0)));
        assertRecordCount(ds, 2);
    }

    @Test @DisplayName("Preparation in class annotation and method - not additive")
    @PrepareDatabase(
        creator = "createOther",
        setup = {"/db-setup.sql", "/db-populate-2.sql"},
        teardown = {"/db-truncate.sql", "/db-teardown.sql"},
        destroyer = "destroyOther",
        additive = false
    )
    void scenariosDandG(final DataSource ds) {
        assertThat(createCounter, atomicIntIs(equalTo(0)));
        assertThat(otherCreateCounter, atomicIntIs(equalTo(1)));
        assertRecordCount(ds, 3);
    }

    @Test @DisplayName("Preparation in class annotation and method - additive")
    @PrepareDatabase(setup = {"/db-populate-2.sql"})
    void scenariosCandG(final DataSource ds) {
        assertThat(createCounter, atomicIntIs(equalTo(1)));
        assertThat(otherCreateCounter, atomicIntIs(equalTo(0)));
        assertRecordCount(ds, 5);
    }

    public static void assertRecordCount(final DataSource ds, final int expectedCount) {
        try (val conn = ds.getConnection()) {
            try (val stmt = conn.createStatement()) {
                val rs = stmt.executeQuery("select count(*) from scores");
                rs.next();
                assertEquals(expectedCount, rs.getInt(1), "Record count mismatch");
            }
        } catch (Exception ex) {
            fail("Problem executing count statement: " + ex.getMessage());
        }
    }
}
