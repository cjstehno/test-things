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

import io.github.cjstehno.testthings.fixtures.BirthGender;
import io.github.cjstehno.testthings.fixtures.Person;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.cjstehno.testthings.fixtures.BirthGender.FEMALE;
import static org.junit.jupiter.api.Assertions.*;

class JacksonJsonSerdesTest {

    private JacksonJsonSerdes serdes;
    private Person person;

    @BeforeEach void beforeEach(){
        serdes = new JacksonJsonSerdes();
        person = new Person("Jessica", FEMALE, 33);
    }

    @Test void serdesBytes() throws IOException {
        val bytes = serdes.serializeToBytes(person);
        val obj = serdes.deserialize(bytes, Person.class);

        assertEquals(person, obj);
    }

    @Test void serdesString() throws IOException {
        val json = serdes.serializeToString(person);
        val obj = serdes.deserialize(json, Person.class);

        assertEquals(person, obj);
    }
}