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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static io.github.cjstehno.testthings.rando.ObjectRandomizer.randomize;
import static io.github.cjstehno.testthings.rando.Randomizers.intRange;
import static io.github.cjstehno.testthings.rando.Randomizers.oneOf;
import static java.lang.String.format;

class ObjectRandomizerTest {

    // TODO: more complex things: maps, lists, arrays (these would just be randomizers)
    // FIXME: make sure super-class population is supported

    @Test @DisplayName("General Usage")
    void general_usage() {
        Randomizer<SomethingElse> rando = randomize(SomethingElse.class, config -> {
            config.property("name", oneOf("alpha", "bravo", "charlie"));
            config.property("score", intRange(10, 100));
            config.field("added", oneOf("one", "two"));
        });

        rando.list(5).forEach(System.out::println);
    }

    @Test @DisplayName("Randomizing nested objects")
    void nested_objects() {
        Randomizer<SomethingElse> randoThing = randomize(SomethingElse.class, config -> {
            config.property("name", oneOf("alpha", "bravo", "charlie"));
            config.property("score", intRange(10, 100));
            config.field("added", oneOf("one", "two"));
        });

        Randomizer<Holder> rando = randomize(Holder.class, cfg -> {
            cfg.property("thing", randoThing);
        });

        rando.list(5).forEach(System.out::println);
    }

    @Test @DisplayName("General Usage (with global types)")
    void general_usage_with_types() {
        Randomizer<SomethingElse> rando = randomize(SomethingElse.class, config -> {
            config.propertyType(String.class, oneOf("alpha", "bravo", "charlie"));
            config.property("name", oneOf("Bob", "Joe"));
            config.fieldType(int.class, intRange(25, 50));
        });

        rando.list(5).forEach(System.out::println);
    }

    static class Something {

        private String name;
        private int score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override public String toString() {
            return format("Something{name='%s', score=%d}", name, score);
        }
    }

    static class SomethingElse extends Something {

        private String added;

        public String getAdded() {
            return added;
        }

        public void setAdded(String added) {
            this.added = "setter-" + added;
        }

        @Override public String toString() {
            return format("SomethingElse{name=%s, score=%d, added=%s}", getName(), getScore(), added);
        }
    }

    static class Holder {

        private SomethingElse thing;

        public SomethingElse getThing() {
            return thing;
        }

        public void setThing(SomethingElse thing) {
            this.thing = thing;
        }

        @Override public String toString() {
            return format("Holder{thing=%s}", thing);
        }
    }

    static class Collector {

        private List<String> strings = new LinkedList<>();

        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }

        @Override public String toString() {
            return "Collector{" + "strings=" + strings + '}';
        }
    }
}