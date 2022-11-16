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

import lombok.val;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.testthings.match.ByteArrayMatcher.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByteArrayMatcherTest {

    private final byte[] bytes = "These are bytes!".getBytes();

    @Test void equal() {
        val matcher = arrayEqualTo(bytes);
        assertTrue(matcher.matches("These are bytes!".getBytes()));
        assertFalse(matcher.matches("These not bytes.".getBytes()));
    }

    @Test void startsWith() {
        val matcher = arrayStartsWith("These are".getBytes());
        assertTrue(matcher.matches(bytes));
        assertFalse(matcher.matches("Other bytes".getBytes()));
    }

//    @Test void endsWith() {
//        val matcher = arrayEndsWith("bytes!".getBytes());
//        assertTrue(matcher.matches(bytes));
//        assertFalse(matcher.matches("These are string!".getBytes()));
//    }

    @Test void length() {
        val matcher = arrayLengthIs(bytes.length);
        assertTrue(matcher.matches(bytes));
        assertFalse(matcher.matches("These are strings!".getBytes()));
    }
}