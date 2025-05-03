package io.github.wasabithumb.vario.varfloat.util;

import org.jetbrains.annotations.ApiStatus;

import java.nio.ByteOrder;

@ApiStatus.Internal
final class VFBitManipulation {

    static final ByteOrder NATIVE_ORDER;
    static final boolean BIG_ENDIAN;
    static final boolean LITTLE_ENDIAN;
    static {
        // Native order: Could speed up buffer access
        NATIVE_ORDER = ByteOrder.nativeOrder();
        BIG_ENDIAN = NATIVE_ORDER == ByteOrder.BIG_ENDIAN;
        LITTLE_ENDIAN = !BIG_ENDIAN;

        // Big endian: Could fix bugs
        // NATIVE_ORDER = ByteOrder.BIG_ENDIAN;
        // BIG_ENDIAN = true;
        // LITTLE_ENDIAN = false;
    }

    //

    static int clz(int val) {
        return Integer.numberOfLeadingZeros(val);
    }

    static int clz(long val) {
        return Long.numberOfLeadingZeros(val);
    }

    static int ctz(int val) {
        return Integer.numberOfTrailingZeros(val);
    }

    static int ctz(long val) {
        return Long.numberOfTrailingZeros(val);
    }

    static int blen(long v) {
        if (v == 0) return 1;
        // return v == 0 ? 1 : 8 - ((clz(v < 0 ? ~v : v) - 1) / 8);
        return Math.floorDiv(63 - clz(v < 0 ? ~v : v), 8) + 1;
    }

    static int blens(long v) {
        return v == 0 ? 1 : 8 - ((clz(v < 0 ? ~v : v) - 1) / 8);
    }

    //

    static long bswap64(long x) {
        return ((x >> 56) & 0x000000FFL) |
                ((x >> 40) & 0x0000FF00L) |
                ((x >> 24) & 0x00FF0000L) |
                ((x >> 8) & 0xFF000000L) |
                ((x & 0xFF000000L) << 8) |
                ((x & 0x00FF0000L) << 24) |
                ((x & 0x0000FF00L) << 40) |
                ((x & 0x000000FFL) << 56);
    }

    //

    static long le64(long v) {
        return LITTLE_ENDIAN ? v : bswap64(v);
    }

    //

    private VFBitManipulation() { }

}
