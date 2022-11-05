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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * A {@link SerdesProvider} implementation based on the Jackson JSON framework.
 */
@RequiredArgsConstructor
public class JacksonJsonSerdes implements SerdesProvider {

    private final ObjectMapper mapper;

    /**
     * Creates a serdes provider for JSON with an instantiated {@link ObjectMapper}.
     */
    public JacksonJsonSerdes() {
        this(new ObjectMapper());
    }

    @Override public byte[] serializeToBytes(final Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    @Override public String serializeToString(final Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    @Override public <T> T deserialize(final byte[] bytes, final Class<? extends T> type) throws IOException {
        return mapper.readValue(bytes, type);
    }

    @Override public <T> T deserialize(String string, Class<? extends T> type) throws IOException {
        return mapper.readValue(string, type);
    }
}
