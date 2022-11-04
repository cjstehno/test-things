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
import io.github.cjstehno.testthings.slf4j.cfg.AppenderConfig;
import io.github.cjstehno.testthings.slf4j.impl.AppenderConfigImpl;
import lombok.Getter;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.any;

public class InMemoryLogAppender extends AppenderBase<ILoggingEvent> {

    @Getter private final List<ILoggingEvent> events = new CopyOnWriteArrayList<>();
    private final AppenderConfigImpl config = new AppenderConfigImpl();

    // FIXME: support configuring this in the logging config file

    public InMemoryLogAppender(final Consumer<AppenderConfig> consumer) {
        if (consumer != null) {
            consumer.accept(config);
        }
    }

    /**
     * Must be called to initiate the log event recording - to register the log appender with the logger.
     */
    public void attach() {
        start();
        config.getLoggers().forEach(lgr -> lgr.addAppender(this));
    }

    /**
     * Should be called when done using the appender to clean up resources.
     */
    public void detach() {
        config.getLoggers().forEach(lgr -> lgr.detachAppender(this));
        stop();
    }

    @Override protected void append(final ILoggingEvent event) {
        if (config.getFilter().matches(event)) {
            events.add(event);
        }
    }

    public int count(final Matcher<ILoggingEvent> matcher) {
        return (int) events.stream().filter(matcher::matches).count();
    }

    public int count() {
        return count(any(ILoggingEvent.class));
    }

    public List<ILoggingEvent> events(final Matcher<ILoggingEvent> matcher) {
        return events.stream().filter(matcher::matches).toList();
    }

    public List<ILoggingEvent> events() {
        return events(any(ILoggingEvent.class));
    }

    public boolean hasEvent(final Matcher<ILoggingEvent> matcher) {
        return count(matcher) > 0;
    }

    public Stream<ILoggingEvent> stream(final Matcher<ILoggingEvent> matcher) {
        return events.stream().filter(matcher::matches);
    }

    public Stream<ILoggingEvent> stream() {
        return stream(any(ILoggingEvent.class));
    }
}
