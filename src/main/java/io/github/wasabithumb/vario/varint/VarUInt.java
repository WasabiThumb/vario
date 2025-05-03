package io.github.wasabithumb.vario.varint;

import io.github.wasabithumb.vario.sequence.ByteSequence;
import io.github.wasabithumb.vario.varint.util.VI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.IOException;

/**
 * <p>
 *     A representation of an unsigned integer which serializes to as little as 1 byte.
 *     May take up to {@link #MAX_BYTES_32} or {@link #MAX_BYTES_64} bytes for
 *     32-bit or 64-bit respectively. Saves space for 32-bit integers between {@code 0} and {@code 2097151}, and
 *     64-bit integers between {@code 0} and {@code 562949953421311}.
 * </p>
 *
 * <h1>Format</h1>
 * <p>A VarUInt consists of at least 1 byte in the following format:</p>
 * <table>
 *     <tr>
 *         <th>Extend</th>
 *         <th>Data</th>
 *     </tr>
 *     <tr>
 *         <td>1 bit</td>
 *         <td>7 bits</td>
 *     </tr>
 * </table>
 *
 * <p>
 *     The "extend" bit of a byte informs a sequential reader whether a subsequent byte is
 *     present in the sequence. The data of each byte represents 7 bits of increasing significance
 *     of the encoded integer. Unspecified bits of the encoded integer
 *     are known to be 0.
 * </p>
 * <p>
 *     In the case of unsigned 64-bit integers, the 9th byte is treated specially.
 *     The "extend" bit may be 1 (which would otherwise be invalid), in which case the MSB of the
 *     encoded integer is known to be 1 instead of 0. Without this special case, 64-bit integers
 *     with an MSB set (all negative signed 64-bit integers) would require 10 bytes to encode instead of 9.
 * </p>
 *
 * @see #of(int)
 * @see #of(long)
 */
@ApiStatus.NonExtendable
public interface VarUInt extends ByteSequence {

    int MAX_BYTES_32 = 5;

    int MAX_BYTES_64 = 9;

    VarUInt ZERO = new LongVarUInt(0L, 1);

    /**
     * Returns a {@link VarUInt} which wraps the given 32-bit value.
     * The {@link #length() length} is guaranteed to be {@link #MAX_BYTES_32} at most.
     * Saves space over basic serialization for values between {@code 0} and {@code 2097151}.
     */
    static @NotNull VarUInt of(int value) {
        if (value == 0) return ZERO;
        long v = Integer.toUnsignedLong(value);
        return new LongVarUInt(v, Math.floorDiv(63 - Long.numberOfLeadingZeros(v), 7) + 1);
    }

    /**
     * Returns a {@link VarUInt} which wraps the given 64-bit value.
     * The {@link #length() length} is guaranteed to be {@link #MAX_BYTES_64} at most.
     * Saves space over basic serialization for values between {@code 0} and {@code 562949953421311}.
     */
    static @NotNull VarUInt of(long value) {
        int n;
        if (value < 0L) {
            n = 9;
        } else if (value == 0L) {
            return ZERO;
        } else {
            n = Math.floorDiv(63 - Long.numberOfLeadingZeros(value), 7) + 1;
        }
        return new LongVarUInt(value, n);
    }

    @Contract("_ -> new")
    static @NotNull VarUInt of(byte @NotNull [] data) {
        return of(ByteSequence.wrap(data));
    }

    @Contract("_, _, _ -> new")
    static @NotNull VarUInt of(byte @NotNull [] data, int offset, int length) {
        return of(ByteSequence.wrap(data, offset, length));
    }

    @Contract("_ -> new")
    static @NotNull VarUInt of(@NotNull ByteSequence data) {
        final int length = data.length();
        if (length < 1 || length > 9)
            throw new IllegalArgumentException("Data length should be between 1 and 9 (got " + length + ")");
        return new ArrayVarUInt(data.toArray());
    }

    @Contract("_, _ -> new")
    static @NotNull VarUInt read(@NotNull InputStream in, boolean allow64) throws IOException {
        byte[] sequence;
        if (allow64) {
            sequence = VI.readVariableSequence(in, MAX_BYTES_64, 0xFF);
        } else {
            sequence = VI.readVariableSequence(in, MAX_BYTES_32, 0x0F);
        }
        return new ArrayVarUInt(sequence);
    }

    @Contract("_ -> new")
    static @NotNull VarUInt read(@NotNull InputStream in) throws IOException {
        return read(in, true);
    }

    //

    long value();

}
