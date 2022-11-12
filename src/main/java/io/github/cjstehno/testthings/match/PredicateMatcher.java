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

import java.util.function.Predicate;

/**
 * A Hamcrest matcher used to wrap a {@link Predicate} where the matcher will match if the predicate resolves to <code>true</code>.
 *
 * @param <T> the matched type
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateMatcher<T> extends BaseMatcher<T> {

    private final Predicate<T> predicate;
    private final String descr;

    /**
     * Creates the predicate matcher with a default description ("a supplied predicate").
     *
     * @param predicate the predicate
     * @param <T>       the matched type
     * @return the matcher
     */
    public static <T> Matcher<T> matchesPredicate(final Predicate<T> predicate) {
        return matchesPredicate(predicate, "a supplied predicate");
    }

    /**
     * Creates the predicate matcher with the provided description.
     *
     * @param predicate the predicate
     * @param descr     the description
     * @param <T>       the matched type
     * @return the matcher
     */
    public static <T> Matcher<T> matchesPredicate(final Predicate<T> predicate, final String descr) {
        return new PredicateMatcher<>(predicate, descr);
    }

    @Override public boolean matches(final Object actual) {
        try {
            return predicate.test((T) actual);
        } catch (ClassCastException cce) {
            return false;
        }
    }

    @Override public void describeTo(final Description description) {
        description.appendText("matches ");
        description.appendText(descr);
    }
}
