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
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.cjstehno.testthings.fixtures.BirthGender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JavaObjectSerdesTest {

    private static Person person() {
        return new Person("Fred", MALE, 44);
    }

    @Test void serializeNonSerializable() {
        val thrown = assertThrows(UnsupportedOperationException.class, () -> {
            new JavaObjectSerdes().serializeToBytes(new Object());
        });
        assertEquals("The object does not implement Serializable.", thrown.getMessage());
    }

    @Test void deserializeNonSerializable() {
        val thrown = assertThrows(UnsupportedOperationException.class, () -> {
            new JavaObjectSerdes().deserialize(new byte[0], Object.class);
        });
        assertEquals("The object does not implement Serializable.", thrown.getMessage());
    }

    @Test void serdes() throws IOException {
        val person = person();

        val serdes = new JavaObjectSerdes();
        val bytes = serdes.serializeToBytes(person);

        assertEquals(283, bytes.length);

        val obj = serdes.deserialize(bytes, Person.class);
        assertEquals(person, obj);
    }

    @Test void noStringSerializationSupport() {
        val thrown = assertThrows(UnsupportedOperationException.class, () -> {
            new JavaObjectSerdes().serializeToString(person());
        });
        assertEquals("Java Object Serialization does not support serialization to String.", thrown.getMessage());
    }

    @Test void noStringDeserializationSupport() {
        val thrown = assertThrows(UnsupportedOperationException.class, () -> {
            new JavaObjectSerdes().deserialize("", Person.class);
        });
        assertEquals("Java Object Serialization does not support deserialization from String.", thrown.getMessage());
    }
}