package io.github.wasabithumb.vario.varfloat.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

@ApiStatus.Internal
final class VFOutput {

    static final int DEFAULT_MAX_LENGTH = 32;

    //

    private final byte[] buf;
    private final int max;
    private int head;

    VFOutput(int max) {
        this.buf = new byte[max];
        this.max = max;
        this.head = 0;
    }

    VFOutput() {
        this(DEFAULT_MAX_LENGTH);
    }

    //

    public void put(byte b) {
        if (this.head == this.max)
            throw new BufferOverflowException();
        this.buf[this.head++] = b;
    }

    public void put(int b) {
        this.put((byte) b);
    }

    public void putInt64(int len, long value) {
        long o = VFBitManipulation.le64(value);
        ByteBuffer bb = VFBuffers.vf64Buffer();
        bb.putLong(0, o);
        for (int i=0; i < len; i++) {
            this.put(bb.get(i));
        }
    }

    public byte @NotNull [] array() {
        if (this.head == this.max) return this.buf;
        byte[] cpy = new byte[this.head];
        System.arraycopy(this.buf, 0, cpy, 0, this.head);
        return cpy;
    }

}
