package io.github.wasabithumb.vario.varint;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@ApiStatus.Internal
final class ArrayVarInt extends AbstractVarInt {

    private final byte[] data;

    ArrayVarInt(byte @NotNull [] data) {
        this.data = data;
    }

    //

    @Override
    public long value() {
        long v;
        boolean flip;
        long n;
        int h;

        n = this.data[0] & 0xFFL;
        flip = (n & 0x40L) != 0L;
        v = n & 0x3FL;
        h = 6;

        for (int i=1; i < this.data.length; i++) {
            n = this.data[i] & 0x7F;
            v |= (n << h);
            h += 7;
        }

        return flip ? (~v) : v;
    }

    @Override
    public int length() {
        return this.data.length;
    }

    @Override
    public @Range(from = 0, to = 0xFF) int byteAt(int index) throws IndexOutOfBoundsException {
        return this.data[index];
    }

}
