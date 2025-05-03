package io.github.wasabithumb.vario.varfloat.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;

public abstract class VFInputException extends Exception {

    @Contract("-> new")
    public static @NotNull VFInputException eof() {
        return new EOF();
    }


    @Contract("_ -> new")
    public static @NotNull VFInputException source(@NotNull IOException cause) {
        return new Source(cause);
    }

    //

    protected VFInputException(@Nullable Throwable cause) {
        super(cause);
    }

    @Contract("-> fail")
    public abstract void rethrowIO() throws IOException;

    @Contract("-> fail")
    public abstract void rethrowArg() throws IllegalArgumentException;

    //

    public static final class EOF extends VFInputException {

        public EOF() {
            super(null);
        }

        @Override
        @Contract("-> fail")
        public void rethrowIO() throws IOException {
            EOFException eof = new EOFException("Truncated or malformed data");
            eof.addSuppressed(this);
            throw eof;
        }

        @Override
        @Contract("-> fail")
        public void rethrowArg() throws IllegalArgumentException {
            IllegalArgumentException arg = new IllegalArgumentException("Truncated or malformed data");
            arg.addSuppressed(this);
            throw arg;
        }

    }

    public static final class Source extends VFInputException {

        public Source(@NotNull IOException cause) {
            super(cause);
        }

        @Override
        public @NotNull IOException getCause() {
            return (IOException) super.getCause();
        }

        @Override
        @Contract("-> fail")
        public void rethrowIO() throws IOException {
            throw this.getCause();
        }

        @Override
        @Contract("-> fail")
        public void rethrowArg() {
            AssertionError ae = new AssertionError("Illegal code path");
            ae.addSuppressed(this);
            throw ae;
        }

    }

}
