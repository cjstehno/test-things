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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static lombok.AccessLevel.PRIVATE;

/**
 * A Hamcrest matcher used to match log level criteria on a logging event.
 */
@RequiredArgsConstructor(access = PRIVATE)
public abstract class LogLevelMatcher extends BaseMatcher<ILoggingEvent> {

    @Getter private final Level level;

    /**
     * Creates a log level matcher matching the configured log level.
     *
     * @param level the target level
     * @return the matcher
     */
    public static Matcher<ILoggingEvent> logLevelEqualTo(final Level level) {
        return new IsLevelEqualToMatcher(level);
    }

    /**
     * Creates a log level matcher matching the configured or higher log level.
     *
     * @param level the target level minimum
     * @return the matcher
     */
    public static Matcher<ILoggingEvent> logLevelAtLeast(final Level level) {
        return new IsLevelAtLeastMatcher(level);
    }

    private static class IsLevelEqualToMatcher extends LogLevelMatcher {

        private IsLevelEqualToMatcher(final Level level) {
            super(level);
        }

        @Override public boolean matches(final Object actual) {
            return ((ILoggingEvent) actual).getLevel().equals(getLevel());
        }

        @Override public void describeTo(final Description description) {
            description.appendText("A logging-event with level equal to " + getLevel());
        }
    }

    private static class IsLevelAtLeastMatcher extends LogLevelMatcher {

        private IsLevelAtLeastMatcher(final Level level) {
            super(level);
        }

        @Override public boolean matches(final Object actual) {
            return ((ILoggingEvent) actual).getLevel().isGreaterOrEqual(getLevel());
        }

        @Override public void describeTo(final Description description) {
            description.appendText("A logging-event with level equal to or greater than " + getLevel());
        }
    }
}
