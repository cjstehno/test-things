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
import org.junit.jupiter.api.extension.ExtendWith;

import static ch.qos.logback.classic.Level.*;
import static io.github.cjstehno.testthings.slf4j.match.LogLevelMatcher.logLevelEqualTo;
import static io.github.cjstehno.testthings.slf4j.match.LogMessageMatcher.logMessage;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LogAppenderExtension.class)
class LogAppenderExtensionTest {

    @SuppressWarnings("unused")
    private static final AppenderConfig APPENDER_CONFIG = AppenderConfig.configure().loggedClass(SomeService.class);

    @SuppressWarnings("unused")
    private final AppenderConfig alternateConfig = AppenderConfig.configure()
        .loggerNamed(SomeService.class.getName())
        .filter(evt -> evt.getLevel().isGreaterOrEqual(WARN));

    @Test void testing(final InMemoryLogAppender appender) {
        val service = new SomeService();
        service.stuff();
        service.error();

        assertEquals(2, appender.count());
        assertEquals(0, appender.count(logLevelEqualTo(DEBUG)));
        assertEquals(1, appender.count(logLevelEqualTo(INFO)));
        assertEquals(0, appender.count(logLevelEqualTo(WARN)));
        assertEquals(1, appender.count(logLevelEqualTo(ERROR)));

        assertTrue(appender.hasEvent(logMessage(equalTo("Hey, I'm loggin' here!"))));
        assertTrue(appender.hasEvent(logMessage(equalTo("Something bad has happened."))));
    }

    @Test @ApplyLogging("alternateConfig")
    void alternateTesting(final InMemoryLogAppender appender) {
        val service = new SomeService();
        service.stuff();
        service.error();

        assertEquals(1, appender.count());
        assertEquals(0, appender.count(logLevelEqualTo(DEBUG)));
        assertEquals(0, appender.count(logLevelEqualTo(INFO)));
        assertEquals(0, appender.count(logLevelEqualTo(WARN)));
        assertEquals(1, appender.count(logLevelEqualTo(ERROR)));

        assertFalse(appender.hasEvent(logMessage(equalTo("Hey, I'm loggin' here!"))));
        assertTrue(appender.hasEvent(logMessage(equalTo("Something bad has happened."))));
    }

    @Slf4j static class SomeService {
        void stuff() {
            log.info("Hey, I'm loggin' here!");
        }

        void error() {
            log.error("Something bad has happened.");
        }
    }
}