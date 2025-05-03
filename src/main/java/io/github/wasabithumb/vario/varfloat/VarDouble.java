package io.github.wasabithumb.vario.varfloat;

import io.github.wasabithumb.vario.sequence.ByteSequence;
import io.github.wasabithumb.vario.varfloat.util.VF64;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

@ApiStatus.NonExtendable
public interface VarDouble extends ByteSequence {

    int MAX_BYTES = 10;

    VarDouble ZERO = new VarDoubleImpl(0d);

    @Contract("_ -> new")
    static @NotNull VarDouble of(double v) {
        return new VarDoubleImpl(v);
    }

    @Contract("_ -> new")
    static @NotNull VarDouble of(byte @NotNull [] array) {
        return of(ByteSequence.wrap(array));
    }

    @Contract("_, _, _ -> new")
    static @NotNull VarDouble of(byte @NotNull [] array, int offset, int length) {
        return of(ByteSequence.wrap(array, offset, length));
    }

    @Contract("_ -> new")
    static @NotNull VarDouble of(@NotNull ByteSequence data) {
        return of(VF64.read(data));
    }

    @Contract("_ -> new")
    static @NotNull VarDouble read(@NotNull InputStream in) throws IOException {
        return of(VF64.read(in));
    }

    //

    double value();

}
