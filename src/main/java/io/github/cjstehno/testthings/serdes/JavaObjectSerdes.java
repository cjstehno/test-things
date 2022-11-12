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

import lombok.val;

import java.io.*;

/**
 *  A {@link SerdesProvider} based on the Java object serialization framework.
 *
 *  <strong>NOTE:</strong> This implementation does not support string-based operations.
 */
public final class JavaObjectSerdes implements SerdesProvider {

    @Override public byte[] serializeToBytes(final Object object) throws IOException {
        if (!(object instanceof Serializable)) {
            throw new UnsupportedOperationException("The object does not implement Serializable.");
        }

        try (val bytes = new ByteArrayOutputStream()) {
            try (val output = new ObjectOutputStream(bytes)) {
                output.writeObject(object);
            }

            return bytes.toByteArray();
        }
    }

    /**
     * String-based serialization is not supported.
     */
    @Override public String serializeToString(final Object object) throws IOException {
        throw new UnsupportedOperationException("Java Object Serialization does not support serialization to String.");
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T deserialize(final byte[] bytes, final Class<? extends T> type) throws IOException {
        if(!Serializable.class.isAssignableFrom(type)){
            throw new UnsupportedOperationException("The object does not implement Serializable.");
        }

        try (val bytess = new ByteArrayInputStream(bytes)) {
            try (val input = new ObjectInputStream(bytess)) {
                // TODO: check if the deser obj is of "type" (param)?
                return (T) input.readObject();
            } catch (ClassNotFoundException e) {
                throw new IOException("Unable to deserialize object: " + e.getMessage(), e);
            }
        }
    }

    /**
     * String-based deserialization is not supported.
     */
    @Override public <T> T deserialize(final String string, final Class<? extends T> type) throws IOException {
        throw new UnsupportedOperationException("Java Object Serialization does not support deserialization from String.");
    }

    @Override public String toString() {
        return getClass().getSimpleName();
    }
}
