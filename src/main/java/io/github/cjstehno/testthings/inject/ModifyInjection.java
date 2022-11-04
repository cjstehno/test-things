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

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Consumer;

/**
 * FIXME: document
 */
@RequiredArgsConstructor
public class ModifyInjection implements Injection {

    private final String name;
    private final Consumer<Object> modifier;

    /**
     * FIXME: document
     */
    @Override public void injectInto(Object instance) throws ReflectiveOperationException {
        val field = Injection.findField(instance.getClass(), name);
        if (field.isPresent()) {
            val currentValue = field.get().get(instance);
            modifier.accept(currentValue);

        } else {
            // FIXME: error
            throw new IllegalArgumentException();
        }
    }
}