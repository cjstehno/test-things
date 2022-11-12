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

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.val;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.testthings.slf4j.match.LogMessageMatcher.logMessage;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogMessageMatcherTest {

    @Test void matching() {
        val event = mock(ILoggingEvent.class);
        when(event.getFormattedMessage()).thenReturn("Something interesting in the logs.");

        assertThat(event, logMessage(startsWith("Something interesting")));
    }

    @Test void description() {
        val matcher = logMessage(endsWith("Foo bar."));

        val descr = new StringDescription();
        matcher.describeTo(descr);

        assertEquals("A log-event with message matching a string ending with \"Foo bar.\"", descr.toString());
    }
}