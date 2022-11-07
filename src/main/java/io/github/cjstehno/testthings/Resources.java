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

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;
import static lombok.AccessLevel.PRIVATE;

/**
 * Some useful utilities for working with classpath resources.
 *
 * @see io.github.cjstehno.testthings.junit.ResourcesExtension
 */
@NoArgsConstructor(access = PRIVATE)
public final class Resources {

    /**
     * Loads the specified resource path as a String with the specified charset.
     *
     * @param path    the classpath resource path
     * @param charset the charset to be used
     * @return the string version of the resource
     */
    public static String resourceToString(final String path, final Charset charset) {
        return new String(resourceToBytes(path), charset);
    }

    /**
     * Loads the specified resource path as a UTF-8 string.
     *
     * @param path the classpath resource path
     * @return the string version of the resource
     */
    public static String resourceToString(final String path) {
        return resourceToString(path, UTF_8);
    }

    /**
     * Loads the specified classpath resource as a byte array.
     *
     * @param path the resource path
     * @return a byte array containing the resource data
     */
    public static byte[] resourceToBytes(final String path) {
        try (val rs = resourceStream(path)) {
            return rs.readAllBytes();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Loads the specified classpath resource as an InputStream.
     *
     * @param path the resource path
     * @return an InputStream containing the resource data
     */
    public static InputStream resourceStream(final String path) {
        return Resources.class.getResourceAsStream(path);
    }

    /**
     * Loads the specified classpath resource as a {@link Reader}, specifically a {@link BufferedReader}.
     *
     * @param path the resource path
     * @return a Reader containing the resource data
     */
    public static Reader resourceReader(final String path) {
        return new BufferedReader(new InputStreamReader(resourceStream(path)));
    }

    /**
     * Retrieves the URL for the specified resource.
     *
     * @param path the resource path
     * @return the resource URL
     */
    public static URL resourceUrl(final String path) {
        return Resources.class.getResource(path);
    }

    /**
     * Retrieves the {@link Path} reference to the classpath resource.
     *
     * @param path the resource path
     * @return the Path reference to the classpath resource
     */
    public static Path resourcePath(final String path) {
        return Path.of(resourceUri(path));
    }

    /**
     * Retrieves the {@link File} reference to the classpath resource.
     *
     * @param path the resource path
     * @return the File reference to the classpath resource
     */
    public static File resourceFile(final String path) {
        return new File(resourceUri(path));
    }

    /**
     * Loads the resource at the specified path as bytes and then deserializes it to the specified object type.
     *
     * @param provider the serdes provider
     * @param path     the resource path
     * @param type     the object type
     */
    public static <T> T resourceDeserialized(final SerdesProvider provider, final String path, final Class<? extends T> type) {
        try {
            return provider.deserialize(resourceToBytes(path), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the resource at the specified path as bytes and then deserializes as JSON it to the specified object type.
     *
     * @param path the resource path
     * @param type the object type
     */
    public static <T> T resourceDeserialized(final String path, final Class<? extends T> type) {
        return resourceDeserialized(new JacksonJsonSerdes(), path, type);
    }

    /**
     * Retrieves the URI for the specified resource.
     *
     * @param path the resource path
     * @return the resource URI
     */
    public static URI resourceUri(final String path) {
        try {
            return resourceUrl(path).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copies the classpath resource at the specified path to the provided destination path.
     *
     * @param resourcePath the classpath resource path
     * @param path         the destination path
     */
    public static void copyResourceTo(final String resourcePath, final Path path) {
        try {
            Files.write(path, resourceToBytes(resourcePath), WRITE, CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copies the classpath resource at the specified path to the provided destination file.
     *
     * @param resourcePath the classpath resource path
     * @param file         the destination file
     */
    public static void copyResourceTo(final String resourcePath, final File file) {
        copyResourceTo(resourcePath, file.toPath());
    }

    /**
     * Loads the content at the specified classpath location as a string template. The replacement values are surrounded
     * by curly braces, as <code>{tag}</code>. The provided map defines the replacement values.
     *
     * @param path         the resource path
     * @param replacements the replacement values
     * @return the template with all defined values replaced
     */
    public static String template(final String path, final Map<String, Object> replacements) {
        var temp = resourceToString(path);
        for (val entry : replacements.entrySet()) {
            temp = temp.replaceAll("\\{%s\\}".formatted(entry.getKey()), entry.getValue().toString());
        }
        return temp;
    }
}
