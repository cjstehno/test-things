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
package io.github.cjstehno.testthings.fixtures;

import io.github.cjstehno.testthings.serdes.JacksonJsonSerdes;
import lombok.val;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.testthings.Verifiers.verifyEqualsAndHashCode;
import static io.github.cjstehno.testthings.Verifiers.verifyToString;
import static io.github.cjstehno.testthings.fixtures.BirthGender.MALE;
import static io.github.cjstehno.testthings.serdes.SerdesVerifiers.verifySerdes;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    private static Person person() {
        return new Person("Chris", MALE, 50);
    }

    @Test void properties() {
        val person = person();
        assertEquals("Chris", person.getName());
        assertEquals(MALE, person.getBirthGender());
        assertEquals(50, person.getAge());
    }

    @Test void serdes() {
        verifySerdes(
            new JacksonJsonSerdes(), // TODO: add a shortcut for this
            person(),
            "{\"name\":\"Chris\",\"birthGender\":\"MALE\",\"age\":50}"
        );
    }

    @Test void string() {
        verifyToString("Person(name=Chris, birthGender=MALE, age=50)", PersonTest::person);
    }

    @Test void equalsAndHash(){
        verifyEqualsAndHashCode(PersonTest::person);
    }
}