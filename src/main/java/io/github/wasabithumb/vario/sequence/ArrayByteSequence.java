package io.github.wasabithumb.vario.sequence;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.OutputStream;

@ApiStatus.Internal
final class ArrayByteSequence extends AbstractByteSequence {

    private final byte[] arr;
    private final int offset;
    private final int length;

    ArrayByteSequence(byte @NotNull [] arr, int offset, int length) {
        this.arr = arr;
        this.offset = offset;
        this.length = length;
    }

    //

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public @Range(from = 0, to = 0xFF) int byteAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.length)
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + this.length);
        return this.arr[this.offset + index] & 0xFF;
    }

    @Override
    public byte @NotNull [] toArray() {
        byte[] ret = new byte[this.length];
        System.arraycopy(this.arr, this.offset, ret, 0, this.length);
        return ret;
    }

    @Override
    public void write(@NotNull OutputStream out) throws IOException {
        out.write(this.arr, this.offset, this.length);
    }

}
