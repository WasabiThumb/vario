package io.github.wasabithumb.vario.varint;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

@ApiStatus.Internal
final class LongVarInt extends AbstractVarInt {

    private final long value;
    private final int length;
    private final boolean flip;

    LongVarInt(long value, int length, boolean flip) {
        this.value = value;
        this.length = length;
        this.flip = flip;
    }

    //

    @Override
    public long value() {
        long v = this.value;
        if (this.flip) v = ~v;
        return v;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public @Range(from = 0, to = 0xFF) int byteAt(int index) throws IndexOutOfBoundsException {
        final int limit = this.length - 1;
        if (index < 0 || index > limit)
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + this.length);

        int ret;
        if (index == 0) {
            ret = (int) (this.value & 0x3F);
            if (this.flip) ret |= 0x40;
        } else {
            long d = this.value >> (6 + 7 * (index - 1));
            ret = (int) (d & 0x7F);
        }
        if (index != limit) ret |= 0x80;
        return ret;
    }

}
