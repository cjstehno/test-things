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

import java.time.chrono.ChronoLocalDate;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.*;

/**
 * Provides some matchers for working with <code>ChronoLocalDate</code> objects - most of these are just syntax aliases
 * on top of standard Hamcrest matchers - useful to make code more clear.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ChronoLocalDateMatcher {

    /**
     * Matches a date that is before (less than but not equal to) the specified date value.
     *
     * @param date the target date value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDate> isBefore(final ChronoLocalDate date) {
        return lessThan(date);
    }

    /**
     * Matches a date that is after (greater than but not equal to) the specified date value.
     *
     * @param date the target date value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDate> isAfter(final ChronoLocalDate date) {
        return greaterThan(date);
    }

    /**
     * Matches a date that is before or equal to the specified date value.
     *
     * @param date the target date value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDate> isEqualOrBefore(final ChronoLocalDate date) {
        return lessThanOrEqualTo(date);
    }

    /**
     * Matches a date that is after or equal to the specified date value.
     *
     * @param date the target date value
     * @return the matcher
     */
    public static Matcher<ChronoLocalDate> isEqualOrAfter(final ChronoLocalDate date) {
        return greaterThanOrEqualTo(date);
    }
}
