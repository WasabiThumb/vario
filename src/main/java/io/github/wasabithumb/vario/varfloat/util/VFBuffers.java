package io.github.wasabithumb.vario.varfloat.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Replacement for C pointer tricks
 */
@ApiStatus.Internal
final class VFBuffers {

    private static final ThreadLocal<ByteBuffer> F32_BUFFER = ThreadLocal.withInitial(() -> {
        ByteBuffer bb = ByteBuffer.allocateDirect(Float.BYTES);
        bb.order(VFBitManipulation.NATIVE_ORDER);
        return bb;
    });

    private static final ThreadLocal<ByteBuffer> F64_BUFFER = ThreadLocal.withInitial(() -> {
        ByteBuffer bb = ByteBuffer.allocateDirect(Double.BYTES);
        bb.order(VFBitManipulation.NATIVE_ORDER);
        return bb;
    });

    //

    static @NotNull ByteBuffer vf32Buffer() {
        return F32_BUFFER.get();
    }

    static @NotNull ByteBuffer vf64Buffer() {
        return F64_BUFFER.get();
    }

    //

    private VFBuffers() { }

}
