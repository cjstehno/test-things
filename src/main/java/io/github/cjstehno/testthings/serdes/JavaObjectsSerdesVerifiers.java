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
package io.github.cjstehno.testthings.serdes;

import io.github.cjstehno.testthings.Verifiers;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class JavaObjectsSerdesVerifiers {

    // FIXME: test

    public static void verifySerialization(final Object object, final String expected) {
        Verifiers.verifySerialization(new JavaObjectSerdes(), object, expected);
    }

    public static void verifySerdes(final Object object, final String json) {
        Verifiers.verifySerdes(new JavaObjectSerdes(), object, json);
    }
}