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

/**
 * An interface to allow for different serialization/deserialization framework implementations to use the same
 * testing framework methods.
 */
public interface SerdesProvider {

    /**
     * Serialize the object to bytes.
     *
     * @param object the object
     * @return a byte array containing the serialized object
     * @throws IOException if there is a problem
     */
    byte[] serializeToBytes(final Object object) throws IOException;

    /**
     * Serialize the object to a string.
     *
     * @param object the object
     * @return a string containing the serialized object
     * @throws IOException if there is a problem
     */
    default String serializeToString(final Object object) throws IOException {
        return new String(serializeToBytes(object), UTF_8);
    }

    /**
     * Deserializes the byte array to an object of the specified type.
     *
     * @param bytes the byte array
     * @param type the object type
     * @return the object instance
     * @param <T> the type of object
     * @throws IOException if there is a problem
     */
    <T> T deserialize(final byte[] bytes, final Class<? extends T> type) throws IOException;

    /**
     * Deserializes the string to an object of the specified type.
     *
     * @param string the string
     * @param type the object type
     * @return the object instance
     * @param <T> the type of object
     * @throws IOException if there is a problem
     */
    default <T> T deserialize(final String string, final Class<? extends T> type) throws IOException {
        return deserialize(string.getBytes(UTF_8), type);
    }
}
