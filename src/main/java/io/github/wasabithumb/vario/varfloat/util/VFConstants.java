package io.github.wasabithumb.vario.varfloat.util;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class VFConstants {

    static final int F32_EXP_SIZE = 8;
    static final int F32_MANT_SIZE = 23;

    static final int F32_MANT_SHIFT = 0;
    static final int F32_EXP_SHIFT = F32_MANT_SIZE;
    static final int F32_SIGN_SHIFT = F32_MANT_SIZE + F32_EXP_SIZE;

    static final int F32_MANT_MASK = (1 << F32_MANT_SIZE) - 1;
    static final int F32_EXP_MASK = (1 << F32_EXP_SIZE) - 1;
    static final int F32_SIGN_MASK = 1;

    // static final int F32_MANT_PREFIX = (1 << F32_MANT_SIZE);
    static final int F32_EXP_BIAS = (1 << (F32_EXP_SIZE - 1)) - 1;

    //

    static final int F64_EXP_SIZE = 11;
    static final int F64_MANT_SIZE = 52;

    static final int F64_MANT_SHIFT = 0;
    static final int F64_EXP_SHIFT = F64_MANT_SIZE;
    static final int F64_SIGN_SHIFT = F64_MANT_SIZE + F64_EXP_SIZE;

    static final long F64_MANT_MASK = (1L << F64_MANT_SIZE) - 1L;
    static final long F64_EXP_MASK = (1L << F64_EXP_SIZE) - 1L;
    static final long F64_SIGN_MASK = 1L;

    // static final long F64_MANT_PREFIX = (1L << F64_MANT_SIZE);
    static final long F64_EXP_BIAS = (1L << (F64_EXP_SIZE - 1)) - 1L;

    //

    static final int U32_MSB = (int) 0x80000000L;
    static final int U32_MSN = (int) 0xf0000000L;

    static final long U64_MSB = 0x8000000000000000L;
    static final long U64_MSN = 0xf000000000000000L;

    //

    private VFConstants() { }

}
