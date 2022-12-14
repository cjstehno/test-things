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
package io.github.cjstehno.testthings.serdes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.hamcrest.Matcher;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test result verifiers useful for working with serialization and deserialization (serdes).
 * <p>
 * Most of these verifier methods take a {@link SerdesProvider}, though there are some which do not - they are provided
 * as simplifications using the {@link JacksonJsonSerdes} provider, since JSON tends to be a very common data format.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerdesVerifiers {

    /**
     * Verifies that the serialized object deserializes properly and that the generated bytes are as expected.
     *
     * @param provider the serdes provider
     * @param object   the object to be verified
     * @param expected the expected byte array
     */
    public static void verifySerdes(final SerdesProvider provider, final Object object, final byte[] expected) {
        try {
            val serialized = provider.serializeToBytes(object);
            assertArrayEquals(expected, serialized);

            val deserialized = provider.deserialize(serialized, object.getClass());
            assertEquals(object, deserialized);

        } catch (final IOException ioe) {
            fail("Exception thrown during serdes verification: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Verifies that the serialized object deserializes properly and that the generated bytes are as expected.
     * This verifier uses the {@link JacksonJsonSerdes} provider.
     *
     * @param object   the object to be verified
     * @param expected the expected byte array
     */
    public static void verifySerdes(final Object object, final byte[] expected) {
        verifySerdes(new JacksonJsonSerdes(), object, expected);
    }

    /**
     * Verifies that the serialized object deserializes properly.
     *
     * @param provider the serdes provider
     * @param object   the object to be verified
     */
    public static void verifySerdes(final SerdesProvider provider, final Object object) {
        try {
            val serialized = provider.serializeToBytes(object);
            val deserialized = provider.deserialize(serialized, object.getClass());
            assertEquals(object, deserialized);

        } catch (final IOException ioe) {
            fail("Exception thrown during serdes verification: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Verifies that the serialized object deserializes properly. This verifier uses the {@link JacksonJsonSerdes}
     * provider.
     *
     * @param object the object to be verified
     */
    public static void verifySerdes(final Object object) {
        verifySerdes(new JacksonJsonSerdes(), object);
    }

    /**
     * Verifies that the serialized object deserializes properly and that the generated string equals the provided one.
     * This verifier uses the {@link JacksonJsonSerdes} provider.
     *
     * @param object the object to be verified
     * @param string the serialized JSON string
     */
    public static void verifySerdes(final Object object, final String string) {
        verifySerdes(new JacksonJsonSerdes(), object, string);
    }

    /**
     * Verifies that the serialized object deserializes properly and that the generated string equals the provided one.
     *
     * @param provider the {@link SerdesProvider} to be used
     * @param object   the object to be verified
     * @param expected the serialized JSON string
     */
    public static void verifySerdes(final SerdesProvider provider, final Object object, final String expected) {
        verifySerdes(provider, object, equalTo(expected));
    }

    /**
     * Verifies that the serialized object deserializes properly and that the generated string matches the provided
     * matcher.
     *
     * @param provider      the {@link SerdesProvider} to be used
     * @param object        the object to be verified
     * @param stringMatcher the matcher used to verify the resulting serialized string
     */
    public static void verifySerdes(final SerdesProvider provider, final Object object, final Matcher<String> stringMatcher) {
        try {
            val serialized = provider.serializeToString(object);
            assertThat(serialized, stringMatcher);

            val deserialized = provider.deserialize(serialized, object.getClass());
            assertEquals(object, deserialized);

        } catch (final IOException ioe) {
            fail("Exception thrown during serdes verification: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Verifies that the serialized object deserializes properly and that the generated string matches the provided
     * matcher. This verifier uses the {@link JacksonJsonSerdes} provider.
     *
     * @param object        the object to be verified
     * @param stringMatcher the matcher used to verify the resulting serialized string
     */
    public static void verifySerdes(final Object object, final Matcher<String> stringMatcher) {
        verifySerdes(new JacksonJsonSerdes(), object, stringMatcher);
    }

    /**
     * Verifies that the object serializes to the expected bytes.
     *
     * @param provider the serdes provider
     * @param object   the object to be verified
     * @param expected the expected byte array
     */
    public static void verifySerialization(final SerdesProvider provider, final Object object, final byte[] expected) {
        try {
            assertArrayEquals(expected, provider.serializeToBytes(object));
        } catch (final IOException ioe) {
            fail("Exception thrown during serialization verification: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Verifies that the object serializes to the expected bytes. This verifier uses the {@link JacksonJsonSerdes}
     * provider.
     *
     * @param object   the object to be verified
     * @param expected the expected byte array
     */
    public static void verifySerialization(final Object object, final byte[] expected) {
        verifySerialization(new JacksonJsonSerdes(), object, expected);
    }

    /**
     * Verifies that the object serializes to the expected string.
     *
     * @param provider the serdes provider
     * @param object   the object to be verified
     * @param expected the expected string
     */
    public static void verifySerialization(final SerdesProvider provider, final Object object, final String expected) {
        try {
            assertEquals(expected, provider.serializeToString(object));
        } catch (final IOException ioe) {
            fail("Exception thrown during serialization verification", ioe);
        }
    }

    /**
     * Verifies that the object serializes to the expected string. This verifier uses the {@link JacksonJsonSerdes}
     * provider.
     *
     * @param object   the object to be verified
     * @param expected the expected string
     */
    public static void verifySerialization(final Object object, final String expected) {
        verifySerialization(new JacksonJsonSerdes(), object, expected);
    }

    /**
     * Verifies that the object deserializes from the bytes to the expected object.
     *
     * @param provider       the serdes provider
     * @param bytes          the serialized bytes
     * @param expectedObject the expected deserialized object
     */
    public static void verifyDeserialization(final SerdesProvider provider, final byte[] bytes, final Object expectedObject) {
        try {
            assertEquals(expectedObject, provider.deserialize(bytes, expectedObject.getClass()));
        } catch (final IOException ioe) {
            fail("Exception thrown during deserialization verification: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Verifies that the object deserializes from the bytes to the expected object. This verifier uses the {@link JacksonJsonSerdes}
     * provider.
     *
     * @param bytes    the serialized bytes
     * @param expected the expected deserialized object
     */
    public static void verifyDeserialization(final byte[] bytes, final Object expected) {
        verifyDeserialization(new JacksonJsonSerdes(), bytes, expected);
    }

    /**
     * Verifies that the object deserializes from the bytes to the expected object.
     *
     * @param provider       the serdes provider
     * @param string         the serialized string
     * @param expectedObject the expected deserialized object
     */
    public static void verifyDeserialization(final SerdesProvider provider, final String string, final Object expectedObject) {
        try {
            assertEquals(expectedObject, provider.deserialize(string, expectedObject.getClass()));
        } catch (final IOException ioe) {
            fail("Exception thrown during deserialization verification: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Verifies that the object deserializes from the bytes to the expected object. This verifier uses the {@link JacksonJsonSerdes}
     * provider.
     *
     * @param string   the serialized string
     * @param expected the expected deserialized object
     */
    public static void verifyDeserialization(final String string, final Object expected) {
        verifyDeserialization(new JacksonJsonSerdes(), string, expected);
    }
}
