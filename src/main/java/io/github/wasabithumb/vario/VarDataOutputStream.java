package io.github.wasabithumb.vario;

import io.github.wasabithumb.vario.varfloat.VarDouble;
import io.github.wasabithumb.vario.varfloat.VarFloat;
import io.github.wasabithumb.vario.varint.VarInt;
import io.github.wasabithumb.vario.varint.VarUInt;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link DataOutputStream} with support for variable-length primitives.
 *
 * <table>
 *     <tr>
 *         <th>Primitive</th>
 *         <th>Wrapper</th>
 *         <th>Methods</th>
 *     </tr>
 *     <tr>
 *         <td>{@code int}</td>
 *         <td>{@link VarInt}</td>
 *         <td>{@link #writeVarInt32(int)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code int}</td>
 *         <td>{@link VarUInt}</td>
 *         <td>{@link #writeVarUInt32(int)}, {@link #writeVarUInt32(long)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code long}</td>
 *         <td>{@link VarInt}</td>
 *         <td>{@link #writeVarInt64(long)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code long}</td>
 *         <td>{@link VarUInt}</td>
 *         <td>{@link #writeVarUInt64(long)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code float}</td>
 *         <td>{@link VarFloat}</td>
 *         <td>{@link #writeVarFloat(float)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@code double}</td>
 *         <td>{@link VarDouble}</td>
 *         <td>{@link #writeVarDouble(double)}</td>
 *     </tr>
 * </table>
 */
public class VarDataOutputStream extends DataOutputStream {

    public VarDataOutputStream(@NotNull OutputStream out) {
        super(out);
    }

    //

    /**
     * Writes a variable length 32-bit signed integer
     * <table>
     *     <tr>
     *         <th>Range</th>
     *         <th>Byte Count</th>
     *     </tr>
     *     <tr>
     *         <td>{@code -64} to {@code 63}</td>
     *         <td>{@code 1}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -8192} to {@code 8191}</td>
     *         <td>{@code 2}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -1048576} to {@code 1048575}</td>
     *         <td>{@code 3}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -134217728} to {@code 134217727}</td>
     *         <td>{@code 4}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -2147483648} to {@code 2147483647}</td>
     *         <td>{@code 5}</td>
     *     </tr>
     * </table>
     * @see VarInt
     */
    public void writeVarInt32(int v) throws IOException {
        VarInt.of(v).write(this);
    }

    /**
     * Writes a variable length 64-bit signed integer
     * <table>
     *     <tr>
     *         <th>Range</th>
     *         <th>Byte Count</th>
     *     </tr>
     *     <tr>
     *         <td>{@code -64} to {@code 63}</td>
     *         <td>{@code 1}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -8192} to {@code 8191}</td>
     *         <td>{@code 2}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -1048576} to {@code 1048575}</td>
     *         <td>{@code 3}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -134217728} to {@code 134217727}</td>
     *         <td>{@code 4}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -17179869184} to {@code 17179869183}</td>
     *         <td>{@code 5}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -2199023255552} to {@code 2199023255551}</td>
     *         <td>{@code 6}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -281474976710656} to {@code 281474976710655}</td>
     *         <td>{@code 7}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -36028797018963968} to {@code 36028797018963967}</td>
     *         <td>{@code 8}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -4611686018427387904} to {@code 4611686018427387903}</td>
     *         <td>{@code 9}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code -9223372036854775808} to {@code 9223372036854775807}</td>
     *         <td>{@code 10}</td>
     *     </tr>
     * </table>
     * @see VarInt
     */
    public void writeVarInt64(long v) throws IOException {
        VarInt.of(v).write(this);
    }

    /**
     * Writes a variable length 32-bit unsigned integer
     * <table>
     *     <tr>
     *         <th>Range</th>
     *         <th>Byte Count</th>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 127}</td>
     *         <td>{@code 1}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 32767}</td>
     *         <td>{@code 2}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 4194303}</td>
     *         <td>{@code 3}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 536870911}</td>
     *         <td>{@code 4}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 4294967295}</td>
     *         <td>{@code 5}</td>
     *     </tr>
     * </table>
     * @see VarUInt
     */
    public void writeVarUInt32(int v) throws IOException {
        VarUInt.of(v).write(this);
    }

    /**
     * Writes a variable length 32-bit unsigned integer.
     * The higher 32 bits of the long value will be ignored.
     * <table>
     *     <tr>
     *         <th>Range</th>
     *         <th>Byte Count</th>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 127}</td>
     *         <td>{@code 1}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 32767}</td>
     *         <td>{@code 2}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 4194303}</td>
     *         <td>{@code 3}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 536870911}</td>
     *         <td>{@code 4}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 4294967295}</td>
     *         <td>{@code 5}</td>
     *     </tr>
     * </table>
     * @see VarUInt
     */
    public void writeVarUInt32(long v) throws IOException {
        this.writeVarUInt32((int) v);
    }

    /**
     * Writes a variable length 64-bit unsigned integer.
     * <table>
     *     <tr>
     *         <th>Range</th>
     *         <th>Byte Count</th>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 127}</td>
     *         <td>{@code 1}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 32767}</td>
     *         <td>{@code 2}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 4194303}</td>
     *         <td>{@code 3}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 536870911}</td>
     *         <td>{@code 4}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 68719476735}</td>
     *         <td>{@code 5}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 8796093022207}</td>
     *         <td>{@code 6}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 1125899906842623}</td>
     *         <td>{@code 7}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 144115188075855871}</td>
     *         <td>{@code 8}</td>
     *     </tr>
     *     <tr>
     *         <td>{@code 0} to {@code 18446744073709551615}</td>
     *         <td>{@code 9}</td>
     *     </tr>
     * </table>
     * @see VarUInt
     */
    public void writeVarUInt64(long v) throws IOException {
        VarUInt.of(v).write(this);
    }

    /**
     * Writes a variable length 32-bit float using
     * <a href="https://github.com/michaeljclark/vf128">vf128</a> encoding.
     * This will use {@code 0} to {@link VarFloat#MAX_BYTES MAX_BYTES} bytes depending on the nature
     * of the value. Whole numbers, small rational numbers, zero, NaN and infinity are good candidates
     * for this method.
     * @see VarFloat
     */
    public void writeVarFloat(float v) throws IOException {
        VarFloat.of(v).write(this);
    }

    /**
     * Writes a variable length 64-bit float using
     * <a href="https://github.com/michaeljclark/vf128">vf128</a> encoding.
     * This will use {@code 0} to {@link VarDouble#MAX_BYTES MAX_BYTES} bytes depending on the nature
     * of the value. Whole numbers, small rational numbers, zero, NaN and infinity are good candidates
     * for this method.
     * @see VarDouble
     */
    public void writeVarDouble(double d) throws IOException {
        VarDouble.of(d).write(this);
    }

}
