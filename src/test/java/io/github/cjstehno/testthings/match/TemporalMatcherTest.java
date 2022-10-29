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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static io.github.cjstehno.testthings.match.TemporalMatcher.*;
import static java.time.LocalDate.parse;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TemporalMatcherTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final LocalDateTime TARGET_DATETIME = LocalDateTime.of(2022, OCTOBER, 1, 10, 25, 15, 0);
    private static final LocalDate TARGET_DATE = LocalDate.of(2022, OCTOBER, 1);

    @ParameterizedTest @CsvSource({
        "2022,true",
        "2021,false",
        "2023,false"
    })
    void inYear(final int year, final boolean expected) {
        assertEquals(expected, isInYear(year).matches(TARGET_DATE));
    }

    @ParameterizedTest @CsvSource({
        "10,true",
        "9,false",
        "11,false"
    })
    void hourOfDay(final int hod, final boolean expected) {
        assertEquals(expected, isInHourOfDay(hod).matches(TARGET_DATETIME));
    }

    @ParameterizedTest @CsvSource({
        "OCTOBER,true",
        "SEPTEMBER,false",
        "NOVEMBER,false"
    })
    void inMonth(final Month month, final boolean expected) {
        assertEquals(expected, isInMonth(month).matches(TARGET_DATE));
    }

    @ParameterizedTest @CsvSource({
        "10,true",
        "9,false",
        "11,false"
    })
    void inMonthInt(final int month, final boolean expected) {
        assertEquals(expected, isInMonth(month).matches(TARGET_DATE));
    }

    @ParameterizedTest @CsvSource({
        "SATURDAY,true",
        "FRIDAY,false",
        "SUNDAY,false"
    })
    void inDayOfWeek(final DayOfWeek dow, final boolean expected) {
        assertEquals(expected, isDayOfWeek(dow).matches(TARGET_DATE));
    }

    @ParameterizedTest @CsvSource({
        "6,true",
        "7,false",
        "1,false"
    })
    void inDayOfWeekInt(final int dow, final boolean expected) {
        assertEquals(expected, isDayOfWeek(dow).matches(TARGET_DATE));
    }

    @ParameterizedTest @CsvSource({
        "10/02/2022,false",
        "10/03/2022,true",
        "10/04/2022,true",
        "10/05/2022,true",
        "10/06/2022,true",
        "10/07/2022,true",
        "10/08/2022,false",
    })
    void weekday(final String date, final boolean expected) {
        assertEquals(expected, isWeekday().matches(parse(date, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "10/02/2022,true",
        "10/03/2022,false",
        "10/04/2022,false",
        "10/05/2022,false",
        "10/06/2022,false",
        "10/07/2022,false",
        "10/08/2022,true",
    })
    void weekend(final String date, final boolean expected) {
        assertEquals(expected, isWeekend().matches(parse(date, FORMATTER)));
    }
}