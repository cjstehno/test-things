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
package io.github.cjstehno.testthings;

import io.github.cjstehno.testthings.serdes.JacksonJsonSerdes;
import io.github.cjstehno.testthings.serdes.SerdesProvider;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

// FIXME: document - stuff to load things from classpath
@NoArgsConstructor(access = PRIVATE)
public final class Resources {

    // FIXME: wrap in try?
    // FIXME: testing

    public static String resourceToString(final String path, final Charset charset) throws IOException {
        return new String(resourceToBytes(path), charset);
    }

    public static String resourceToString(final String path) throws IOException {
        return resourceToString(path, UTF_8);
    }

    public static byte[] resourceToBytes(final String path) throws IOException {
        return resourceStream(path).readAllBytes();
    }

    public static InputStream resourceStream(final String path) {
        return Resources.class.getResourceAsStream(path);
    }

    public static URL resourceUrl(final String path) {
        return Resources.class.getResource(path);
    }

    public static <T> T resourceDeserialized(final SerdesProvider provider, final String path, final Class<? extends T> type) throws IOException {
        return provider.deserialize(resourceToBytes(path), type);
    }

    // FIXME: pull out and create one for each type?
    public static <T> T resourceDeserialized(final String path, final Class<? extends T> type) throws IOException {
        return resourceDeserialized(new JacksonJsonSerdes(), path, type);
    }

    public static URI resourceUri(final String path) throws URISyntaxException {
        return resourceUrl(path).toURI();
    }

    public static void copyResourceTo(final String resourcePath, final Path path) throws IOException {
        Files.write(path, resourceToBytes(resourcePath), StandardOpenOption.WRITE);
    }

    public static void copyResourceTo(final String resourcePath, final File file) throws IOException {
        copyResourceTo(resourcePath, file.toPath());
    }

    public static String template(final String path, final Map<String, Object> replacements) throws IOException {
        var temp = resourceToString(path);

        for (val entry : replacements.entrySet()) {
            temp = temp.replaceAll("{" + entry.getKey() + "}", entry.getValue().toString());
        }

        return temp;
    }
}
