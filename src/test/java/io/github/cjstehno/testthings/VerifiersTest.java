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

import io.github.cjstehno.testthings.fixtures.BirthGender;
import io.github.cjstehno.testthings.fixtures.Person;
import io.github.cjstehno.testthings.rando.PersonRandomizer;
import io.github.cjstehno.testthings.rando.Randomizer;
import lombok.val;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.testthings.Verifiers.verifyEqualsAndHashCode;
import static io.github.cjstehno.testthings.Verifiers.verifyToString;
import static io.github.cjstehno.testthings.fixtures.BirthGender.MALE;
import static io.github.cjstehno.testthings.rando.PersonRandomizer.randomPerson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class VerifiersTest {

    private static Person person(){
        return new Person("Alfred", MALE, 65);
    }

    @Test void equalsAndHashSuccess(){
        verifyEqualsAndHashCode(VerifiersTest::person);
        verifyEqualsAndHashCode(person(), person());
    }

    @Test void equalsAndHashFailure(){
        assertThrows(AssertionError.class, () -> {
            verifyEqualsAndHashCode(person(), randomPerson().one());
        });
    }

    @Test void string(){
        val expected = "Person(name=Alfred, birthGender=MALE, age=65)";

        verifyToString(expected, VerifiersTest::person);
        verifyToString(equalTo(expected), VerifiersTest::person);

        verifyToString(expected, person());
        verifyToString(expected, person());
    }
}