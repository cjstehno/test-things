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

import static io.github.cjstehno.testthings.junit.DatabaseExtensionTest.assertRecordCount;

@ExtendWith(DatabaseExtension.class) @Slf4j
public class MoreDatabaseExtensionTest {

    DataSource create() {
        val ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
        log.info("Created DataSource.");
        return ds;
    }

    void destroy(final DataSource dataSource) {
        ((JdbcConnectionPool) dataSource).dispose();
        log.info("Destroyed DataSource.");
    }

    DataSource createDataSource() {
        val ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
        log.info("Created default DataSource.");
        return ds;
    }

    void destroyDataSource(final DataSource dataSource) {
        ((JdbcConnectionPool) dataSource).dispose();
        log.info("Destroyed default DataSource.");
    }

    @Test @DisplayName("Preparation in method")
    @PrepareDatabase(
        creator = "create",
        setup = {"/db-setup.sql", "/db-populate.sql"},
        teardown = {"/db-truncate.sql", "/db-teardown.sql"},
        destroyer = "destroy"
    )
    void scenarioX(final DataSource ds) {
        assertRecordCount(ds, 2);
    }

    @Test @DisplayName("Preparation in method")
    @PrepareDatabase(
        setup = {"/db-setup.sql", "/db-populate-2.sql"},
        teardown = {"/db-truncate.sql", "/db-teardown.sql"}
    )
    void scenarioY(final DataSource ds) {
        assertRecordCount(ds, 3);
    }
}
