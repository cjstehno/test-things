package io.github.cjstehno.testthings.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}