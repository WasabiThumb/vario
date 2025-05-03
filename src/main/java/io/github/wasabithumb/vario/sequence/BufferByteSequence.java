package io.github.wasabithumb.vario.sequence;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@ApiStatus.Internal
final class BufferByteSequence extends AbstractByteSequence {

    private final ByteBuffer backing;

    BufferByteSequence(@NotNull ByteBuffer backing) {
        this.backing = backing;
    }

    //

    @Override
    public int length() {
        return this.backing.limit();
    }

    @Override
    public @Range(from = 0, to = 0xFF) int byteAt(int index) throws IndexOutOfBoundsException {
        return this.backing.get(index) & 0xFF;
    }

    @Override
    public byte @NotNull [] toArray() {
        if (!this.backing.hasArray())
            return super.toArray();

        byte[] array = this.backing.array();
        int offset = this.backing.arrayOffset();
        int limit = this.backing.limit();

        byte[] cpy = new byte[limit];
        System.arraycopy(array, offset, cpy, 0, limit);
        return cpy;
    }

    @Override
    public void write(@NotNull OutputStream out) throws IOException {
        if (!this.backing.hasArray()) {
            super.write(out);
            return;
        }

        out.write(
                this.backing.array(),
                this.backing.arrayOffset(),
                this.backing.limit()
        );
    }
}
