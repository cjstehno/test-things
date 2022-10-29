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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LogNameMatcher extends BaseMatcher<ILoggingEvent> {

    private final Matcher<String> nameMatcher;

    public static LogNameMatcher loggerName(final Matcher<String> matcher) {
        return new LogNameMatcher(matcher);
    }

    @Override public boolean matches(final Object actual) {
        return nameMatcher.matches(((ILoggingEvent) actual).getLoggerName());
    }

    @Override public void describeTo(final Description description) {
        // FIXME: impl
    }
}
