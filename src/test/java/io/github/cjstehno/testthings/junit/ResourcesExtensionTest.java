package io.github.cjstehno.testthings.junit;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.nio.file.Path;

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
        assertThat(setupSqlPath.toString(), endsWith("/test-things/build/resources/test/db-setup.sql"));
        assertTrue(setupSqlPath.toFile().exists());

        assertThat(setupSqlFile.toString(), endsWith("/test-things/build/resources/test/db-setup.sql"));
        assertTrue(setupSqlFile.exists());
    }

    @Test void resourceFields() {
        assertThat(shortTextPath.toString(), endsWith("/test-things/build/resources/test/short-text-file.txt"));
        assertTrue(shortTextPath.toFile().exists());

        assertThat(shortTextFile.toString(), endsWith("/test-things/build/resources/test/short-text-file.txt"));
        assertTrue(shortTextFile.exists());
    }

    @Test void resourceParams(
        @Resource("/template.txt") final Path templatePath,
        @Resource("/template.txt") final File templateFile
    ) {
        assertThat(templatePath.toString(), endsWith("/test-things/build/resources/test/template.txt"));
        assertTrue(templatePath.toFile().exists());

        assertThat(templateFile.toString(), endsWith("/test-things/build/resources/test/template.txt"));
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

    @Test void resourceDeserializers() throws Exception {

    }
}