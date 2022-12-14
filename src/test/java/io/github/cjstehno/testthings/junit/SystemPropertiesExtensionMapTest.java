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

import static java.lang.System.getProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SystemPropertiesExtension.class)
class SystemPropertiesExtensionMapTest {

    @SuppressWarnings("unused")
    static final Map<String, String> SYSTEM_PROPERTIES = Map.of(
        "player.name", "bjones",
        "player.score", "123,987"
    );
    @SuppressWarnings("unused")
    static final Map<String, String> OVERLAY = Map.of(
        "player.name", "asmith",
        "player.score", "777,123"
    );

    @Test void checkValues() {
        assertEquals("bjones", getProperty("player.name"));
        assertEquals("123,987", getProperty("player.score"));

        assertNull(getProperty("first.name"));
        assertNull(getProperty("last.name"));
    }

    @Test @ApplyProperties("OVERLAY")
    void checkOverlayValues() {
        assertEquals("asmith", getProperty("player.name"));
        assertEquals("777,123", getProperty("player.score"));

        assertNull(getProperty("first.name"));
        assertNull(getProperty("last.name"));
    }
}