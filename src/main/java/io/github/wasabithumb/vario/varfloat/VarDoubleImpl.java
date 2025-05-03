package io.github.wasabithumb.vario.varfloat;

import io.github.wasabithumb.vario.varfloat.util.VF64;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

@ApiStatus.Internal
final class VarDoubleImpl implements VarDouble {

    private final double value;
    private transient volatile byte[] data;

    VarDoubleImpl(double value) {
        this.value = value;
        this.data = null;
    }

    //

    private byte @NotNull [] data() {
        byte[] data = this.data;
        if (data == null) {
            data = VF64.write(this.value);
            this.data = data;
        }
        return data;
    }

    @Override
    public double value() {
        return this.value;
    }

    @Override
    public int length() {
        return this.data().length;
    }

    @Override
    public @Range(from = 0, to = 0xFF) int byteAt(int index) throws IndexOutOfBoundsException {
        return this.data()[index];
    }

    @Override
    public byte @NotNull [] toArray() {
        final byte[] b = this.data();
        return Arrays.copyOf(b, b.length);
    }

    @Override
    public void write(@NotNull OutputStream out) throws IOException {
        out.write(this.data());
    }

    //

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof VarDouble) {
            return this.value == ((VarDouble) obj).value();
        }
        return super.equals(obj);
    }

    @Override
    public @NotNull String toString() {
        return "VarDouble[" + this.value + "]";
    }

}
