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

import io.github.cjstehno.testthings.Verifiers;
import io.github.cjstehno.testthings.fixtures.BirthGender;
import io.github.cjstehno.testthings.fixtures.FemaleName;
import io.github.cjstehno.testthings.fixtures.Person;
import io.github.cjstehno.testthings.rando.PersonRandomizer;
import io.github.cjstehno.testthings.serdes.JacksonJsonSerdes;
import io.github.cjstehno.testthings.serdes.JacksonXmlSerdes;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.nio.file.Path;

import static io.github.cjstehno.testthings.fixtures.BirthGender.FEMALE;
import static io.github.cjstehno.testthings.fixtures.FemaleName.LAYLA;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ResourcesExtension.class)
class ResourcesExtensionTest {

    @Resource("/db-setup.sql") static Path setupSqlPath;
    @Resource("/db-setup.sql") static File setupSqlFile;

    @Resource("/short-text-file.txt") Path shortTextPath;
    @Resource("/short-text-file.txt") File shortTextFile;

    @Test void resourceStaticFields() {
        assertThat(setupSqlPath.toString(), endsWith("/db-setup.sql"));
        assertTrue(setupSqlPath.toFile().exists());

        assertThat(setupSqlFile.toString(), endsWith("/db-setup.sql"));
        assertTrue(setupSqlFile.exists());
    }

    @Test void resourceFields() {
        assertThat(shortTextPath.toString(), endsWith("/short-text-file.txt"));
        assertTrue(shortTextPath.toFile().exists());

        assertThat(shortTextFile.toString(), endsWith("/short-text-file.txt"));
        assertTrue(shortTextFile.exists());
    }

    @Test void resourceParams(
        @Resource("/template.txt") final Path templatePath,
        @Resource("/template.txt") final File templateFile
    ) {
        assertThat(templatePath.toString(), endsWith("/template.txt"));
        assertTrue(templatePath.toFile().exists());

        assertThat(templateFile.toString(), endsWith("/template.txt"));
        assertTrue(templateFile.exists());
    }

    @Test void resourceLoaders(
        @Resource("/short-text-file.txt") byte[] fileBytes,
        @Resource("/short-text-file.txt") String fileString,
        @Resource("/short-text-file.txt") InputStream fileStream,
        @Resource("/short-text-file.txt") Reader fileReader
    ) throws IOException {
        val text = "\"That fire have more nimbly is moon and long. That free have feet for beer. And, taste thunder that aunt have ghost\" anchors, for swan is.";

        assertArrayEquals(text.getBytes(UTF_8), fileBytes);
        assertEquals(text, fileString);
        assertArrayEquals(text.getBytes(UTF_8), fileStream.readAllBytes());
        assertEquals(text, ((BufferedReader)fileReader).readLine());
    }

    @Test void resourceDeserializers(
        @Resource("/person-01.json") final Person personJson,
        @Resource(value = "/person-01.xml", serdes = JacksonXmlSerdes.class) final Person personXml
    ) throws Exception {
        val expectedPerson = new Person(LAYLA.toString(), FEMALE, 23);

        assertEquals(expectedPerson, personJson);
        assertEquals(expectedPerson, personXml);
    }
}