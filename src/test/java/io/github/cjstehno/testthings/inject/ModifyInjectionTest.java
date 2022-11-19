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

import io.github.cjstehno.testthings.fixtures.PhoneticAlphabet;
import io.github.cjstehno.testthings.inject.SetInjectionTest.Target;
import io.github.cjstehno.testthings.inject.UpdateInjectionTest.UpdateTarget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.cjstehno.testthings.fixtures.PhoneticAlphabet.ALPHA;
import static io.github.cjstehno.testthings.fixtures.PhoneticAlphabet.BRAVO;
import static org.junit.jupiter.api.Assertions.*;

class ModifyInjectionTest {

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void modification(final boolean useGetter) throws ReflectiveOperationException {
        val target = new MutableTarget(new LinkedList<>(List.of(ALPHA)));
        new ModifyInjection(
            "values",
            obj -> {
                ((List<PhoneticAlphabet>)obj).add(BRAVO);
            },
            useGetter
        ).injectInto(target);

        assertEquals(List.of(ALPHA, BRAVO), target.values);
        assertEquals(useGetter, target.isGetterCalled());
    }

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void modificationOfNull(final boolean useGetter) throws ReflectiveOperationException {
        val nullReceived = new AtomicBoolean(false);
        val target = new MutableTarget(null);
        new ModifyInjection(
            "values",
            obj -> {
                nullReceived.set(obj == null);
            },
            useGetter
        ).injectInto(target);

        assertNull(target.values);
        assertTrue(nullReceived.get());
        assertEquals(useGetter, target.isGetterCalled());
    }

    @RequiredArgsConstructor
    static class MutableTarget {

        private final List<PhoneticAlphabet> values;
        @Getter private boolean getterCalled;

        public List<PhoneticAlphabet> getValues() {
            getterCalled = true;
            return values;
        }
    }
}