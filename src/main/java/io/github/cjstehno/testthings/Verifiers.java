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
package io.github.cjstehno.testthings;

import lombok.NoArgsConstructor;
import org.hamcrest.Matcher;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Some utility methods useful for verifying common functionality of objects.
 */
@NoArgsConstructor(access = PRIVATE)
public final class Verifiers {

    /**
     * An alias to the <code>verifyEqualsAndHashCode(Object,Object)</code> method, calling the provided
     * <code>Supplier&lt;Object&gt;</code> for each. See that method description for details.
     *
     * @param supplier a supplier that will be called twice to provide the objects to be used in the test
     */
    public static void verifyEqualsAndHashCode(final Supplier<Object> supplier) {
        verifyEqualsAndHashCode(supplier.get(), supplier.get());
    }

    /**
     * Used to verify that the <code>equals(Object)</code> and <code>hashCode()</code> methods return values that meet
     * with the standard contract for those methods.
     *
     * The two instances provided should be equivalent, but not the same instance.
     *
     * @param instanceA the first instance
     * @param instanceB the second instance
     */
    public static void verifyEqualsAndHashCode(final Object instanceA, final Object instanceB) {
        assertThat(instanceA, equalTo(instanceA));
        assertThat(instanceB, equalTo(instanceB));
        assertThat(instanceA, equalTo(instanceB));
        assertThat(instanceB, equalTo(instanceA));
        assertThat(instanceA.hashCode(), equalTo(instanceA.hashCode()));
        assertThat(instanceB.hashCode(), equalTo(instanceB.hashCode()));
        assertThat(instanceA.hashCode(), equalTo(instanceB.hashCode()));
        assertThat(instanceB.hashCode(), equalTo(instanceA.hashCode()));
    }

    /**
     * Used to verify that the <code>toString()</code> method of the object provided by the
     * <code>Supplier&lt;Object&gt;</code> matches the expected String.
     *
     * @param expected the expected string
     * @param supplier a supplier that will be used to generate the object to be tested
     */
    public static void verifyToString(final String expected, final Supplier<Object> supplier) {
        verifyToString(expected, supplier.get());
    }

    /**
     * Used to verify that the <code>toString()</code> method of the object provided by the
     * <code>Supplier&lt;Object&gt;</code> matches the given Matcher.
     *
     * @param matcher the Matcher for the generated string
     * @param supplier a supplier that will be used to generate the object to be tested
     */
    public static void verifyToString(final Matcher<String> matcher, final Supplier<Object> supplier) {
        verifyToString(matcher, supplier.get());
    }

    /**
     * Used to verify that the <code>toString()</code> method of the object provided by the matches the given Matcher.
     *
     * @param matcher the Matcher for the generated string
     * @param obj the object to be tested
     */
    public static void verifyToString(final Matcher<String> matcher, final Object obj) {
        assertThat(obj.toString(), matcher);
    }

    /**
     * Used to verify that the <code>toString()</code> method of the object provided by the matches the given string.
     *
     * @param expected the expected string value
     * @param obj the object to be tested
     */
    public static void verifyToString(final String expected, final Object obj) {
        verifyToString(equalTo(expected), obj);
    }

    // TODO: move to AtomicVerifiers? or create a matcher for atomics

    /**
     * Verifies that the value of the provided {@link AtomicInteger} matches the expected value.
     *
     * @param expected the expected int value
     * @param value the actual value wrapped in the AtomicInteger
     */
    public static void verifyAtomicInteger(final int expected, final AtomicInteger value) {
        assertEquals(expected, value.get());
    }
}
