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

import io.github.cjstehno.testthings.fixtures.Person;
import io.github.cjstehno.testthings.junit.Resource;
import io.github.cjstehno.testthings.junit.ResourcesExtension;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static io.github.cjstehno.testthings.serdes.SerdesVerifiers.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(ResourcesExtension.class) @SuppressWarnings("unused")
class SerdesVerifiersTest {

    @Resource("/person-01.json") private static String personJson;
    @Resource("/person-01.xml") private static String personXml;
    @Resource("/person-01.ser") private static byte[] personSer;
    @Resource("/person-01.json") private Person person;

    @ParameterizedTest(name = "[{index}] with {0} expecting bytes")
    @MethodSource("providersWithBytes")
    void verifySerdesWithProviderExpectingBytes(final SerdesProvider provider, final byte[] expected) {
        verifySerdes(provider, person, expected);
    }

    @Test void verifySerdesWithProviderException() throws IOException {
        val provider = mock(SerdesProvider.class);
        when(provider.serializeToBytes(any())).thenThrow(new IOException("oops"));
        when(provider.serializeToString(any())).thenThrow(new IOException("oops"));

        var thrown = assertThrows(AssertionFailedError.class, () -> {
            verifySerdes(provider, person, personSer);
        });
        assertEquals("Exception thrown during serdes verification: oops", thrown.getMessage());

        thrown = assertThrows(AssertionFailedError.class, () -> {
            verifySerdes(provider, person);
        });
        assertEquals("Exception thrown during serdes verification: oops", thrown.getMessage());

        val thrown2 = assertThrows(AssertionError.class, () -> {
            verifySerdes(provider, person, equalTo(personJson));
        });
        assertEquals("Exception thrown during serdes verification: oops", thrown2.getMessage());
    }

    @Test void generateSampleObject() throws IOException {
        val bytes = new JavaObjectSerdes().serializeToBytes(person);
        Files.write(new File("/media/cjstehno/Storage/projects/test-things/src/test/resources/person-01.ser").toPath(), bytes, CREATE, WRITE);
    }

    @ParameterizedTest(name = "[{index}] with {0} expecting string")
    @MethodSource("providersWithStrings")
    void verifySerdesWithProviderExpectingString(final SerdesProvider provider, final String expected) {
        verifySerdes(provider, person, expected);
    }

    @ParameterizedTest(name = "[{index}] with {0} matching string")
    @MethodSource("providersWithStrings")
    void verifySerdesWithProviderMatchingString(final SerdesProvider provider, final String expected) {
        verifySerdes(provider, person, containsString("Layla"));
    }

    @ParameterizedTest(name = "[{index}] with {0} expecting string")
    @MethodSource("providers")
    void verifySerdesWithProvider(final SerdesProvider provider) {
        verifySerdes(provider, person);
    }

    @Test void verifyJsonSerdes() {
        verifySerdes(person);
        verifySerdes(person, personJson.getBytes(UTF_8));
        verifySerdes(person, personJson);
        verifySerdes(person, containsString("Layla"));
    }

    @ParameterizedTest(name = "[{index}] with {0} expecting bytes")
    @MethodSource("providersWithBytes")
    void verifySerializationWithProviderExpectingBytes(final SerdesProvider provider, final byte[] expected) {
        verifySerialization(provider, person, expected);
    }

    @ParameterizedTest(name = "[{index}] with {0} expecting string")
    @MethodSource("providersWithStrings")
    void verifySerializationWithProviderExpectingString(final SerdesProvider provider, final String expected) {
        verifySerialization(provider, person, expected);
    }

    @Test void verifyJsonSerialization() {
        verifySerialization(person, personJson);
        verifySerialization(person, personJson.getBytes(UTF_8));
    }

    @ParameterizedTest(name = "[{index}] with {0} expecting bytes")
    @MethodSource("providersWithBytes")
    void verifyDeserializationWithProviderExpectingBytes(final SerdesProvider provider, final byte[] expected) {
        verifyDeserialization(provider, expected, person);
    }

    @ParameterizedTest(name = "[{index}] with {0} expecting string")
    @MethodSource("providersWithStrings")
    void verifyDeserializationWithProviderExpectingString(final SerdesProvider provider, final String expected) {
        verifyDeserialization(provider, expected, person);
    }

    @Test void verifyJsonDeserialization() {
        verifyDeserialization(personJson.getBytes(UTF_8), person);
        verifyDeserialization(personJson, person);
    }

    @Test void verifySerializationWithException() throws IOException {
        val provider = mock(SerdesProvider.class);
        when(provider.serializeToBytes(any())).thenThrow(new IOException("oops"));

        val thrown = assertThrows(AssertionFailedError.class, () -> {
            verifySerialization(provider, person, personSer);
        });
        assertEquals("Exception thrown during serialization verification: oops", thrown.getMessage());
    }

    @Test void verifyDeserializationBytesWithException() throws IOException {
        val provider = mock(SerdesProvider.class);
        when(provider.deserialize(any(byte[].class), eq(Person.class))).thenThrow(new IOException("oops"));

        val thrown = assertThrows(AssertionFailedError.class, () -> {
            verifyDeserialization(provider,personSer, person);
        });
        assertEquals("Exception thrown during deserialization verification: oops", thrown.getMessage());
    }

    @Test void verifyDeserializationStringWithException() throws IOException {
        val provider = mock(SerdesProvider.class);
        when(provider.deserialize(any(String.class), eq(Person.class))).thenThrow(new IOException("oops"));

        val thrown = assertThrows(AssertionFailedError.class, () -> {
            verifyDeserialization(provider,personJson, person);
        });
        assertEquals("Exception thrown during deserialization verification: oops", thrown.getMessage());
    }

    private static Stream<Arguments> providers() {
        return Stream.of(
            Arguments.of(new JacksonJsonSerdes()),
            Arguments.of(new JacksonXmlSerdes())
        );
    }

    private static Stream<Arguments> providersWithStrings() {
        return Stream.of(
            Arguments.of(new JacksonJsonSerdes(), personJson),
            Arguments.of(new JacksonXmlSerdes(), personXml)
        );
    }

    private static Stream<Arguments> providersWithBytes() {
        return Stream.of(
            Arguments.of(new JacksonJsonSerdes(), personJson.getBytes(UTF_8)),
            Arguments.of(new JacksonXmlSerdes(), personXml.getBytes(UTF_8)),
            Arguments.of(new JavaObjectSerdes(), personSer)
        );
    }
}