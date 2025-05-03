package io.github.wasabithumb.vario.varint;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class LongVarUInt extends AbstractVarUInt {

    private final long value;
    private final int byteLength;

    LongVarUInt(long value, int byteLength) {
        this.value = value;
        this.byteLength = byteLength;
    }

    //

    @Override
    public long value() {
        return this.value;
    }

    @Override
    public int length() {
        return this.byteLength;
    }

    @Override
    public int byteAt(int offset) throws IndexOutOfBoundsException {
        final int limit = this.byteLength - 1;
        if (offset < 0 || offset > limit)
            throw new IndexOutOfBoundsException("Index " + offset + " out of bounds for length " + this.byteLength);
        long ret = this.value >> ((offset << 3) - offset);
        if (offset == 8) {
            ret &= 0xffL;
        } else {
            ret &= 0x7fL;
            if (offset != limit) ret |= 0x80;
        }
        return (int) ret;
    }

}
