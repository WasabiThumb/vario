package io.github.wasabithumb.vario.varint;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
abstract class AbstractVarUInt implements VarUInt {

    @Override
    public int hashCode() {
        return Long.hashCode(this.value());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractVarUInt)) return false;
        return this.value() == ((AbstractVarUInt) obj).value();
    }

    @Override
    public @NotNull String toString() {
        return "VarUInt[" + Long.toUnsignedString(this.value()) + "]";
    }

}
