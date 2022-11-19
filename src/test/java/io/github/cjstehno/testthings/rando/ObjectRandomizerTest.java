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

import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import lombok.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.github.cjstehno.testthings.rando.CoreRandomizers.constant;
import static io.github.cjstehno.testthings.rando.NumberRandomizers.anInt;
import static io.github.cjstehno.testthings.rando.ObjectRandomizers.randomized;
import static io.github.cjstehno.testthings.rando.StringRandomizers.alphanumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SharedRandomExtension.class)
class ObjectRandomizerTest {

    // fIXME: test with field injections

    @Test void complexObject() {
        val rando = randomized(new LowerLevel(), inj -> {
            inj.setProperty("alpha", alphanumeric(constant(6)));
            inj.setProperty("bravo", anInt());
            inj.setProperty("charlie", alphanumeric(constant(6)));
            inj.setProperty("delta", anInt());
            inj.setProperty("echo", alphanumeric(constant(6)));
            inj.setProperty("foxtrot", anInt());
        });
        assertEquals(
            new LowerLevel(
                "T4KAK8", 1696540702,
                "x184bG", -1114350914,
                "Y9V1E1", 2048415859
            ),
            rando.one()
        );
    }

    @Test void complexObjectByType() {
        val rando = randomized(new LowerLevel(), inj -> {
            inj.setProperty(String.class, alphanumeric(constant(6)));
            inj.setProperty(Integer.TYPE, anInt());
        });
        assertEquals(
            new LowerLevel(
                "T4KAK8", 764810047,
                "L22nj6", 2043171710,
                "uY9V1E", 2048415859
            ),
            rando.one()
        );
    }

    @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString @Getter @Setter
    private static class TopLevel {
        private String alpha;
        private int bravo;
    }

    @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true) @Getter @Setter
    private static class MidLevel extends TopLevel {
        private String charlie;
        private int delta;

        public MidLevel(String alpha, int bravo, String charlie, int delta) {
            super(alpha, bravo);
            this.charlie = charlie;
            this.delta = delta;
        }
    }

    @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true) @Getter @Setter
    private static class LowerLevel extends MidLevel {
        private String echo;
        private int foxtrot;

        public LowerLevel(String alpha, int bravo, String charlie, int delta, String echo, int foxtrot) {
            super(alpha, bravo, charlie, delta);
            this.echo = echo;
            this.foxtrot = foxtrot;
        }
    }
}