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
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;

import static java.time.DayOfWeek.*;
import static java.time.temporal.ChronoField.*;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporalMatcher extends BaseMatcher<Temporal> {

            /*
    FIXME: implement

    - for datetime (above-ish +)
        - isInMorning
        - isInAfternoon
        - isInHourOfDay
        - before after (or with equals)
        - between

        how do these test results work with LocalDate vs LocalDateTime?
     */

    private final TemporalField temporalField;
    private final Matcher<Integer> matcher;

    public static Matcher<Temporal> fieldIsEqualTo(final TemporalField field, int value) {
        return new TemporalMatcher(field, equalTo(value));
    }

    public static Matcher<Temporal> fieldIsGreaterThan(final TemporalField field, int value) {
        return new TemporalMatcher(field, greaterThan(value));
    }

    public static Matcher<Temporal> fieldIsGreaterThanOrEqualTo(final TemporalField field, int value) {
        return new TemporalMatcher(field, greaterThanOrEqualTo(value));
    }

    public static Matcher<Temporal> fieldIsLessThan(final TemporalField field, int value) {
        return new TemporalMatcher(field, lessThan(value));
    }

    public static Matcher<Temporal> fieldIsLessThanOrEqualTo(final TemporalField field, int value) {
        return new TemporalMatcher(field, lessThanOrEqualTo(value));
    }

    public static Matcher<Temporal> isInYear(final int year) {
        return fieldIsEqualTo(YEAR, year);
    }

    public static Matcher<Temporal> isInMonth(final Month month) {
        return fieldIsEqualTo(MONTH_OF_YEAR, month.getValue());
    }

    public static Matcher<Temporal> isInMonth(final int month) {
        return isInMonth(Month.of(month));
    }

    public static Matcher<Temporal> isInHourOfDay(final int hour){
        return fieldIsEqualTo(HOUR_OF_DAY, hour);
    }

    public static Matcher<Temporal> isDayOfWeek(final DayOfWeek dow) {
        return fieldIsEqualTo(DAY_OF_WEEK, dow.getValue());
    }

    public static Matcher<Temporal> isDayOfWeek(final int dow) {
        return isDayOfWeek(DayOfWeek.of(dow));
    }

    public static Matcher<Temporal> isWeekday() {
        return anyOf(
            isDayOfWeek(MONDAY),
            isDayOfWeek(TUESDAY),
            isDayOfWeek(WEDNESDAY),
            isDayOfWeek(THURSDAY),
            isDayOfWeek(FRIDAY)
        );
    }

    public static Matcher<Temporal> isWeekend() {
        return anyOf(
            isDayOfWeek(SATURDAY),
            isDayOfWeek(SUNDAY)
        );
    }

    @Override public boolean matches(final Object actual) {
        return matcher.matches(((Temporal) actual).get(temporalField));
    }

    @Override public void describeTo(Description description) {
        // FIXME: impl
    }
}
