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
import io.github.cjstehno.testthings.match.PredicateMatcher;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * The configuration interface for the {@link InMemoryLogAppender}.
 */
public interface AppenderConfig {

    /**
     * Creates an instance of {@link AppenderConfig} which may be used to populate the configuration options.
     *
     * @return an instance of AppenderConfig
     */
    static AppenderConfig configure() {
        return new AppenderConfigImpl();
    }

    /**
     * Configures a logger instance to be captured by the appender.
     *
     * @param logger the logger instance
     * @return a reference to this configuration
     */
    AppenderConfig logger(final Logger logger);

    /**
     * Configures a logger by name to be captured by the appender.
     *
     * @param loggerName the logger name
     * @return a reference to this configuration
     */
    default AppenderConfig loggerNamed(final String loggerName) {
        return logger(LoggerFactory.getLogger(loggerName));
    }

    /**
     * Configures a logger by type to be captured by the appender. Assuming the standard convention of using the class
     * for the logger name, this should generally be the class being logged.
     *
     * @param loggedClass the logged class
     * @return a reference to this configuration
     */
    default AppenderConfig loggedClass(final Class<?> loggedClass) {
        return logger(LoggerFactory.getLogger(loggedClass));
    }

    /**
     * Configures a filter which will be used to allow only log events matching the filter to be appended. Only one filter
     * may be defined, if this is called twice, the last one will be used.
     *
     * @param filter the filter
     * @return a reference to this configuration
     */
    AppenderConfig filter(final Matcher<ILoggingEvent> filter);

    /**
     * Configures a filter which will be used to allow only log events matching the filter to be appended. Only one filter
     * may be defined, if this is called twice, the last one will be used.
     *
     * @param filter the filter
     * @return a reference to this configuration
     */
    default AppenderConfig filter(final Predicate<ILoggingEvent> filter) {
        return filter(PredicateMatcher.matchesPredicate(filter));
    }
}
