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

import io.github.cjstehno.testthings.TestVerifiers;
import lombok.val;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.github.cjstehno.testthings.TestVerifiers.assertMatcherDescription;
import static io.github.cjstehno.testthings.match.ByteArrayMatcher.*;
import static org.junit.jupiter.api.Assertions.*;

class ByteArrayMatcherTest {

    private final byte[] bytes = "These are bytes!".getBytes();

    @Test void equal() {
        val matcher = arrayEqualTo(bytes);
        assertTrue(matcher.matches("These are bytes!".getBytes()));
        assertFalse(matcher.matches("These not bytes.".getBytes()));

        assertMatcherDescription("an array of bytes equal to another array of bytes", matcher);
    }

    @Test void startsWith() {
        val matcher = arrayStartsWith("These are".getBytes());
        assertTrue(matcher.matches(bytes));
        assertFalse(matcher.matches("Other bytes".getBytes()));

        assertMatcherDescription("an array of bytes starting with the prefix bytes", matcher);
    }

    @Test void length() {
        val matcher = arrayLengthIs(bytes.length);
        assertTrue(matcher.matches(bytes));
        assertFalse(matcher.matches("These are strings!".getBytes()));

        assertMatcherDescription("matches a byte array with length 16", matcher);
    }

    @ParameterizedTest @CsvSource({
        "are,true",
        "es,true",
        "blah,false",
        "s!,true"
    })
    void contains(final String subString, final boolean expected) {
        val matcher = arrayContains(subString.getBytes());
        assertEquals(expected, matcher.matches(bytes));

        assertMatcherDescription("an array of bytes containing the sub-array of bytes", matcher);
    }

}