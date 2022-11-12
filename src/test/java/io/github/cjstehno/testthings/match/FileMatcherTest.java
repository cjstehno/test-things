/**
 * Copyright (C) 2022 Christopher J. Stehno
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.testthings.match;

import io.github.cjstehno.testthings.junit.Resource;
import io.github.cjstehno.testthings.junit.ResourcesExtension;
import lombok.val;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.collection.ArrayMatching;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

import static io.github.cjstehno.testthings.match.ByteArrayMatcher.arrayEqualTo;
import static io.github.cjstehno.testthings.match.FileMatcher.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.asEqualMatchers;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ResourcesExtension.class)
class FileMatcherTest {

    @Resource("/person-01.json") File personFile;

    @Test void matchingExtension() {
        assertThat(personFile, extensionEqualTo("json"));
        assertThat(personFile, extensionMatches(startsWith("js")));

        assertMatcherDescription("A file with extension matching \"json\"", extensionEqualTo("json"));
    }

    @Test void matchingName() {
        assertThat(personFile, nameEqualTo("person-01.json"));
        assertThat(personFile, nameMatches(startsWith("person-")));

        assertMatcherDescription("A file with name matching \"foo.bar\"", nameEqualTo("foo.bar"));
    }

    @Test void fileProperties() {
        assertThat(personFile, isFile());
        assertMatcherDescription("matches a file that is a normal file", isFile());

        assertThat(personFile, not(isDirectory()));
        assertMatcherDescription("not matches a file that is a directory", not(isDirectory()));

        assertThat(personFile, fileExists());
        assertMatcherDescription("matches a file that exists", fileExists());

        assertThat(personFile, isReadable());
        assertMatcherDescription("matches a file that is readable", isReadable());

        assertThat(personFile, isWritable());
        assertMatcherDescription("matches a file that is writable", isWritable());
    }

    @Test void sizeMatcher() {
        assertThat(personFile, fileSizeMatches(equalTo(48L)));
        assertMatcherDescription("a file with size matching <61L>", fileSizeMatches(equalTo(61L)));
    }

    @Test void fileText(@Resource("/short-text-file.txt") final File textFile) {
        assertThat(textFile, fileTextMatches(startsWith("\"That fire have more nimbly ")));
    }

    @Test void fileBytes(
        @Resource("/short-text-file.txt") final File textFile,
        @Resource("/short-text-file.txt") final byte[] bytes
    ) {
        assertThat(textFile, fileBytesMatch(arrayEqualTo(bytes)));
    }

    // FIXME: move this to a shared util? public?
    public static void assertMatcherDescription(final String expected, final Matcher<?> matcher) {
        val desc = new StringDescription();
        matcher.describeTo(desc);
        assertEquals(expected, desc.toString());
    }
}