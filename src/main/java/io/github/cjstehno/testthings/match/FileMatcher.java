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
package io.github.cjstehno.testthings.match;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.github.cjstehno.testthings.match.PredicateMatcher.matchesPredicate;
import static java.lang.String.join;
import static java.nio.file.Files.readAllLines;
import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Matchers useful for matching File information.
 */
public abstract class FileMatcher extends BaseMatcher<File> {

    /**
     * Creates a file matcher for matching the file name.
     *
     * @param nameMatcher the name string matcher
     * @return the file matcher
     */
    public static Matcher<File> nameMatches(final Matcher<String> nameMatcher) {
        return new FileNameMatcher(nameMatcher);
    }

    /**
     * Creates a file matcher for matching the file name.
     *
     * @param name expected name
     * @return the file matcher
     */
    public static Matcher<File> nameEqualTo(final String name) {
        return nameMatches(equalTo(name));
    }

    /**
     * Creates a file matcher for matching the file extension (not including the dot).
     *
     * @param extensionMatcher the extension string matcher
     * @return the file matcher
     */
    public static Matcher<File> extensionMatches(final Matcher<String> extensionMatcher) {
        return new FileExtensionMatcher(extensionMatcher);
    }

    /**
     * Creates a file matcher for matching the file extension (not including the dot).
     *
     * @param extension the file extension
     * @return the file matcher
     */
    public static Matcher<File> extensionEqualTo(final String extension) {
        return extensionMatches(equalTo(extension));
    }

    /**
     * Creates a file matcher for matching whether a file exists.
     *
     * @return the file matcher
     */
    public static Matcher<File> fileExists() {
        return matchesPredicate(File::exists, "a file that exists");
    }

    /**
     * Creates a file matcher for matching whether is readable.
     *
     * @return the file matcher
     */
    public static Matcher<File> isReadable() {
        return matchesPredicate(File::canRead, "a file that is readable");
    }

    /**
     * /**
     * Creates a file matcher for matching whether a file is writable.
     *
     * @return the file matcher
     */
    public static Matcher<File> isWritable() {
        return matchesPredicate(File::canRead, "a file that is writable");
    }

    /**
     * Creates a file matcher for matching whether a file is a file (not a directory).
     *
     * @return the file matcher
     */
    public static Matcher<File> isFile() {
        return matchesPredicate(File::isFile, "a file that is a normal file");
    }

    /**
     * Creates a file matcher for matching whether a file is a directory.
     *
     * @return the file matcher
     */
    public static Matcher<File> isDirectory() {
        return matchesPredicate(File::isDirectory, "a file that is a directory");
    }

    /**
     * Creates a file matcher for matching the content bytes.
     *
     * @param arrayMatcher the byte array matcher
     * @return the file matcher
     */
    public static Matcher<File> fileBytesMatch(final Matcher<byte[]> arrayMatcher) {
        return new FileBytesMatcher(arrayMatcher);
    }

    /**
     * Creates a file matcher for matching the content as a string. Multiline content will be joined with line breaks
     * (i.e. "\n").
     *
     * @param stringMatcher the string text matcher
     * @return the file matcher
     */
    public static Matcher<File> fileTextMatches(final Matcher<String> stringMatcher) {
        return new FileTextMatcher(stringMatcher);
    }

    /**
     * Creates a file matcher for matching the size of a file.
     *
     * @param sizeMatcher the matcher for the size of the file
     * @return the file matcher
     */
    public static Matcher<File> fileSizeMatches(final Matcher<Long> sizeMatcher) {
        return new FileSizeMatcher(sizeMatcher);
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class FileBytesMatcher extends FileMatcher {
        private final Matcher<byte[]> contentMatcher;

        @Override public boolean matches(final Object actual) {
            try {
                return contentMatcher.matches(Files.readAllBytes(((File) actual).toPath()));
            } catch (final IOException e) {
                return false;
            }
        }

        @Override public void describeTo(final Description description) {
            description.appendText("a file with content matching ");
            contentMatcher.describeTo(description);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class FileTextMatcher extends FileMatcher {
        private final Matcher<String> contentMatcher;

        @Override public boolean matches(final Object actual) {
            try {
                return contentMatcher.matches(join("\n", readAllLines(((File) actual).toPath())));
            } catch (final IOException e) {
                return false;
            }
        }

        @Override public void describeTo(final Description description) {
            description.appendText("a file with content matching ");
            contentMatcher.describeTo(description);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class FileNameMatcher extends FileMatcher {
        private final Matcher<String> nameMatcher;

        @Override public boolean matches(final Object actual) {
            return nameMatcher.matches(((File) actual).getName());
        }

        @Override public void describeTo(final Description description) {
            description.appendText("A file with name matching ");
            nameMatcher.describeTo(description);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class FileSizeMatcher extends FileMatcher {
        private final Matcher<Long> sizeMatcher;

        @Override public boolean matches(final Object actual) {
            val actualFile = (File) actual;
            System.out.println(actualFile.length());
            return sizeMatcher.matches(actualFile.length());
        }

        @Override public void describeTo(final Description description) {
            description.appendText("a file with size matching ");
            sizeMatcher.describeTo(description);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class FileExtensionMatcher extends FileMatcher {
        private final Matcher<String> extensionMatcher;

        @Override public boolean matches(final Object actual) {
            val actualName = ((File) actual).getName();
            val actualExt = actualName.substring(actualName.lastIndexOf('.') + 1);
            return extensionMatcher.matches(actualExt);
        }

        @Override public void describeTo(final Description description) {
            description.appendText("A file with extension matching ");
            extensionMatcher.describeTo(description);
        }
    }
}
