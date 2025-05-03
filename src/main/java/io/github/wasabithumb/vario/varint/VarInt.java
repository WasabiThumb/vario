package io.github.wasabithumb.vario.varint;

import io.github.wasabithumb.vario.sequence.ByteSequence;
import io.github.wasabithumb.vario.varint.util.VI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     A representation of a signed integer which serializes to as little as 1 byte.
 *     May take up to {@link #MAX_BYTES_32} or {@link #MAX_BYTES_64} bytes for
 *     32-bit or 64-bit respectively. Saves space for 32-bit integers between {@code -1048576} and {@code 1048575}, and
 *     64-bit integers between {@code -281474976710656} and {@code 281474976710655}.
 * </p>
 *
 * <h1>Format</h1>
 * <p>The first byte of a VarInt is as follows:</p>
 * <table>
 *     <tr>
 *         <th>Extend</th>
 *         <th>Sign</th>
 *         <th>Data</th>
 *     </tr>
 *     <tr>
 *         <td>1 bit</td>
 *         <td>1 bit</td>
 *         <td>6 bits</td>
 *     </tr>
 * </table>
 * <p>
 *     Let {@code k} be a big-endian byte sequence representing a 32 or 64 bit integer.
 * </p>
 * <p>
 *     If the sign bit 0, {@code k} is arithmetically equal to the encoded integer.
 *     Otherwise, {@code k} is the bitwise NOT of the encoded integer.
 *     The 6 data bits of this byte are the 6 least significant bits of {@code k}.
 * </p>
 * <p>
 *     If the extend bit is 0, the encoded sequence is 1 byte. Otherwise,
 *     subsequent bytes will be encoded as follows:
 * </p>
 *
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
 * <p>
 *     The sequence will consist of 0 or more of such bytes, the data of which represents
 *     bits of {@code k} in order of increasing significance. Unspecified bits of {@code k}
 *     are known to be 0.
 * </p>
 *
 * @see #of(int)
 * @see #of(long)
 */
@ApiStatus.NonExtendable
public interface VarInt extends ByteSequence {

    int MAX_BYTES_32 = 5;

    int MAX_BYTES_64 = 10;

    VarInt ZERO = of(0);

    /**
     * Returns a {@link VarInt} which wraps the given 32-bit value.
     * The {@link #length() length} is guaranteed to be {@link #MAX_BYTES_32} at most.
     * Saves space over basic serialization for values between {@code -1048576} and {@code 1048575}.
     */
    static @NotNull VarInt of(int value) {
        return of((long) value);
    }

    /**
     * Returns a {@link VarInt} which wraps the given 64-bit value.
     * The {@link #length() length} is guaranteed to be {@link #MAX_BYTES_64} at most.
     * Saves space over basic serialization for values between {@code -281474976710657} and {@code 281474976710656}.
     */
    static @NotNull VarInt of(long value) {
        boolean flip = (value < 0L);
        if (flip) value = ~value;
        int nz = Long.numberOfLeadingZeros(value);
        int length;
        if (nz > 57) {
            length = 1;
        } else {
            length = Math.floorDiv(57 - nz, 7) + 2;
        }
        return new LongVarInt(value, length, flip);
    }

    @Contract("_ -> new")
    static @NotNull VarInt of(byte @NotNull [] array) {
        return of(ByteSequence.wrap(array));
    }

    @Contract("_, _, _ -> new")
    static @NotNull VarInt of(byte @NotNull [] array, int offset, int length) {
        return of(ByteSequence.wrap(array, offset, length));
    }

    @Contract("_ -> new")
    static @NotNull VarInt of(@NotNull ByteSequence data) {
        final int length = data.length();
        if (length < 1 || length > 10)
            throw new IllegalArgumentException("Data length should be between 1 and 10 (got " + length + ")");
        return new ArrayVarInt(data.toArray());
    }

    @Contract("_, _ -> new")
    static @NotNull VarInt read(@NotNull InputStream in, boolean allow64) throws IOException {
        byte[] sequence;
        if (allow64) {
            sequence = VI.readVariableSequence(in, MAX_BYTES_64, 0x03);
        } else {
            sequence = VI.readVariableSequence(in, MAX_BYTES_32, 0x1F);
        }
        return new ArrayVarInt(sequence);
    }

    @Contract("_ -> new")
    static @NotNull VarInt read(@NotNull InputStream in) throws IOException {
        return read(in, true);
    }

    //

    long value();

}
