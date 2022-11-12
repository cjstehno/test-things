package io.github.cjstehno.testthings.match;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Arrays;

import static io.github.cjstehno.testthings.match.PredicateMatcher.matchesPredicate;

/**
 * Hamcrest matchers for working with byte arrays.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ByteArrayMatcher extends BaseMatcher<byte[]> {

    /**
     * Matches a byte array equal to the provided byte array.
     *
     * @param bytes the byte array
     * @return the matcher
     */
    public static Matcher<byte[]> arrayEqualTo(final byte[] bytes) {
        return new ArrayEqualToMatcher(bytes);
    }

    /**
     * Matches a byte array starting with the provided byte array.
     *
     * @param prefix the prefix bytes
     * @return the matcher
     */
    public static Matcher<byte[]> arrayStartsWith(final byte[] prefix) {
        return new ArrayStartsWithMatcher(prefix);
    }

    // TODO: contains, endsWith

    /**
     * Matches a byte array with the specified length.
     *
     * @param length the byte array length
     * @return the matcher
     */
    public static Matcher<byte[]> arrayLengthIs(final int length) {
        return matchesPredicate(bs -> bs.length == length, "a byte array with length " + length);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ArrayStartsWithMatcher extends ByteArrayMatcher {
        private final byte[] prefix;

        @Override public boolean matches(final Object actual) {
            val actualBytes = (byte[]) actual;

            for (int b = 0; b < prefix.length; b++) {
                if (actualBytes[b] != prefix[b]) {
                    return false;
                }
            }

            return true;
        }

        @Override public void describeTo(Description description) {
            description.appendText("an array of bytes starting with the prefix bytes");
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ArrayEqualToMatcher extends ByteArrayMatcher {
        private final byte[] array;

        @Override public boolean matches(final Object actual) {
            return Arrays.equals(array, (byte[]) actual);
        }

        @Override public void describeTo(Description description) {
            description.appendText("an array of bytes equal to another array of bytes");
        }
    }
}
