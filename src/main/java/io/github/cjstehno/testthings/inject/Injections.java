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
package io.github.cjstehno.testthings.inject;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Defines some standard injection utilities.
 *
 * <strong>Random Value Objects.</strong> If the <code>value</code> object passed into the injection methods is an
 * instance of a <code>Randomizer</code>, that randomizer will be used to randomly generate the value.
 */
public interface Injections {

    /**
     * Injects a value directly into an object field, if it exists, it not, an error is thrown.
     *
     * @param name  the name of the field to be injected
     * @param value the value (may be a Randomizer).
     * @return the configured Injections instance
     */
    default Injections setField(final String name, final Object value) {
        return set(name, value, false);
    }

    /**
     * If <code>preferSetter</code> is <code>true</code>, an attempt will be made to inject the value using a setter
     * method for the named property, otherwise the field itself will be injected. If the <code>preferSetter</code>
     * value is <code>false</code> the field will be directly injected.
     *
     * @param name         the name of the property or field
     * @param value        the value (may be a Randomizer).
     * @param preferSetter whether to prefer the setter over direct injection
     * @return the configured Injections instance
     */
    Injections set(final String name, final Object value, final boolean preferSetter);

    /**
     * Trys to inject the value using a setter, if it exists, otherwise direct field injection is performed.
     *
     * @param name the property name
     * @param value the value
     * @return the configured injections instance
     */
    default Injections setProperty(final String name, final Object value) {
        return set(name, value, true);
    }

    /**
     * Injects a value directly into all of the fields of the specified type.
     *
     * @param type the field type
     * @param value the value to inject
     * @return the configured injections instance
     */
    default Injections setField(final Class<?> type, final Object value) {
        return set(type, value, false);
    }

    /**
     * Injects a value into the setters setting the specified type - if a setter does not exist, it will try to inject
     * directly into a field of the type.
     *
     * If a setter and a field both match, it will only inject one of them, not both.
     *
     * @param type the field type
     * @param value the value to inject
     * @return the configured injections instance
     */
    default Injections setProperty(final Class<?> type, final Object value) {
        return set(type, value, true);
    }

    /**
     * Injects a value into the setters setting the specified type - if a setter does not exist, it will try to inject
     * directly into a field of the type.
     *
     * If a setter and a field both match, it will only inject one of them, not both.
     *
     * @param type the field type
     * @param value the value to inject
     * @param setter prefers using the setter if it exists
     * @return the configured injections instance
     */
    Injections set(final Class<?> type, final Object value, final boolean setter);

    /**
     * Updates a field value by applying given function to the current value.
     * <p>
     * If the updater function returns a Randomizer, it will be used to generate the updated value.
     *
     * @param name    the property or field name
     * @param updater the updater function
     * @return the instance to the Injections implementation
     */
    default Injections updateField(final String name, final Function<Object, Object> updater) {
        return update(name, updater, false);
    }

    /**
     * Updates a field value by applying given function to the current value of the field. The getter and setter method
     * will be used, if they exist.
     * <p>
     * If the updater function returns a Randomizer, it will be used to generate the updated value.
     *
     * @param name    the property or field name
     * @param updater the updater function
     * @return the instance to the Injections implementation
     */
    default Injections updateProperty(final String name, final Function<Object, Object> updater) {
        return update(name, updater, true);
    }

    /**
     * Updates a field value by applying given function to the current value. If the <code>preferProps</code>
     * parameter is <code>true</code>, the injector will first attempt to use the named property before
     * defaulting update the field directly.
     * <p>
     * If the updater function returns a Randomizer, it will be used to generate the updated value.
     *
     * @param name    the property or field name
     * @param updater the updater function
     * @param preferProps prefer the use of the getter and setter methods over direct field access
     * @return the instance to the Injections implementation
     */
    default Injections update(final String name, final Function<Object, Object> updater, boolean preferProps) {
        return update(name, updater, preferProps, preferProps);
    }

    /**
     * Updates a field value by applying given function to the current value. If the <code>preferProps</code>
     * parameter is <code>true</code>, the injector will first attempt to use the named property before
     * defaulting update the field directly.
     * <p>
     * If the updater function returns a Randomizer, it will be used to generate the updated value.
     *
     * @param name    the property or field name
     * @param updater the updater function
     * @param preferSetter will try to use the setter rather than go directly to the field
     * @param preferGetter will try to use the getter rather than go directly to the field
     * @return the instance to the Injections implementation
     */
    Injections update(final String name, final Function<Object, Object> updater, final boolean preferSetter, final boolean preferGetter);

    /**
     * Allows in-place modification of a value reference from the target instance. The target instance will be passed
     * to the provided consumer for modification.
     *
     * @param name     the name
     * @param modifier the modifier
     * @return the instance of the Injections implementation
     */
    default Injections modifyField(final String name, final Consumer<Object> modifier) {
        return modify(name, modifier, false);
    }

    /**
     * Allows in-place modification of a value reference from the target instance. The target instance will be passed
     * to the provided consumer for modification. The getter will be used, if it exists.
     *
     * @param name     the name
     * @param modifier the modifier
     * @return the instance of the Injections implementation
     */
    default Injections modifyProperty(final String name, final Consumer<Object> modifier) {
        return modify(name, modifier, true);
    }

    /**
     * Allows in-place modification of a value reference from the target instance. The target instance will be passed
     * to the provided consumer for modification.
     *
     * @param name     the name
     * @param modifier the modifier
     * @param preferGetter will try to use the getter rather than going directly to the field
     * @return the instance of the Injections implementation
     */
    Injections modify(final String name, final Consumer<Object> modifier, final boolean preferGetter);
}