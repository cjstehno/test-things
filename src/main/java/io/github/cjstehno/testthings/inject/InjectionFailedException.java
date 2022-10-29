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

import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class InjectionFailedException extends RuntimeException {

    private final List<Throwable> exceptions = new LinkedList<>();

    InjectionFailedException(final List<Throwable> exes) {
        this.exceptions.addAll(exes);
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override public String getMessage() {
        return format(
            "There were %d exceptions thrown during injection (%s)",
            exceptions.size(),
            exceptions.stream().map(throwable -> throwable.getClass().getSimpleName() + ": " + throwable.getMessage()).collect(joining(", "))
        );
    }
}