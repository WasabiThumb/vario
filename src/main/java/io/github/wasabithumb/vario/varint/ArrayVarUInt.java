package io.github.wasabithumb.vario.varint;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@ApiStatus.Internal
final class ArrayVarUInt extends AbstractVarUInt {

    private final byte[] data;

    ArrayVarUInt(byte @NotNull [] data) {
        this.data = data;
    }

    //

    @Override
    public long value() {
        final int limit = this.data.length - 1;
        long v = 0L;
        long b;
        for (int i=0; i <= limit; i++) {
            b = this.data[i];
            b &= ((i == limit) ? 0xffL : 0x7fL);
            b <<= ((i << 3) - i);
            v |= b;
        }
        return v;
    }

    @Override
    public int length() {
        return this.data.length;
    }

    @Override
    public @Range(from = 0, to = 0xFF) int byteAt(int offset) throws IndexOutOfBoundsException {
        return this.data[offset];
    }

}
