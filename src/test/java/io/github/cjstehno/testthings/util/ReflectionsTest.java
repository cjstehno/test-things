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
package io.github.cjstehno.testthings.util;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.cjstehno.testthings.match.AtomicMatcher.atomicIntIs;
import static io.github.cjstehno.testthings.util.Reflections.extractValue;
import static io.github.cjstehno.testthings.util.Reflections.invokeMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.platform.commons.support.HierarchyTraversalMode.TOP_DOWN;

class ReflectionsTest {

    private static final String SOME_VALUE = "an interesting value";
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Test void extractingValue() {
        val fields = ReflectionSupport.findFields(
            ReflectionsTest.class,
            f -> f.getName().equals("SOME_VALUE"),
            TOP_DOWN
        );

        val value = extractValue(ReflectionsTest.class, fields.get(0), String.class);
        assertEquals(SOME_VALUE, value);
    }

    @Test void invokingMethod() {
        val method = ReflectionSupport.findMethod(ReflectionsTest.class, "someMethod");
        invokeMethod(ReflectionsTest.class, method.get());
        assertThat(counter, atomicIntIs(equalTo(1)));
    }

    @SuppressWarnings("unused")
    private static void someMethod() {
        counter.incrementAndGet();
    }
}