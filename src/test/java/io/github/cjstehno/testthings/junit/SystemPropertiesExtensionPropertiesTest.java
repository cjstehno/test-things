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
package io.github.cjstehno.testthings.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Properties;

import static io.github.cjstehno.testthings.junit.SystemPropertiesExtension.asProperties;
import static java.lang.System.getProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SystemPropertiesExtension.class)
class SystemPropertiesExtensionPropertiesTest {

    @SuppressWarnings("unused")
    static final Properties SYSTEM_PROPERTIES = asProperties(Map.of(
        "first.name", "Bob",
        "last.name", "Jones"
    ));

    @Test void checkValues() {
        assertEquals("Bob", getProperty("first.name"));
        assertEquals("Jones", getProperty("last.name"));

        assertNull(getProperty("player.name"));
        assertNull(getProperty("player.score"));
    }
}