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
package io.github.cjstehno.testthings.inject;

import io.github.cjstehno.testthings.junit.ResourcesExtension;
import io.github.cjstehno.testthings.junit.SharedRandomExtension;
import io.github.cjstehno.testthings.rando.CoreRandomizers;
import io.github.cjstehno.testthings.rando.StringRandomizers;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static io.github.cjstehno.testthings.rando.CoreRandomizers.constant;
import static io.github.cjstehno.testthings.rando.StringRandomizers.alphanumeric;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SharedRandomExtension.class)
class SetInjectionTest {

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void setterInjection(final boolean useSetter) throws Exception {
        val target = new Target();

        new SetInjection("name", "injected", useSetter).injectInto(target);

        assertEquals("injected", target.getName());
        assertEquals(useSetter, target.isSetterCalled());
    }

    @Test void setterInjectionWithoutSetter() throws Exception {
        val target = new Target();

        val injection = new SetInjection("id", 42, true);
        injection.injectInto(target);

        assertEquals(42, target.getId());
    }

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void injectingNull(final boolean useSetter) throws Exception {
        val target = new Target("stuff");

        new SetInjection("name", null, useSetter).injectInto(target);

        assertNull(target.getName());
        assertEquals(useSetter, target.isSetterCalled());
    }

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void injectingRando(final boolean useSetter) throws Exception {
        val target = new Target();

        new SetInjection("name", alphanumeric(constant(6)), useSetter).injectInto(target);

        assertEquals("T4KAK8", target.getName());
        assertEquals(useSetter, target.isSetterCalled());
    }

    @NoArgsConstructor
    static class Target {

        @Getter private String name;
        @Getter private int id = 8675309;
        @Getter private boolean setterCalled;

        Target(final String name) {
            this.name = name == null || name.isBlank() ? null : name;
        }

        public void setName(final String name) {
            this.name = name;
            setterCalled = true;
        }
    }
}