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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UpdateInjectionTest {

    @ParameterizedTest @CsvSource({
        ",true,true,-updated",
        ",true,false,-updated",
        ",false,true,-updated",
        ",false,false,-updated",
        "alpha,true,true,alpha-updated",
        "bravo,true,false,bravo-updated",
        "charlie,false,true,charlie-updated",
        "delta,false,false,delta-updated",
    })
    void updateValue(final String initial, final boolean useGetter, final boolean useSetter, final String expected) throws ReflectiveOperationException {
        val target = new UpdateTarget(initial);

        new UpdateInjection(
            "value",
            current -> (current != null ? current : "") + "-updated",
            useSetter,
            useGetter
        ).injectInto(target);

        assertEquals(expected, target.value);
        assertEquals(useSetter, target.isSetterCalled());
        assertEquals(useGetter, target.isGetterCalled());
    }

    @ParameterizedTest @CsvSource({"true", "false"})
    void updateToNull(final boolean useProps) throws ReflectiveOperationException {
        val target = new UpdateTarget("test!");

        new UpdateInjection(
            "value",
            current -> null,
            useProps, useProps
        ).injectInto(target);

        assertNull(target.value);
        assertEquals(useProps, target.isSetterCalled());
        assertEquals(useProps, target.isGetterCalled());
    }

    @NoArgsConstructor
    static class UpdateTarget {

        protected String value;
        @Getter private boolean setterCalled;
        @Getter private boolean getterCalled;

        UpdateTarget(final String value) {
            this.value = value == null || value.isBlank() ? null : value;
        }

        public void setValue(final String value) {
            this.value = value;
            setterCalled = true;
        }

        public String getValue() {
            getterCalled = true;
            return value;
        }
    }
}