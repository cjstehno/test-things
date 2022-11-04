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
package io.github.cjstehno.testthings.rando;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;

/**
 * `Randomizer` implementations are used to produce random instances of the specified object type on-demand.
 *
 * @param <T> the type of random object produced
 */
public interface Randomizer<T> {

    /**
     * Used to generate a single random instance of the target class.
     *
     * @return a random instance of the target class
     */
    T one();

    /**
     * Used to generate a list of `count` randomly generated instances of the target class.
     *
     * @param count the number of items in the list
     * @return an unmodifiable list of randomly generated items
     */
    default List<T> many(int count) {
        val values = new ArrayList<T>(10);

        for (int i = 0; i < count; i++) {
            values.add(one());
        }

        return unmodifiableList(values);
    }

    /**
     * Used to generate a stream of `count` randomly generated instances of the target class.
     *
     * @param count the number of items in the stream
     * @return a Stream of randomly generated items
     */
    default Stream<T> stream(int count) {
        return many(count).stream();
    }
}