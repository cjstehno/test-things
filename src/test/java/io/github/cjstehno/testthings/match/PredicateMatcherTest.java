/**
 * Copyright (C) 2022 Christopher J. Stehno
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.testthings.match;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.testthings.match.FileMatcherTest.assertMatcherDescription;
import static io.github.cjstehno.testthings.match.PredicateMatcher.matchesPredicate;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

class PredicateMatcherTest {

    @Test void withoutDescription() {
        final Matcher<Integer> matcher = matchesPredicate(v -> v > 100);

        assertThat(10, not(matcher));
        assertThat(200, matcher);

        assertMatcherDescription("matches a supplied predicate", matcher);
    }

    @Test void withDescription() {
        final Matcher<Integer> matcher = matchesPredicate(v -> v > 100, "an int greater than 100");

        assertThat(10, not(matcher));
        assertThat(200, matcher);

        assertMatcherDescription("matches an int greater than 100", matcher);
    }
}