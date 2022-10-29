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

public interface Injections {

    // injects a value into a setter or field
    // if the value is a Randomizer it will be used to generate the value according to its definition
    default Injections set(final String name, final Object value) {
        return set(name, value, false);
    }

    // injects a value into a setter or field
    // if the value is a Randomizer it will be used to generate the value according to its definition
    Injections set(final String name, final Object value, final boolean preferSetter);

    // sets a field based on a transformation of the current value
    default Injections update(final String name, final Function<Object,Object> updater) {
        return update(name, updater, false);
    }

    // if the function returns a Randomizer it willbe used to generate the value
    Injections update(final String name, final Function<Object,Object> updater, boolean preferProps);

    Injections modify(final String name, final Consumer<Object> modifier);
}