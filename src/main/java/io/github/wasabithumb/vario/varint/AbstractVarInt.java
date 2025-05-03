package io.github.wasabithumb.vario.varint;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
abstract class AbstractVarInt implements VarInt {

    @Override
    public int hashCode() {
        return Long.hashCode(this.value());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractVarInt)) return false;
        return this.value() == ((AbstractVarInt) obj).value();
    }

    @Override
    public @NotNull String toString() {
        return "VarInt[" + this.value() + "]";
    }

}
