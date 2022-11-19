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

import io.github.cjstehno.testthings.inject.Injections;
import io.github.cjstehno.testthings.inject.Injector;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.cjstehno.testthings.inject.Injector.injector;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ObjectRandomizers {

    public static <O> Randomizer<O> randomized(final Supplier<O> instanceProvider, final Injector injector) {
        return new ObjectRandomizer<O>(instanceProvider, injector);
    }

    public static <O> Randomizer<O> randomized(final O instance, final Injector injector) {
        return new ObjectRandomizer<O>(() -> instance, injector);
    }

    public static <O> Randomizer<O> randomized(final Supplier<O> instanceProvider, final Injections injections) {
        return new ObjectRandomizer<O>(instanceProvider, injector(injections));
    }

    public static <O> Randomizer<O> randomized(final O instance, final Injections injections) {
        return new ObjectRandomizer<O>(() -> instance, injector(injections));
    }

    public static <O> Randomizer<O> randomized(final Supplier<O> instanceProvider, final Consumer<Injections> config) {
        return new ObjectRandomizer<O>(instanceProvider, injector(config));
    }

    public static <O> Randomizer<O> randomized(final O instance, final Consumer<Injections> config) {
        return new ObjectRandomizer<O>(() -> instance, injector(config));
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class ObjectRandomizer<O> implements Randomizer<O> {
        private final Supplier<O> instanceProvider;
        private final Injector injector;

        @Override public O one() {
            try {
                return injector.inject(instanceProvider.get());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
