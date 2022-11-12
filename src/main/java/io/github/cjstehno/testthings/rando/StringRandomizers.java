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

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static java.util.Locale.ROOT;
import static java.util.stream.Collectors.joining;
import static lombok.AccessLevel.PRIVATE;

/**
 * A collection of randomizers for generating strings.
 */
@NoArgsConstructor(access = PRIVATE)
public final class StringRandomizers {

    /**
     * Letters of the alphabet (upper case).
     */
    public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Numbers from 0 to 9.
     */
    public static final String NUMBERS = "0123456789";

    /**
     * Selects a random value from the characters of the given string.
     *
     * @param string the string of values
     * @return the randomizer
     */
    public static Randomizer<String> oneFrom(final String string) {
        return () -> {
            val random = current();
            val ch = String.valueOf(string.charAt(random.nextInt(string.length())));
            return random.nextBoolean() ? ch.toLowerCase(ROOT) : ch.toUpperCase(ROOT);
        };
    }

    /**
     * Generates a random letter (upper or lower case).
     *
     * @return the randomizer
     */
    public static Randomizer<String> letter() {
        return () -> oneFrom(LETTERS).one();
    }

    /**
     * Generates a random number string (one number).
     *
     * @return the randomizer
     */
    public static Randomizer<String> number() {
        return () -> oneFrom(NUMBERS).one();
    }

    /**
     * Generates a random alphabetic string (upper and lower case).
     *
     * @param countRando the randomizer used to determine the length of the string
     * @return the randomizer
     */
    public static Randomizer<String> alphabetic(final Randomizer<Integer> countRando) {
        return () -> letter().stream(countRando.one()).collect(joining(""));
    }

    /**
     * Generates a random numeric string (with multiple digits).
     *
     * @param countRando the randomizer used to determine the length of the string
     * @return the randomizer
     */
    public static Randomizer<String> numeric(final Randomizer<Integer> countRando) {
        return () -> number().stream(countRando.one()).collect(joining(""));
    }

    /**
     * Generates a random alphanumeric string (upper and lower case).
     *
     * @param countRando the randomizer used to determine the length of the string
     * @return the randomizer
     */
    public static Randomizer<String> alphanumeric(final Randomizer<Integer> countRando) {
        return () -> oneFrom(LETTERS + NUMBERS).stream(countRando.one()).collect(joining(""));
    }

    /**
     * Generates multiple alphabetic strings of random size, returned as an array of words.
     *
     * @param countRando    the randomizer used to determine the number of words
     * @param wordSizeRando the randomizer used to determine the size of each word
     * @return the randomizer
     */
    public static Randomizer<String[]> words(final Randomizer<Integer> countRando, final Randomizer<Integer> wordSizeRando) {
        return () -> {
            val words = new String[countRando.one()];
            for (int w = 0; w < words.length; w++) {
                words[w] = alphabetic(wordSizeRando).one();
            }
            return words;
        };
    }

    /**
     * Randomly selects a word from the provided sentence (words are delimited by spaces).
     *
     * @param sentence the sentence
     * @return the randomizer
     */
    public static Randomizer<String> wordFrom(final String sentence) {
        return () -> wordFrom(sentence.split(" ")).one();
    }

    /**
     * Randomly selects a word from the provided words.
     *
     * @param words the available words
     * @return the randomizer
     */
    public static Randomizer<String> wordFrom(final String[] words) {
        return () -> words[current().nextInt(words.length)];
    }

    /**
     * Randomly generates a character (letters only).
     *
     * @return the randomizer
     */
    public static Randomizer<Character> aChar() {
        return () -> letter().one().charAt(0);
    }

    /**
     * Randomly generates a character array of random length.
     *
     * @param countRando the randomizer used to determine the length of the array
     * @return the randomizer
     */
    public static Randomizer<char[]> charArray(final Randomizer<Integer> countRando) {
        return () -> alphanumeric(countRando).one().toCharArray();
    }
}
