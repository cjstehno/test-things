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

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PRIVATE;

/**
 * Hamcrest matchers for matching Atomic values.
 */
@NoArgsConstructor(access = PRIVATE)
public final class AtomicMatcher {

    /**
     * Creates a matcher for matching an {@link AtomicInteger} value against a provided matcher.
     *
     * @param matcher the value matcher
     * @return the configured matcher
     */
    public static Matcher<AtomicInteger> atomicIntIs(final Matcher<Integer> matcher) {
        return new AtomicIntegerMatching(matcher);
    }

    /**
     * Creates a matcher for matching an {@link AtomicBoolean} value.
     *
     * @param value the expected value
     * @return the configured matcher
     */
    public static Matcher<AtomicBoolean> atomicBooleanEqualTo(final boolean value) {
        return new AtomicBooleanMatching(value);
    }

    /**
     * Creates a matcher for matching an {@link AtomicReference} value against a provided matcher.
     *
     * @param matcher the value matcher
     * @param <V> the value type
     * @return the configured matcher
     */
    public static <V> Matcher<AtomicReference<V>> atomicReferenceIs(final Matcher<V> matcher) {
        return new AtomicReferenceMatching<>(matcher);
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class AtomicIntegerMatching extends BaseMatcher<AtomicInteger> {
        private final Matcher<Integer> matcher;

        @Override public boolean matches(final Object actual) {
            return matcher.matches(((AtomicInteger) actual).get());
        }

        @Override public void describeTo(final Description description) {
            description.appendText("an atomic integer matching ");
            matcher.describeTo(description);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class AtomicReferenceMatching<V> extends BaseMatcher<AtomicReference<V>> {
        private final Matcher<V> matcher;

        @Override @SuppressWarnings("unchecked")
        public boolean matches(final Object actual) {
            return matcher.matches(((AtomicReference<V>) actual).get());
        }

        @Override public void describeTo(final Description description) {
            description.appendText("an atomic reference matching ");
            matcher.describeTo(description);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class AtomicBooleanMatching extends BaseMatcher<AtomicBoolean> {
        private final boolean value;

        @Override public boolean matches(final Object actual) {
            return ((AtomicBoolean) actual).get() == value;
        }

        @Override public void describeTo(final Description description) {
            description.appendText("an atomic boolean equal to " + value);
        }
    }
}
