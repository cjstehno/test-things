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
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

import static io.github.cjstehno.testthings.junit.DatabaseExtension.withStatement;
import static io.github.cjstehno.testthings.junit.Lifecycle.LifecyclePoint.AFTER_EACH;
import static io.github.cjstehno.testthings.junit.Lifecycle.LifecyclePoint.BEFORE_EACH;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({LifecycleExtension.class, DatabaseExtension.class})
class DatabaseExtensionTest {

    private JdbcConnectionPool dataSource;

    @Lifecycle(BEFORE_EACH) void before() {
        dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
    }

    @Test @PrepareDatabase(
        setup = {"/db-setup.sql"},
        teardown = {"/db-truncate.sql"}
    )
    void emptyDatabase() throws SQLException {
        assertEquals(0, recordsCount());
    }

    @Test @PrepareDatabase(
        setup = {"/db-setup.sql", "/db-populate.sql"},
        teardown = {"/db-truncate.sql", "/db-teardown.sql"}
    )
    void preparedDatabase() throws SQLException {
        assertEquals(2, recordsCount());
    }

    public int recordsCount() throws SQLException {
        return withStatement(dataSource, stmt -> {
            try {
                val rs = stmt.executeQuery("select count(*) from scores");
                rs.next();
                return rs.getInt(1);
            } catch (Exception ex) {
                return -1;
            }
        });
    }

    @Lifecycle(AFTER_EACH) void after() {
        dataSource.dispose();
    }
}