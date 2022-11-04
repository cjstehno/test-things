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
package io.github.cjstehno.testthings.rando;

import lombok.NoArgsConstructor;
import lombok.val;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.RandomStringUtils.*;

/**
 * A collection of randomizers for generating strings.
 */
@NoArgsConstructor(access = PRIVATE)
public final class StringRandomizers {

    // FIXME: patternedStrign?
    // FIXME: random string with set of chars

    // FIXME: need to use shared random to allow for testing and pinning

    public static Randomizer<String> string(final Randomizer<Integer> countRando) {
        return () -> random(countRando.one());
    }

    public static Randomizer<String> string(final Randomizer<Integer> countRando, final String characters) {
        return () -> random(countRando.one(), characters);
    }

    public static Randomizer<String> text(final Randomizer<Integer> lineWidthRando, final Randomizer<Integer> lineCountRando) {
        return () -> {
            val text = new StringBuilder();
            for (int i = 0; i < lineCountRando.one(); i++) {
                text.append(string(lineWidthRando)).append("\n");
            }
            return text.toString();
        };
    }

    public static Randomizer<String> numbers(final Randomizer<Integer> countRando) {
        return () -> randomNumeric(countRando.one());
    }

    public static Randomizer<Character> aChar() {
        return () -> randomAlphanumeric(1).toCharArray()[0];
    }

    public static Randomizer<char[]> aCharArray(final Randomizer<Integer> countRando) {
        return () -> string(countRando).one().toCharArray();
    }
}
