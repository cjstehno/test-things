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


import lombok.val;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Default Injections implementation.
 */
public class InjectionsImpl implements Injections {

    private final List<Injection> injections = new LinkedList<>();

    @Override public Injections set(final String name, final Object value, final boolean preferSetter) {
        injections.add(new SetInjection(name, value, preferSetter));
        return this;
    }

    @Override public Injections set(final Class<?> type, final Object value, final boolean setter) {
        injections.add(new TypeSetInjection(type, value, setter));
        return this;
    }

    @Override
    public Injections update(String name, Function<Object, Object> updater, final boolean preferSetter, final boolean preferGetter) {
        injections.add(new UpdateInjection(name, updater, preferSetter, preferGetter));
        return this;
    }

    @Override public Injections modify(final String name, final Consumer<Object> modifier, final boolean preferGetter) {
        injections.add(new ModifyInjection(name, modifier, true));
        return this;
    }

    /**
     * Applies the configured injections to the given instance.
     *
     * @param instance the target instance
     * @return the updated instance (reference to the incoming instance)
     * @param <T> the type of the target instance
     * @throws ReflectiveOperationException if there is a problem injecting
     */
    public <T> T apply(final T instance) throws ReflectiveOperationException {
        for (val injection : injections) {
            injection.injectInto(instance);
        }
        return instance;
    }
}