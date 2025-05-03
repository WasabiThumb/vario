package io.github.wasabithumb.vario.varfloat;

import io.github.wasabithumb.vario.sequence.ByteSequence;
import io.github.wasabithumb.vario.varfloat.util.VF32;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

@ApiStatus.NonExtendable
public interface VarFloat extends ByteSequence {

    int MAX_BYTES = 6;

    VarFloat ZERO = new VarFloatImpl(0f);

    @Contract("_ -> new")
    static @NotNull VarFloat of(float v) {
        return new VarFloatImpl(v);
    }

    @Contract("_ -> new")
    static @NotNull VarFloat of(byte @NotNull [] array) {
        return of(ByteSequence.wrap(array));
    }

    @Contract("_, _, _ -> new")
    static @NotNull VarFloat of(byte @NotNull [] array, int offset, int length) {
        return of(ByteSequence.wrap(array, offset, length));
    }

    @Contract("_ -> new")
    static @NotNull VarFloat of(@NotNull ByteSequence data) {
        return of(VF32.read(data));
    }

    @Contract("_ -> new")
    static @NotNull VarFloat read(@NotNull InputStream in) throws IOException {
        return of(VF32.read(in));
    }

    //

    float value();

}
