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

import io.github.cjstehno.testthings.serdes.SerdesProvider;
import lombok.NoArgsConstructor;
import lombok.val;
import org.hamcrest.Matcher;

import java.io.IOException;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// FIXME: document - used to verify conditions
@NoArgsConstructor(access = PRIVATE)
public final class Verifiers {

    // FIXME: note that the generated instance should be the same data just not the same instace
    public static void verifyEqualsAndHashCode(final Supplier<Object> supplier) {
        verifyEqualsAndHashCode(supplier.get(), supplier.get());
    }

    // FIXME: note that the generated instance should be the same data just not the same instace
    public static void verifyEqualsAndHashCode(final Object instanceA, final Object instanceB) {
        // TODO: add expection labels
        assertThat(instanceA, equalTo(instanceA));
        assertThat(instanceB, equalTo(instanceB));
        assertThat(instanceA, equalTo(instanceB));
        assertThat(instanceB, equalTo(instanceA));
        assertThat(instanceA.hashCode(), equalTo(instanceA.hashCode()));
        assertThat(instanceB.hashCode(), equalTo(instanceB.hashCode()));
        assertThat(instanceA.hashCode(), equalTo(instanceB.hashCode()));
        assertThat(instanceB.hashCode(), equalTo(instanceA.hashCode()));
    }

    // FIXME: document
    public static void verifyToString(final String expected, final Supplier<Object> supplier) {
        verifyToString(expected, supplier.get());
    }

    public static void verifyToString(final Matcher<String> matcher, final Supplier<Object> supplier) {
        verifyToString(matcher, supplier.get());
    }

    public static void verifyToString(final Matcher<String> matcher, final Object obj) {
        assertThat(obj.toString(), matcher);
    }

    // FIXME: document
    public static void verifyToString(final String expected, final Object obj) {
        verifyToString(equalTo(expected), obj);
    }

    // FIXME: serdes matcher version

    public static void verifySerdes(final SerdesProvider provider, final Object object, final byte[] expected) {
        try {
            val serialized = provider.serializeToBytes(object);
            assertArrayEquals(expected, serialized);

            val deserialized = provider.deserialize(serialized, object.getClass());
            assertEquals(object, deserialized);

        } catch (final IOException ioe) {
            fail("Exception thrown during serdes verification", ioe);
        }
    }

    public static void verifySerdes(final SerdesProvider provider, final Object object) {
        try {
            val serialized = provider.serializeToBytes(object);
            val deserialized = provider.deserialize(serialized, object.getClass());
            assertEquals(object, deserialized);

        } catch (final IOException ioe) {
            fail("Exception thrown during serdes verification", ioe);
        }
    }

    // FIXME: document
    public static void verifySerdes(final SerdesProvider provider, final Object object, final String expected) {
        try {
            val serialized = provider.serializeToString(object);
            assertEquals(expected, serialized);

            val deserialized = provider.deserialize(serialized, object.getClass());
            assertEquals(object, deserialized);

        } catch (final IOException ioe) {
            fail("Exception thrown during serdes verification", ioe);
        }
    }

    public static void verifySerialization(final SerdesProvider provider, final Object object, final byte[] expected) {
        try {
            assertArrayEquals(expected, provider.serializeToBytes(object));
        } catch (final IOException ioe) {
            fail("Exception thrown during serialization verification", ioe);
        }
    }

    public static void verifySerialization(final SerdesProvider provider, final Object object, final String expected) {
        try {
            assertEquals(expected, provider.serializeToString(object));
        } catch (final IOException ioe) {
            fail("Exception thrown during serialization verification", ioe);
        }
    }

    public static void verifyDeserialization(final SerdesProvider provider, final byte[] bytes, final Object expectedObject) {
        try {
            assertEquals(expectedObject, provider.deserialize(bytes, expectedObject.getClass()));
        } catch (final IOException ioe) {
            fail("Exception thrown during deserialization verification", ioe);
        }
    }

    public static void verifyDeserialization(final SerdesProvider provider, final String string, final Object expectedObject) {
        try {
            assertEquals(expectedObject, provider.deserialize(string, expectedObject.getClass()));
        } catch (final IOException ioe) {
            fail("Exception thrown during deserialization verification", ioe);
        }
    }
}
