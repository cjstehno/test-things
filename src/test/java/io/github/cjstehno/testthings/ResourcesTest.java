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

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;

import static io.github.cjstehno.testthings.Resources.*;
import static io.github.cjstehno.testthings.match.FileMatcher.*;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourcesTest {

    private static final String RESOURCE_PATH = "/short-text-file.txt";
    private static final String CONTENT = "\"That fire have more nimbly is moon and long. That free have feet for beer. And, taste thunder that aunt have ghost\" anchors, for swan is.";

    @Test void loadAsString() throws IOException {
        assertEquals(CONTENT, resourceToString(RESOURCE_PATH));
        assertEquals(CONTENT, resourceToString(RESOURCE_PATH, US_ASCII));
    }

    @Test void loadAsBytes() throws IOException {
        assertArrayEquals(CONTENT.getBytes(UTF_8), resourceToBytes(RESOURCE_PATH));
    }

    @Test void loadAsInputStream() throws IOException {
        try (val instr = resourceStream(RESOURCE_PATH)) {
            assertArrayEquals(CONTENT.getBytes(UTF_8), instr.readAllBytes());
        }
    }

    @Test void asUrl() throws MalformedURLException {
        val url = resourceUrl(RESOURCE_PATH);
        assertThat(url.getProtocol(), equalTo("file"));
        assertThat(url.getPath(), endsWith("/resources/test/short-text-file.txt"));
    }

    @Test void asUri() throws URISyntaxException {
        val uri = resourceUri(RESOURCE_PATH);
        assertThat(uri.getScheme(), equalTo("file"));
        assertThat(uri.getPath(), endsWith("/resources/test/short-text-file.txt"));
    }

    @Test void templating() throws IOException {
        assertEquals(
            "Hello, Bob. You've won a new car.",
            template("/template.txt", Map.of(
                "name", "Bob",
                "prize", "a new car"
            ))
        );
    }

    @Test void copyToPath(@TempDir final Path dir) throws IOException {
        val path = dir.resolve("thefile.txt");

        copyResourceTo(RESOURCE_PATH, path);

        assertThat(
            path.toFile(),
            allOf(
                fileExists(),
                isFile(),
                fileSizeMatches(equalTo(138L))
            )
        );
    }

    @Test void copyToFile(@TempDir final File dir) throws IOException {
        val file = new File(dir, "somefile.txt");

        copyResourceTo(RESOURCE_PATH, file);

        assertThat(
            file,
            allOf(
                fileExists(),
                isFile(),
                fileSizeMatches(equalTo(138L))
            )
        );
    }
}