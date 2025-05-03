package io.github.wasabithumb.vario.varfloat.util;

import io.github.wasabithumb.vario.sequence.ByteSequence;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

@ApiStatus.Internal
interface VFInput {

    byte readByte() throws VFInputException;

    int readUnsignedByte() throws VFInputException;

    default long readInt64(int len) throws VFInputException {
        long l = 0;
        for (int i=0; i < len; i++) {
            l |= ((long) this.readUnsignedByte()) << (i << 3);
        }
        return l;
    }

    default long readInt64SE(int len) throws VFInputException {
        long v = this.readInt64(len);
        int y = 64 - (len << 3);
        return (v << y) >> y;
    }

    //

    final class Sequence implements VFInput {

        private final ByteSequence sequence;
        private int head;

        Sequence(@NotNull ByteSequence sequence) {
            this.sequence = sequence;
            this.head = 0;
        }

        @Override
        public int readUnsignedByte() throws VFInputException {
            if (this.head >= this.sequence.length()) throw VFInputException.eof();
            return this.sequence.byteAt(this.head++);
        }

        @Override
        public byte readByte() throws VFInputException {
            return (byte) this.readUnsignedByte();
        }

    }

    //

    final class Stream implements VFInput {

        private final InputStream in;

        Stream(@NotNull InputStream in) {
            this.in = in;
        }

        @Override
        public int readUnsignedByte() throws VFInputException {
            int n;
            try {
                n = this.in.read();
            } catch (IOException e) {
                throw VFInputException.source(e);
            }
            if (n == -1) throw VFInputException.eof();
            return n;
        }

        @Override
        public byte readByte() throws VFInputException {
            return (byte) this.readUnsignedByte();
        }

    }

}
