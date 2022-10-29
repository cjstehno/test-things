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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.github.cjstehno.testthings.match.ChronoLocalDateMatcher.*;
import static java.time.LocalDate.parse;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronoLocalDateMatcherTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final LocalDate TARGET_DATE = LocalDate.of(2022, OCTOBER, 1);

    @ParameterizedTest @CsvSource({
        "10/01/2022,false",
        "10/02/2022,false",
        "09/30/2022,true",
        "10/01/2021,true",
        "10/01/2023,false"
    })
    void before(final String date, final boolean expected) {
        assertEquals(expected, isBefore(TARGET_DATE).matches(parse(date, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "10/01/2022,true",
        "10/02/2022,false",
        "09/30/2022,true",
        "10/01/2021,true",
        "10/01/2023,false"
    })
    void equalOrBefore(final String date, final boolean expected) {
        assertEquals(expected, isEqualOrBefore(TARGET_DATE).matches(parse(date, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "10/01/2022,false",
        "10/02/2022,true",
        "09/30/2022,false",
        "10/01/2021,false",
        "10/01/2023,true"
    })
    void after(final String date, final boolean expected) {
        assertEquals(expected, isAfter(TARGET_DATE).matches(parse(date, FORMATTER)));
    }

    @ParameterizedTest @CsvSource({
        "10/01/2022,true",
        "10/02/2022,true",
        "09/30/2022,false",
        "10/01/2021,false",
        "10/01/2023,true"
    })
    void equalOrAfter(final String date, final boolean expected) {
        assertEquals(expected, isEqualOrAfter(TARGET_DATE).matches(parse(date, FORMATTER)));
    }
}