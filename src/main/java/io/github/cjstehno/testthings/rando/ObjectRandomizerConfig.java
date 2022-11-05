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

/**
 * The configuration interface for the ObjectRandomizer
 */
public interface ObjectRandomizerConfig {

    // FIXME: rename ObjectRandomizerConfig?

    /**
     * fIXME: document
     *
     * @param name the property name
     * @param randomizer the randomizer
     * @return this config instance
     * @param <P> the property type
     */
    <P> ObjectRandomizerConfig property(final String name, final Randomizer<P> randomizer);

    /**
     * fIXME: document
     *
     * @param type the property type
     * @param randomizer the randomizer
     * @return this config instance
     * @param <P> the property type
     */
    <P> ObjectRandomizerConfig propertyType(final Class<P> type, final Randomizer<P> randomizer);

    /**
     * fIXME: document
     *
     * @param name the field name
     * @param randomizer the randomizer
     * @return this config instance
     * @param <P> the field type
     */
    <P> ObjectRandomizerConfig field(final String name, final Randomizer<P> randomizer);

    /**
     * fIXME: document
     *
     * @param type the field type
     * @param randomizer the randomizer
     * @return this config instance
     * @param <P> the property type
     */
    <P> ObjectRandomizerConfig fieldType(final Class<P> type, final Randomizer<P> randomizer);
}