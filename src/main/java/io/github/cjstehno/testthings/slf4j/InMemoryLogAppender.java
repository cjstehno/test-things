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
import ch.qos.logback.core.AppenderBase;
import lombok.Getter;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.any;

/**
 * An {@link ch.qos.logback.core.Appender} for the SLF4J logging library (with logback backend) that allows for verifying
 * results that may only appear in log messages.
 * <p>
 * To use this appender you register the classes to be monitored using the <code>loggedClass(Class)</code> configuration
 * method and then call the <code>attach()</code> method on the appender to hook it into the logging system.
 * <p>
 * You should ensure that the <code>detach()</code> method is also called when you are done testing so that the test code
 * is not left in the logger configuration.
 * <p>
 * Various methods are provided to read through and filter the captured log messages.
 * <p>
 * See also the {@link io.github.cjstehno.testthings.junit.LogAppenderExtension} for a simple means of using this in tests.
 */
public class InMemoryLogAppender extends AppenderBase<ILoggingEvent> {

    // FIXME: support configuring this in the logging config file

    @Getter private final List<ILoggingEvent> events = new CopyOnWriteArrayList<>();
    private final AppenderConfigImpl config;

    /**
     * Creates an in-memory Logback {@link ch.qos.logback.core.Appender} for testing with the provided configuration.
     *
     * @param consumer the configuration
     */
    public InMemoryLogAppender(final Consumer<AppenderConfig> consumer) {
        this(new AppenderConfigImpl());

        if (consumer != null) {
            consumer.accept(config);
        }
    }

    public InMemoryLogAppender(final AppenderConfig config) {
        this.config = (AppenderConfigImpl) config;
    }

    /**
     * Must be called to initiate the log event recording - to register the log appender with the logger.
     */
    public void attach() {
        start();
        config.forEachLogger(lgr -> lgr.addAppender(this));
    }

    /**
     * Should be called when done using the appender to clean up resources.
     */
    public void detach() {
        config.forEachLogger(lgr -> lgr.detachAppender(this));
        stop();
    }

    /**
     * Called when a logging event is to be added to the appender.
     *
     * @param event the logging event
     */
    @Override protected void append(final ILoggingEvent event) {
        if (config.matches(event)) {
            events.add(event);
        }
    }

    /**
     * Counts the number of captured events matching the provided matcher.
     *
     * @param matcher the event matcher
     * @return the count of matching events
     */
    public int count(final Matcher<ILoggingEvent> matcher) {
        return (int) events.stream().filter(matcher::matches).count();
    }

    /**
     * Counts all captured logging events.
     *
     * @return the count
     */
    public int count() {
        return count(any(ILoggingEvent.class));
    }

    /**
     * Retrieves all captured logging events matching the provided matcher.
     *
     * @param matcher the matcher
     * @return the list of matching events
     */
    public List<ILoggingEvent> events(final Matcher<ILoggingEvent> matcher) {
        return events.stream().filter(matcher::matches).toList();
    }

    /**
     * Retrieves all captured logging events.
     *
     * @return all captured logging events
     */
    public List<ILoggingEvent> events() {
        return events(any(ILoggingEvent.class));
    }

    /**
     * Returns <code>true</code> if there is at least one captured logging event matching the provided matcher.
     *
     * @param matcher the matcher
     * @return true if there is a matching event
     */
    public boolean hasEvent(final Matcher<ILoggingEvent> matcher) {
        return count(matcher) > 0;
    }

    /**
     * Retrieves the captured logging events matching the provided matcher, as a Stream.
     *
     * @param matcher the matcher
     * @return the stream of matching events
     */
    public Stream<ILoggingEvent> stream(final Matcher<ILoggingEvent> matcher) {
        return events.stream().filter(matcher::matches);
    }

    /**
     * Retrieves the captured logging events as a Stream.
     *
     * @return the stream of events
     */
    public Stream<ILoggingEvent> stream() {
        return stream(any(ILoggingEvent.class));
    }
}
