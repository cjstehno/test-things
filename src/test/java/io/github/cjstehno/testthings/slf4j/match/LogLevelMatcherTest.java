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
package io.github.cjstehno.testthings.slf4j.match;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.val;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.qos.logback.classic.Level.*;
import static io.github.cjstehno.testthings.slf4j.match.LogLevelMatcher.logLevelAtLeast;
import static io.github.cjstehno.testthings.slf4j.match.LogLevelMatcher.logLevelEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogLevelMatcherTest {

    private ILoggingEvent debugEvent;
    private ILoggingEvent infoEvent;
    private ILoggingEvent warnEvent;
    private ILoggingEvent errorEvent;

    @BeforeEach void beforeEach() {
        debugEvent = mockEvent(DEBUG);
        infoEvent = mockEvent(INFO);
        warnEvent = mockEvent(WARN);
        errorEvent = mockEvent(ERROR);
    }

    private static ILoggingEvent mockEvent(final Level level) {
        val event = mock(ILoggingEvent.class, level.toString());
        when(event.getLevel()).thenReturn(level);
        return event;
    }

    @Test void equalTo() {
        assertThat(debugEvent, logLevelEqualTo(DEBUG));
        assertThat(infoEvent, logLevelEqualTo(INFO));
        assertThat(warnEvent, logLevelEqualTo(WARN));
        assertThat(errorEvent, logLevelEqualTo(ERROR));
    }

    @Test void atLeast() {
        assertThat(debugEvent, logLevelAtLeast(DEBUG));
        assertThat(infoEvent, logLevelAtLeast(INFO));
        assertThat(warnEvent, logLevelAtLeast(WARN));
        assertThat(errorEvent, logLevelAtLeast(ERROR));

        assertThat(debugEvent, not(logLevelAtLeast(INFO)));
        assertThat(infoEvent, logLevelAtLeast(INFO));
        assertThat(warnEvent, logLevelAtLeast(INFO));
        assertThat(errorEvent, logLevelAtLeast(INFO));
    }

    @Test void equalToDescription(){
        val matcher = logLevelEqualTo(WARN);

        val description = new StringDescription();
        matcher.describeTo(description);

        assertEquals("A logging-event with level equal to WARN", description.toString());
    }

    @Test void atLeastDescription(){
        val matcher = logLevelAtLeast(WARN);

        val description = new StringDescription();
        matcher.describeTo(description);

        assertEquals("A logging-event with level equal to or greater than WARN", description.toString());
    }
}