package io.github.wasabithumb.vario.sequence;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Represents a read-only finite sequence of bytes with random access.
 * Similar to Java's {@link CharSequence CharSequence}.
 * This is replaceable with {@link ByteBuffer} in most cases but useful as an
 * abstraction within VarIO due to having efficient {@link #toArray() toArray}
 * and {@link #write(OutputStream) write} implementations.
 */
public interface ByteSequence extends Comparable<ByteSequence> {

    /**
     * Wraps the given {@link ByteBuffer} into a {@link ByteSequence}.
     * The {@link ByteBuffer#limit() limit} of the buffer will be used as the
     * sequence length, and its {@link ByteBuffer#position() position} is ignored.
     */
    @Contract("_ -> new")
    static @NotNull ByteSequence wrap(@NotNull ByteBuffer buffer) {
        return new BufferByteSequence(buffer);
    }

    /**
     * Wraps the given array into a {@link ByteSequence}. Changes to the
     * array will be visible through the returned object.
     * @see #wrap(byte[], int, int)
     */
    @Contract("_ -> new")
    static @NotNull ByteSequence wrap(byte @NotNull [] array) throws IllegalArgumentException {
        return new ArrayByteSequence(array, 0, array.length);
    }

    /**
     * Wraps the given array into a {@link ByteSequence}. Changes to the
     * array will be visible through the returned object.
     * @param offset Offset into the array
     * @param length Length of the sequence
     * @see #wrap(byte[])
     */
    @Contract("_, _, _ -> new")
    static @NotNull ByteSequence wrap(byte @NotNull [] array, int offset, int length) throws IllegalArgumentException {
        if (length < 0)
            throw new IllegalArgumentException("Length must be positive, got " + length);

        if (offset < 0)
            throw new IllegalArgumentException("Offset must be positive, got " + offset);

        int end = offset + length;
        if (end > array.length)
            throw new IllegalArgumentException("Illegal offset " + offset + " and length " + length + " for array of length " + array.length);

        return new ArrayByteSequence(array, offset, length);
    }

    //

    int length();

    @Range(from = 0, to = 0xFF) int byteAt(int index) throws IndexOutOfBoundsException;

    //

    @Contract("-> new")
    default byte @NotNull [] toArray() {
        final int len = this.length();
        byte[] ret = new byte[len];
        for (int i=0; i < len; i++) {
            ret[i] = (byte) this.byteAt(i);
        }
        return ret;
    }

    default void write(@NotNull OutputStream out) throws IOException {
        for (int i=0; i < this.length(); i++) {
            out.write(this.byteAt(i));
        }
    }

    @Override
    default int compareTo(@NotNull ByteSequence o) {
        int al = this.length();
        int bl = o.length();
        int ol, lc, cmp;

        if (al == bl) {
            ol = al;
            lc = 0;
        } else if (al < bl) {
            ol = al;
            lc = -1;
        } else {
            ol = bl;
            lc = 1;
        }

        for (int i=0; i < ol; i++) {
            cmp = Integer.compare(this.byteAt(i), o.byteAt(i));
            if (cmp != 0) return cmp;
        }

        return lc;
    }

}
