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
package io.github.cjstehno.testthings.slf4j;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.qos.logback.classic.Level.*;
import static io.github.cjstehno.testthings.slf4j.match.LogLevelMatcher.logLevel;
import static io.github.cjstehno.testthings.slf4j.match.LogMessageMatcher.logMessage;
import static io.github.cjstehno.testthings.slf4j.match.LogNameMatcher.loggerName;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryLogAppenderTest {

    private InMemoryLogAppender appender = new InMemoryLogAppender(cfg -> {
        cfg.loggedClass(Alpha.class);
        cfg.loggedClass(Bravo.class);
    });

    @BeforeEach void beforeEach() {
        appender.attach();
    }

    @AfterEach void afterEach() {
        appender.detach();
    }

    @Test void logging() {
        val alpha = new Alpha();
        val bravo = new Bravo();

        alpha.execute();
        bravo.execute();

        assertEquals(2, appender.count());

        assertEquals(0, appender.count(logLevel(ERROR)));
        assertEquals(1, appender.count(logLevel(WARN)));
        assertEquals(1, appender.count(logLevel(INFO)));
        assertEquals(2, appender.count());

        assertTrue(appender.hasEvent(allOf(
            loggerName(endsWith("Alpha")),
            logLevel(INFO),
            logMessage(equalTo("Doing something."))
        )));

        assertTrue(appender.hasEvent(allOf(
            loggerName(endsWith("Bravo")),
            logLevel(WARN),
            logMessage(equalTo("Hey, look out!"))
        )));
    }

    @Slf4j static class Alpha {

        void execute() {
            log.info("Doing something.");
        }
    }

    @Slf4j static class Bravo {

        void execute() {
            log.warn("Hey, look out!");
        }
    }
}
