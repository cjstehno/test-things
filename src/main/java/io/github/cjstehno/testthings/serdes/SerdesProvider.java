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
package io.github.cjstehno.testthings.serdes;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

// FIXME: document - this allows different imple
public interface SerdesProvider {

    // FIXME: JacksonJson, JavaObject, JavaXML?, JacksonXML?
    // FIXME: maybe remove the string methods and just use bytes - the verifiers can do the string conversion

    byte[] serializeToBytes(final Object object) throws IOException;

    default String serializeToString(final Object object) throws IOException {
        return new String(serializeToBytes(object), UTF_8);
    }

    <T> T deserialize(final byte[] bytes, final Class<? extends T> type) throws IOException;

    default <T> T deserialize(final String string, final Class<? extends T> type) throws IOException {
        return deserialize(string.getBytes(UTF_8), type);
    }
}
