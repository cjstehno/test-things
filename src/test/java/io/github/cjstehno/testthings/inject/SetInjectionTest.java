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

import io.github.cjstehno.testthings.fixtures.BirthGender;
import io.github.cjstehno.testthings.fixtures.Person;
import io.github.cjstehno.testthings.fixtures.UnisexName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.cjstehno.testthings.fixtures.BirthGender.MALE;
import static io.github.cjstehno.testthings.fixtures.UnisexName.ASPEN;
import static org.junit.jupiter.api.Assertions.*;

class SetInjectionTest {

    @Test void setterInjection() throws Exception {
        val target = new Target();

        val value = "injected";
        val injection = new SetInjection("name", value, true);

        injection.injectInto(target);

        assertEquals("injected", target.getName());
        assertTrue(target.isSetterCalled());
    }

    @Test void fieldInjection() throws Exception {
        val target = new Target();

        val value = "injected";
        val injection = new SetInjection("name", value, false);

        injection.injectInto(target);

        assertEquals("injected", target.getName());
        assertFalse(target.isSetterCalled());
    }

    @Test void setterInjectionWithoutSetter() throws Exception {
        val target = new Target();

        val injection = new SetInjection("id", 42, true);
        injection.injectInto(target);

        assertEquals(42, target.getId());
    }

    private static class Target {

        @Getter private String name;
        @Getter private int id = 8675309;
        @Getter private boolean setterCalled;

        public void setName(final String name){
            this.name = name;
            setterCalled = true;
        }
    }
}