package io.github.wasabithumb.vario.varint.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

@ApiStatus.Internal
public final class VI {

    /**
     * Reads up to {@code maxBytes} bytes from {@code in},
     * stopping early if any byte does not have its highest bit set.
     * The last byte read, in either condition, is stored as
     * the result of a bitwise AND with itself and {@code trailMask}.
     */
    public static byte @NotNull [] readVariableSequence(@NotNull InputStream in, int maxBytes, int trailMask) throws IOException {
        byte[] data = new byte[maxBytes];
        int next;

        next = in.read();
        if (next == -1) throw new EOFException("Cannot read variable sequence at end of stream");
        data[0] = (byte) next;

        int head = 1;
        while ((next & 0x80) == 0x80) {
            next = in.read();
            if (next == -1) {
                throw new EOFException("Truncated or corrupted variable sequence");
            }
            if (head == (maxBytes - 1)) {
                data[head] = (byte) (next & trailMask);
                return data;
            } else {
                data[head++] = (byte) next;
            }
        }

        byte[] shrink = new byte[head];
        System.arraycopy(data, 0, shrink, 0, head);
        return shrink;
    }

    //

    private VI() { }

}
