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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Defines some standard injection utilities.
 */
public interface Injections {

    // FIXME: collapse this?

    /**
     * Injects a value into a setter or field. This version does not prefer the setter.
     *
     * @param name  the name of the property or field
     * @param value the value (may be a Randomizer).
     * @return the configured Injections instance
     */
    default Injections set(final String name, final Object value) {
        return set(name, value, false);
    }

    /**
     * Injects a value into a setter or field.
     *
     * @param name         the name of the property or field
     * @param value        the value (may be a Randomizer).
     * @param preferSetter whether to prefer the setter over direct injection
     * @return the configured Injections instance
     */
    Injections set(final String name, final Object value, final boolean preferSetter);

    // sets a field based on a transformation of the current value

    /**
     * Updates a field value by applying given function to the current value.
     * <p>
     * If the updater function returns a Randomizer, it will be used to generate the updated value.
     *
     * @param name    the property or field name
     * @param updater the updater function
     * @return the instance to the Injections implementation
     */
    default Injections update(final String name, final Function<Object, Object> updater) {
        return update(name, updater, false);
    }

    /**
     * Updates a field value by applying given function to the current value.
     * <p>
     * If the updater function returns a Randomizer, it will be used to generate the updated value.
     *
     * @param name    the property or field name
     * @param updater the updater function
     * @return the instance to the Injections implementation
     */
    Injections update(final String name, final Function<Object, Object> updater, boolean preferProps);

    /**
     * FIXME: document
     *
     * @param name     the name
     * @param modifier the modifier
     * @return the instance of the Injections implementation
     */
    Injections modify(final String name, final Consumer<Object> modifier);
}