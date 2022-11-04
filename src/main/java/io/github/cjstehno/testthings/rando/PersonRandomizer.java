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
import io.github.cjstehno.testthings.fixtures.FemaleName;
import io.github.cjstehno.testthings.fixtures.MaleName;
import io.github.cjstehno.testthings.fixtures.Person;
import lombok.NoArgsConstructor;
import lombok.val;

import static io.github.cjstehno.testthings.rando.CoreRandomizers.oneOf;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anIntBetween;

/**
 * A randomizer used to generate random <code>Person</code> instances from the fixtures.
 */
@NoArgsConstructor(staticName = "person")
public class PersonRandomizer implements Randomizer<Person> {

    @Override public Person one() {
        val birthGender = oneOf(BirthGender.class).one();

        val nameRando = switch (birthGender) {
            case MALE -> oneOf(MaleName.class);
            case FEMALE -> oneOf(FemaleName.class);
        };

        return new Person(nameRando.one().toString(), birthGender, anIntBetween(1, 100).one());
    }
}
