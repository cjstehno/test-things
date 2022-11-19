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
package io.github.cjstehno.testthings.match;

import io.github.cjstehno.testthings.TestVerifiers;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.cjstehno.testthings.TestVerifiers.assertMatcherDescription;
import static io.github.cjstehno.testthings.match.AtomicMatcher.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class AtomicMatcherTest {

    @Test void atomicInt() {
        assertThat(new AtomicInteger(42), atomicIntIs(equalTo(42)));
        assertThat(new AtomicInteger(42), not(atomicIntIs(equalTo(101))));

        assertMatcherDescription(
            "an atomic integer matching <100>",
            atomicIntIs(equalTo(100))
        );
    }

    @Test void atomicBoolean() {
        assertThat(new AtomicBoolean(true), atomicBooleanEqualTo(true));
        assertThat(new AtomicBoolean(false), atomicBooleanEqualTo(false));

        assertMatcherDescription(
            "an atomic boolean equal to true",
            atomicBooleanEqualTo(true)
        );
    }

    @Test void atomicReference() {
        val ref = new AtomicReference<>("stuff");
        assertThat(ref, atomicReferenceIs(equalTo("stuff")));
        assertThat(ref, atomicReferenceIs(startsWith("st")));

        assertMatcherDescription(
            "an atomic reference matching \"stuff\"",
            atomicReferenceIs(equalTo("stuff"))
        );
    }
}