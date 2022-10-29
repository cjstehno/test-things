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
package io.github.cjstehno.testthings.slf4j.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.github.cjstehno.testthings.slf4j.cfg.AppenderConfig;
import lombok.Getter;
import org.hamcrest.Matcher;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.any;

public class AppenderConfigImpl implements AppenderConfig {

    @Getter private final Collection<ch.qos.logback.classic.Logger> loggers = new LinkedList<>();
    @Getter private Matcher<ILoggingEvent> filter = any(ILoggingEvent.class);

    @Override public void logger(final Logger logger) {
        loggers.add((ch.qos.logback.classic.Logger) logger);
    }

    @Override public AppenderConfig filter(final Matcher<ILoggingEvent> filter) {
        this.filter = filter != null ? filter : any(ILoggingEvent.class);
        return this;
    }
}
