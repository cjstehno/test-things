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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.Locale;

import static java.time.DayOfWeek.*;
import static java.time.temporal.ChronoField.*;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

/**
 * Matchers used to match temporal-based criteria.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporalMatcher extends BaseMatcher<Temporal> {

    private final TemporalField temporalField;
    private final Matcher<Integer> matcher;

    /**
     * Matches a temporal field that is equal to the specified value.
     *
     * @param field the temporal field
     * @param value the target value
     * @return the matcher
     */
    public static Matcher<Temporal> fieldIsEqualTo(final TemporalField field, int value) {
        return new TemporalMatcher(field, equalTo(value));
    }

    /**
     * Matches a temporal field that is greater than the specified value.
     *
     * @param field the temporal field
     * @param value the target value
     * @return the matcher
     */
    public static Matcher<Temporal> fieldIsGreaterThan(final TemporalField field, int value) {
        return new TemporalMatcher(field, greaterThan(value));
    }

    /**
     * Matches a temporal field that is greater than or equal to the specified value.
     *
     * @param field the temporal field
     * @param value the target value
     * @return the matcher
     */
    public static Matcher<Temporal> fieldIsGreaterThanOrEqualTo(final TemporalField field, int value) {
        return new TemporalMatcher(field, greaterThanOrEqualTo(value));
    }

    /**
     * Matches a temporal field that is less than the specified value.
     *
     * @param field the temporal field
     * @param value the target value
     * @return the matcher
     */
    public static Matcher<Temporal> fieldIsLessThan(final TemporalField field, int value) {
        return new TemporalMatcher(field, lessThan(value));
    }

    /**
     * Matches a temporal field that is less than or equal to the specified value.
     *
     * @param field the temporal field
     * @param value the target value
     * @return the matcher
     */
    public static Matcher<Temporal> fieldIsLessThanOrEqualTo(final TemporalField field, int value) {
        return new TemporalMatcher(field, lessThanOrEqualTo(value));
    }

    /**
     * Matches a YEAR value with the specified value.
     *
     * @param year the year
     * @return the matcher
     */
    public static Matcher<Temporal> isInYear(final int year) {
        return fieldIsEqualTo(YEAR, year);
    }

    /**
     * Matches a MONTH_OF_YEAR value with the specified value.
     *
     * @param month the month of the year
     * @return the matcher
     */
    public static Matcher<Temporal> isInMonth(final Month month) {
        return fieldIsEqualTo(MONTH_OF_YEAR, month.getValue());
    }

    /**
     * Matches a MONTH_OF_YEAR value with the specified value.
     *
     * @param month the month of the year
     * @return the matcher
     */
    public static Matcher<Temporal> isInMonth(final int month) {
        return isInMonth(Month.of(month));
    }

    /**
     * Matches a HOUR_OF_DAY value with the specified value.
     *
     * @param hour the hour of the day
     * @return the matcher
     */
    public static Matcher<Temporal> isInHourOfDay(final int hour) {
        return fieldIsEqualTo(HOUR_OF_DAY, hour);
    }

    /**
     * Matches an HOUR_OF_DAY value after midnight and before noon.
     *
     * @return the matcher
     */
    public static Matcher<Temporal> isInMorning() {
        return allOf(
            fieldIsGreaterThanOrEqualTo(HOUR_OF_DAY, 0),
            fieldIsLessThan(HOUR_OF_DAY, 12)
        );
    }

    /**
     * Matches an HOUR_OF_DAY value of noon or later, but before midnight.
     *
     * @return the matcher
     */
    public static Matcher<Temporal> isInAfternoon() {
        return allOf(
            fieldIsGreaterThanOrEqualTo(HOUR_OF_DAY, 12),
            fieldIsLessThan(HOUR_OF_DAY, 24)
        );
    }

    /**
     * Matches a DAY_OF_WEEK value with the specified value.
     *
     * @param dow day of the week
     * @return the matcher
     */
    public static Matcher<Temporal> isDayOfWeek(final DayOfWeek dow) {
        return fieldIsEqualTo(DAY_OF_WEEK, dow.getValue());
    }

    /**
     * Matches a DAY_OF_WEEK value with the specified value.
     *
     * @param dow day of the week
     * @return the matcher
     */
    public static Matcher<Temporal> isDayOfWeek(final int dow) {
        return isDayOfWeek(DayOfWeek.of(dow));
    }

    /**
     * Matches a DAY_OF_WEEK value for a weekday (i.e. MONDAY through FRIDAY inclusive).
     *
     * @return the matcher
     */
    public static Matcher<Temporal> isWeekday() {
        return anyOf(
            isDayOfWeek(MONDAY),
            isDayOfWeek(TUESDAY),
            isDayOfWeek(WEDNESDAY),
            isDayOfWeek(THURSDAY),
            isDayOfWeek(FRIDAY)
        );
    }

    /**
     * Matches a DAY_OF_WEEK value for a weekend (i.e. SATURDAY or SUNDAY).
     *
     * @return the matcher
     */
    public static Matcher<Temporal> isWeekend() {
        return anyOf(
            isDayOfWeek(SATURDAY),
            isDayOfWeek(SUNDAY)
        );
    }

    @Override public boolean matches(final Object actual) {
        return matcher.matches(((Temporal) actual).get(temporalField));
    }

    @Override public void describeTo(final Description description) {
        description.appendText("a " + temporalField.getDisplayName(Locale.ROOT) + " matching ");
        matcher.describeTo(description);
    }
}
