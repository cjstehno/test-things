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

import io.github.cjstehno.testthings.fixtures.UnisexName;
import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.LinkedList;
import java.util.List;

import static io.github.cjstehno.testthings.fixtures.PhoneticAlphabet.CHARLIE;
import static io.github.cjstehno.testthings.rando.CoreRandomizers.oneOf;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anIntBetween;
import static io.github.cjstehno.testthings.rando.ObjectRandomizer.randomize;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
class ObjectRandomizerTest {

    // TODO: more complex things: maps, lists, arrays (these would just be randomizers)
    // FIXME: extension for making randos (config multiple with param resolvers)

    private static final Randomizer<String> NAME_RANDO = oneOf(
        stream(UnisexName.values())
            .map(UnisexName::toString)
            .toList()
            .toArray(new String[0])
    );

    @Test @DisplayName("General Usage")
    void general_usage() {
        val rando = randomize(SomethingElse.class, config -> {
            config.property("name", NAME_RANDO);
            config.property("score", anIntBetween(10, 100));
            config.field("added", oneOf("one", "two"));
        });

        assertEquals(new SomethingElse(CHARLIE.toString(), 91, "two"), rando.one());
    }

    @Test @DisplayName("Randomizing nested objects")
    void nested_objects() {
        val rando = randomize(Holder.class, cfg -> {
            cfg.property("thing", randomize(SomethingElse.class, config -> {
                config.property("name", NAME_RANDO);
                config.property("score", anIntBetween(10, 100));
                config.field("added", oneOf("one", "two"));
            }));
        });

        assertEquals(new Holder(
            new SomethingElse(CHARLIE.toString(), 91, "two")
        ), rando.one());
    }

    @Test @DisplayName("General Usage (with global types)")
    void general_usage_with_types() {
        Randomizer<SomethingElse> rando = randomize(SomethingElse.class, config -> {
            config.propertyType(String.class, NAME_RANDO);
            config.property("name", oneOf("Bob", "Joe"));
            config.fieldType(int.class, anIntBetween(25, 50));
        });

        assertEquals(new SomethingElse("Joe", 44, "setter-Charlie"), rando.one());
    }

    @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
    static class Something {

        @Getter @Setter private String name;
        @Getter @Setter private int score;
    }

    @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
    static class SomethingElse extends Something {

        @Getter private String added;

        SomethingElse(final String name, final int score, final String added) {
            super(name, score);
            this.added = added;
        }

        public void setAdded(String added) {
            this.added = "setter-" + added;
        }
    }

    @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
    static class Holder {

        @Getter @Setter private SomethingElse thing;
    }
}