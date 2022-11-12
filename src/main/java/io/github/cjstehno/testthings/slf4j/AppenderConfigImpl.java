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
package io.github.cjstehno.testthings.slf4j;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hamcrest.Matcher;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.any;

/**
 * Implementation of the {@link AppenderConfig} interface used by the {@link InMemoryLogAppender}.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class AppenderConfigImpl implements AppenderConfig {

    private final Collection<ch.qos.logback.classic.Logger> loggers = new LinkedList<>();
    private Matcher<ILoggingEvent> filter = any(ILoggingEvent.class);

    @Override public AppenderConfig logger(final Logger logger) {
        loggers.add((ch.qos.logback.classic.Logger) logger);
        return this;
    }

    @Override public AppenderConfig filter(final Matcher<ILoggingEvent> filter) {
        this.filter = filter != null ? filter : any(ILoggingEvent.class);
        return this;
    }

    /**
     * Iterates over each configured logger, applying the consumer to each one.
     *
     * @param consumer the consumer used to process each logger
     */
    void forEachLogger(final Consumer<ch.qos.logback.classic.Logger> consumer) {
        loggers.forEach(consumer);
    }

    /**
     * Returns <code>true</code> if the logging event matches the internal filter (defaults to allow any).
     *
     * @param event the incoming event
     * @return true if the logging event matches the filter
     */
    boolean matches(final ILoggingEvent event) {
        return filter.matches(event);
    }
}
