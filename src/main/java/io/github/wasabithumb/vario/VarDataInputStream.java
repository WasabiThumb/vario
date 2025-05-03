package io.github.wasabithumb.vario;

import io.github.wasabithumb.vario.varfloat.VarDouble;
import io.github.wasabithumb.vario.varfloat.VarFloat;
import io.github.wasabithumb.vario.varint.VarInt;
import io.github.wasabithumb.vario.varint.VarUInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * A {@link DataInputStream} with support for variable-length
 * primitives.

 * <table>
 *     <tr>
 *         <th>Primitive</th>
 *         <th>Wrapper</th>
 *         <th>Methods</th>
 *     </tr>
 *     <tr>
 *         <td>{@code int}</td>
 *         <td>{@link VarInt}</td>
 *         <td>{@link #readVarInt32()}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code int}</td>
 *         <td>{@link VarUInt}</td>
 *         <td>{@link #readVarUInt32()}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code long}</td>
 *         <td>{@link VarInt}</td>
 *         <td>{@link #readVarInt64()}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code long}</td>
 *         <td>{@link VarUInt}</td>
 *         <td>{@link #readVarUInt64()}, {@link #readVarUBigInt64()}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code float}</td>
 *         <td>{@link VarFloat}</td>
 *         <td>{@link #readVarFloat()}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code double}</td>
 *         <td>{@link VarDouble}</td>
 *         <td>{@link #readVarDouble()}</td>
 *     </tr>
 * </table>
 */
public class VarDataInputStream extends DataInputStream {

    /** Creates a VarDataInputStream that uses the specified underlying InputStream */
    public VarDataInputStream(@NotNull InputStream in) {
        super(in);
    }

    //

    /**
     * Reads a variable-length 32-bit signed integer
     * @see VarInt
     */
    public int readVarInt32() throws IOException {
        return (int) VarInt.read(this, false).value();
    }

    /**
     * Reads a variable-length 64-bit signed integer
     * @see VarInt
     */
    public long readVarInt64() throws IOException {
        return VarInt.read(this, true).value();
    }

    /**
     * Reads a variable length 32-bit unsigned integer.
     * The result of this method can be cast to an {@code int} without data loss,
     * however the MSB will be interpreted as the sign bit.
     * @see VarUInt
     */
    public @Range(from=0, to=0xFFFFFFFFL) long readVarUInt32() throws IOException {
        return VarUInt.read(this, false).value();
    }

    /**
     * Reads a variable length 64-bit unsigned integer.
     * The result of this method may be negative due to the limitations of the {@code long} type.
     * To instead yield a positive {@link java.math.BigInteger BigInteger}, use {@link #readVarUBigInt64()}.
     * @see VarUInt
     */
    public long readVarUInt64() throws IOException {
        return VarUInt.read(this, true).value();
    }

    /**
     * Reads a variable length 64-bit unsigned integer
     * @see #readVarUInt64()
     */
    public @NotNull BigInteger readVarUBigInt64() throws IOException {
        final long v = this.readVarUInt64();
        // https://stackoverflow.com/questions/55752927/how-to-convert-an-unsigned-long-to-biginteger/55752928#55752928
        return new BigInteger(
                1,
                new byte[] {
                        (byte) ((v >> 56) & 0xFFL),
                        (byte) ((v >> 48) & 0xFFL),
                        (byte) ((v >> 40) & 0xFFL),
                        (byte) ((v >> 32) & 0xFFL),
                        (byte) ((v >> 24) & 0xFFL),
                        (byte) ((v >> 16) & 0xFFL),
                        (byte) ((v >> 8 ) & 0xFFL),
                        (byte) ((v      ) & 0xFFL)
                }
        );
    }

    /**
     * Reads a variable length 32-bit float
     * using <a href="https://github.com/michaeljclark/vf128">vf128</a> decoding
     * @see VarFloat
     */
    public float readVarFloat() throws IOException {
        return VarFloat.read(this).value();
    }

    /**
     * Reads a variable length 64-bit float
     * using <a href="https://github.com/michaeljclark/vf128">vf128</a> decoding
     * @see VarDouble
     */
    public double readVarDouble() throws IOException {
        return VarDouble.read(this).value();
    }

}
