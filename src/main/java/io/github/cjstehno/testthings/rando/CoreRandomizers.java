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


import lombok.NoArgsConstructor;
import lombok.val;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import static io.github.cjstehno.testthings.rando.SharedRandom.current;
import static lombok.AccessLevel.PRIVATE;

/**
 * Defines the core randomizers.
 */
@NoArgsConstructor(access = PRIVATE)
public final class CoreRandomizers {

    // FIXME: give it a map with keys mapped to randomizers - gen random values for map

    /**
     * Defines a randomizer which will always return the same value... so not really random at all.
     *
     * @param value the value to be returned
     * @param <V>   the type of the generated value
     * @return the randomizer
     */
    public static <V> Randomizer<V> constant(final V value) {
        return () -> value;
    }

    /**
     * A randomizer that returns a value from a collection of values.
     *
     * @param options the available options
     * @param <V>     the type of the generated value
     * @return the randomizer
     */
    @SafeVarargs
    public static <V> Randomizer<V> oneOf(final V... options) {
        return () -> options[current().nextInt(options.length)];
    }

    /**
     * A randomizer that returns a value from one of the defined enum values.
     *
     * @param enumType the type of enum (provides the values)
     * @param <V>      the type of generated value
     * @return the randomizer
     */
    @SuppressWarnings("unchecked")
    public static <V extends Enum<V>> Randomizer<V> oneOf(final Class<V> enumType) {
        try {
            return oneOf((V[]) enumType.getMethod("values").invoke(enumType));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * A randomizer which will randomly select a value from the collection, popping that value off so that each value
     * will only be used once.
     *
     * @param values the collection of values
     * @param <V>    the type of the generated value
     * @return the randomizer
     */
    public static <V> Randomizer<V> onceEachOf(final Collection<V> values) {
        return new OnceEachRandomizer<>(values);
    }

    private static class OnceEachRandomizer<V> implements Randomizer<V> {
        private final List<V> values = new CopyOnWriteArrayList<>();

        private OnceEachRandomizer(final Collection<V> values) {
            this.values.addAll(values);
        }

        @Override public V one() {
            return values.isEmpty() ? null : values.remove(current().nextInt(values.size()));
        }
    }

    /**
     * A randomizer which will generate a list of random values. The size of the generated list is also random.
     *
     * @param countRando the randomizer used to determine the list size
     * @param valueRando the randomizer used to determine the list values
     * @param <V>        the type of the generated value
     * @return the randomizer
     */
    public static <V> Randomizer<List<V>> listOf(final Randomizer<Integer> countRando, final Randomizer<V> valueRando) {
        return () -> valueRando.many(countRando.one());
    }

    /**
     * A randomizer which will generate a set of random values. The size of the generated set is also random.
     *
     * @param countRando the randomizer used to determine the set size
     * @param valueRando the randomizer used to determine the set values
     * @param <V>        the type of the generated value
     * @return the randomizer
     */
    public static <V> Randomizer<Set<V>> setOf(final Randomizer<Integer> countRando, final Randomizer<V> valueRando) {
        return () -> new HashSet<>(valueRando.many(countRando.one()));
    }

    /**
     * A randomizer which will generate a map of random values. The size of the generated map is also random.
     *
     * @param countRando the randomizer used to determine the map size
     * @param keyRando   the randomizer used to determine the map keys
     * @param valueRando the randomizer used to determine the map values
     * @param <K> the type of the key value
     * @param <V>        the type of the generated value
     * @return the randomizer
     */
    public static <K, V> Randomizer<Map<K, V>> mapOf(final Randomizer<Integer> countRando, final Randomizer<K> keyRando, final Randomizer<V> valueRando) {
        return () -> {
            val map = new LinkedHashMap<K, V>();
            for (int i = 0; i < countRando.one(); i++) {
                map.put(keyRando.one(), valueRando.one());
            }
            return map;
        };
    }

    /**
     * A randomizer which will generate an array of random values. The size of the generated array is also random.
     *
     * @param countRando the randomizer used to determine the array size
     * @param valueRando the randomizer used to determine the array values
     * @param <V>        the type of the generated value
     * @return the randomizer
     */
    @SuppressWarnings("unchecked")
    public static <V> Randomizer<V[]> arrayOf(final Randomizer<Integer> countRando, final Randomizer<V> valueRando) {
        return () -> valueRando.many(countRando.one()).toArray((V[]) new Object[0]);
    }

    /**
     * A randomizer which will generate a stream of random values. The size of the generated stream is also random.
     *
     * @param countRando the randomizer used to determine the stream size
     * @param valueRando the randomizer used to determine the stream values
     * @param <V>        the type of the generated value
     * @return the randomizer
     */
    public static <V> Randomizer<Stream<V>> streamOf(final Randomizer<Integer> countRando, final Randomizer<V> valueRando) {
        return () -> valueRando.stream(countRando.one());
    }

    // FIXME: date, localdate, localdatetime
}
