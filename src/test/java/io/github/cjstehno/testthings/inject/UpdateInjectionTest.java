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

import io.github.cjstehno.testthings.inject.SetInjectionTest.Target;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UpdateInjectionTest {

    @ParameterizedTest @CsvSource({
        ",true,-updated",
        ",false,-updated",
        "something,true,something-updated",
        "something,false,something-updated",
    })
    void updateValue(final String initial, final boolean useSetter, final String expected) throws ReflectiveOperationException {
        val target = new Target(initial);

        new UpdateInjection(
            "name",
            current -> (current != null ? current : "") + "-updated",
            useSetter
        ).injectInto(target);

        assertEquals(expected, target.getName());
        assertEquals(useSetter, target.isSetterCalled());
    }

    @ParameterizedTest @CsvSource({"true", "false"})
    void updateToNull(final boolean useSetter) throws ReflectiveOperationException {
        val target = new Target("test!");

        new UpdateInjection(
            "name",
            current -> null,
            useSetter
        ).injectInto(target);

        assertNull(target.getName());
        assertEquals(useSetter, target.isSetterCalled());
    }
}