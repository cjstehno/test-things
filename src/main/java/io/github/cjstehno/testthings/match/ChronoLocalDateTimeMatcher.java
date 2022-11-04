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
package io.github.cjstehno.testthings.match;

import lombok.NoArgsConstructor;
import org.hamcrest.Matcher;

import java.time.chrono.ChronoLocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.*;

/**
 * Provides some matchers for working with <code>ChronoLocalDateTime</code> objects - most of these are just syntax aliases
 * on top of standard Hamcrest matchers - useful to make code more clear.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ChronoLocalDateTimeMatcher {

    /**
     * Matches a date-time that is before (less than but not equal to) the specified date value.
     *
     * @param dateTime the target date-time value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDateTime<?>> isBefore(final ChronoLocalDateTime<?> dateTime) {
        return lessThan(dateTime);
    }

    /**
     * Matches a date-time that is before or equal to the specified date-time value.
     *
     * @param dateTime the target date-time value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDateTime<?>> isEqualOrBefore(final ChronoLocalDateTime<?> dateTime) {
        return lessThanOrEqualTo(dateTime);
    }

    /**
     * Matches a date-time that is after (greater than but not equal to) the specified date-time value.
     *
     * @param dateTime the target date-time value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDateTime<?>> isAfter(final ChronoLocalDateTime<?> dateTime) {
        return greaterThan(dateTime);
    }

    /**
     * Matches a date-time that is after or equal to the specified date-time value.
     *
     * @param dateTime the target date-time value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDateTime<?>> isEqualOrAfter(final ChronoLocalDateTime<?> dateTime) {
        return greaterThanOrEqualTo(dateTime);
    }
}
