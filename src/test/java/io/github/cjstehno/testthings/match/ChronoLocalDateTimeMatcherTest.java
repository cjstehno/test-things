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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.github.cjstehno.testthings.match.ChronoLocalDateTimeMatcher.*;
import static java.time.LocalDateTime.parse;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronoLocalDateTimeMatcherTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final LocalDateTime TARGET_DATETIME = LocalDateTime.of(2022, OCTOBER, 1, 10, 25, 15, 0);

    @ParameterizedTest @CsvSource({
        "09/30/2022 23:59:59,true",
        "10/01/2022 10:25:15,false",
        "10/01/2021 10:25:15,true",
        "11/01/2022 10:25:15,false"
    })
    void before(final String dateTime, final boolean expected) {
        assertEquals(expected, isBefore(TARGET_DATETIME).matches(parse(dateTime, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "09/30/2022 23:59:59,true",
        "10/01/2022 10:25:15,true",
        "10/01/2021 10:25:15,true",
        "11/01/2022 10:25:15,false"
    })
    void beforeOrEqual(final String dateTime, final boolean expected) {
        assertEquals(expected, isEqualOrBefore(TARGET_DATETIME).matches(parse(dateTime, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "09/30/2022 23:59:59,false",
        "10/01/2022 10:25:15,false",
        "10/01/2021 10:25:15,false",
        "11/01/2022 10:25:15,true"
    })
    void after(final String dateTime, final boolean expected) {
        assertEquals(expected, isAfter(TARGET_DATETIME).matches(parse(dateTime, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "09/30/2022 23:59:59,false",
        "10/01/2022 10:25:15,true",
        "10/01/2021 10:25:15,false",
        "11/01/2022 10:25:15,true"
    })
    void afterOrEqual(final String dateTime, final boolean expected) {
        assertEquals(expected, isEqualOrAfter(TARGET_DATETIME).matches(parse(dateTime, FORMATTER)));
    }
}