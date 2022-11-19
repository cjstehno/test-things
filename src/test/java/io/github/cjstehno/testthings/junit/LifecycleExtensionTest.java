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
package io.github.cjstehno.testthings.junit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.cjstehno.testthings.junit.Lifecycle.LifecyclePoint.*;
import static io.github.cjstehno.testthings.match.AtomicMatcher.atomicIntIs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LifecycleExtensionTest {

    @Mock(answer = CALLS_REAL_METHODS) private ExtensionContext extensionContext;
    private static final AtomicInteger beforeAllCount = new AtomicInteger(0);
    private static final AtomicInteger afterAllCount = new AtomicInteger(0);
    private final AtomicInteger beforeEachCount = new AtomicInteger(0);
    private final AtomicInteger afterEachCount = new AtomicInteger(0);
    private LifecycleExtension extension;

    @BeforeEach void beforeEach() {
        extension = new LifecycleExtension();
    }

    @AfterEach void afterEach() {
        beforeAllCount.set(0);
        beforeEachCount.set(0);
        afterEachCount.set(0);
        afterAllCount.set(0);
    }

    @Test void executing() throws Exception {
        when(extensionContext.getTestClass()).thenReturn(Optional.of(LifecycleExtensionTest.class));
        when(extensionContext.getTestInstance()).thenReturn(Optional.of(this));

        extension.beforeAll(extensionContext);
        extension.beforeEach(extensionContext);
        extension.afterEach(extensionContext);
        extension.afterAll(extensionContext);

        assertThat(beforeAllCount, atomicIntIs(equalTo(1)));
        assertThat(beforeEachCount, atomicIntIs(equalTo(1)));
        assertThat(afterEachCount, atomicIntIs(equalTo(1)));
        assertThat(afterAllCount, atomicIntIs(equalTo(1)));
    }

    // NOTE: these are for testing, they are not actual lifecycle methods

    @Lifecycle(BEFORE_ALL) static void lifecycleBeforeAll() {
        beforeAllCount.incrementAndGet();
    }

    @Lifecycle(AFTER_ALL) static void lifecycleAfterAll() {
        afterAllCount.incrementAndGet();
    }

    @Lifecycle(BEFORE_EACH) void lifecycleBeforeEach() {
        beforeEachCount.incrementAndGet();
    }

    @Lifecycle(AFTER_EACH) void lifecycleAfterEach() {
        afterEachCount.incrementAndGet();
    }
}