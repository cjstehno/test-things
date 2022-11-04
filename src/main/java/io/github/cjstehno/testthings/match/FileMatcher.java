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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.File;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.equalTo;

/// FIXME: similar one for Path?

/**
 * Matchers useful for matching File information.
 */
public abstract class FileMatcher extends BaseMatcher<File> {

    // FIXME: test

    /**
     * FIXME: document
     */
    public static Matcher<File> nameMatches(final Matcher<String> nameMatcher) {
        return new FileNameMatcher(nameMatcher);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> nameEqualTo(final String name) {
        return nameMatches(equalTo(name));
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> extensionMatches(final Matcher<String> extensionMatcher) {
        return new FileExtensionMatcher(extensionMatcher);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> extensionEqualTo(final String extension) {
        return extensionMatches(equalTo(extension));
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> fileExists() {
        return new FilePredicateMatcher(File::exists);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> isReadable() {
        return new FilePredicateMatcher(File::canRead);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> isWritable() {
        return new FilePredicateMatcher(File::canWrite);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> isFile() {
        return new FilePredicateMatcher(File::isFile);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> isDirectory() {
        return new FilePredicateMatcher(File::isDirectory);
    }

    /**
     * FIXME: document
     */
    public static Matcher<File> fileSizeMatches(final Matcher<Long> sizeMatcher) {
        return new FileSizeMatcher(sizeMatcher);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FilePredicateMatcher extends FileMatcher {
        private final Predicate<File> predicate;

        @Override public boolean matches(final Object actual) {
            val actualFile = (File) actual;
            return predicate.test(actualFile);
        }

        @Override public void describeTo(Description description) {
            // FIXME: impl
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FileNameMatcher extends FileMatcher {
        private final Matcher<String> nameMatcher;

        @Override public boolean matches(final Object actual) {
            return nameMatcher.matches(((File) actual).getName());
        }

        @Override public void describeTo(Description description) {
            // FIXME: impl
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FileSizeMatcher extends FileMatcher {
        private final Matcher<Long> sizeMatcher;

        @Override public boolean matches(final Object actual) {
            val actualFile = (File) actual;
            return sizeMatcher.matches(actualFile.length());
        }

        @Override public void describeTo(Description description) {
            // FIXME: impl
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FileExtensionMatcher extends FileMatcher {
        private final Matcher<String> extensionMatcher;

        @Override public boolean matches(final Object actual) {
            val actualName = ((File) actual).getName();
            val actualExt = actualName.substring(actualName.lastIndexOf('.'));
            return extensionMatcher.matches(actualExt);
        }

        @Override public void describeTo(Description description) {
            // FIXME: impl
        }
    }
}
