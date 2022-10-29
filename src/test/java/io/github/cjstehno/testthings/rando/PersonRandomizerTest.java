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
package io.github.cjstehno.testthings.rando;

import io.github.cjstehno.testthings.fixtures.BirthGender;
import io.github.cjstehno.testthings.fixtures.MaleName;
import io.github.cjstehno.testthings.fixtures.Person;
import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.cjstehno.testthings.fixtures.BirthGender.FEMALE;
import static io.github.cjstehno.testthings.fixtures.BirthGender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
class PersonRandomizerTest {

    @Test void randomPerson() {
        val rando = PersonRandomizer.person();

        assertEquals(new Person("Luke", MALE, 31), rando.one());
        assertEquals(new Person("John", MALE, 54), rando.one());
        assertEquals(new Person("William", MALE, 2), rando.one());
        assertEquals(new Person("Lily", FEMALE, 78), rando.one());
    }
}