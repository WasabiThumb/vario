package io.github.wasabithumb.vario.varfloat.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static io.github.wasabithumb.vario.varfloat.util.VFBuffers.*;
import static io.github.wasabithumb.vario.varfloat.util.VFConstants.*;

import java.nio.ByteBuffer;

@ApiStatus.Internal
final class VFMethods {

    static float f32_from_bits(int v) {
        ByteBuffer bb = vf32Buffer();
        bb.putInt(0, v);
        return bb.getFloat(0);
    }

    static int f32_to_bits(float v) {
        ByteBuffer bb = vf32Buffer();
        bb.putFloat(0, v);
        return bb.getInt(0);
    }

    static int f32_mant_dec(float x) {
        return (f32_to_bits(x) >> F32_MANT_SHIFT) & F32_MANT_MASK;
    }

    static int f32_exp_dec(float x) {
        return (f32_to_bits(x) >> F32_EXP_SHIFT) & F32_EXP_MASK;
    }

    static int f32_sign_dec(float x) {
        return (f32_to_bits(x) >> F32_SIGN_SHIFT) & F32_SIGN_MASK;
    }

    static int f32_mant_enc(int v) {
        return ((v & F32_MANT_MASK) << F32_MANT_SHIFT);
    }

    static int f32_exp_enc(int v) {
        return ((v & F32_EXP_MASK) << F32_EXP_SHIFT);
    }

    static int f32_sign_enc(int v) {
        return ((v & F32_SIGN_MASK) << F32_SIGN_SHIFT);
    }

    /*
    static boolean f32_is_zero(float x) {
        return f32_exp_dec(x) == 0 && f32_mant_dec(x) == 0;
    }

    static boolean f32_is_inf(float x) {
        return f32_exp_dec(x) == F32_EXP_MASK && f32_mant_dec(x) == 0;
    }

    static boolean f32_is_nan(float x) {
        return f32_exp_dec(x) == F32_EXP_MASK && f32_mant_dec(x) != 0;
    }

    static boolean f32_is_denorm(float x) {
        return f32_exp_dec(x) == 0 && f32_mant_dec(x) != 0;
    }
    */

    static @NotNull F32Struct f32_unpack_float(float x) {
        return new F32Struct(f32_mant_dec(x), f32_exp_dec(x), f32_sign_dec(x));
    }

    static float f32_pack_float(@NotNull F32Struct s) {
        return f32_from_bits(f32_mant_enc(s.mant) | f32_exp_enc(s.exp) | f32_sign_enc(s.sign));
    }

    //

    static double f64_from_bits(long v) {
        ByteBuffer bb = vf64Buffer();
        bb.putLong(0, v);
        return bb.getDouble(0);
    }

    static long f64_to_bits(double v) {
        ByteBuffer bb = vf64Buffer();
        bb.putDouble(0, v);
        return bb.getLong(0);
    }

    static long f64_mant_dec(double x) {
        return (f64_to_bits(x) >> F64_MANT_SHIFT) & F64_MANT_MASK;
    }

    static long f64_exp_dec(double x) {
        return (f64_to_bits(x) >> F64_EXP_SHIFT) & F64_EXP_MASK;
    }

    static long f64_sign_dec(double x) {
        return (f64_to_bits(x) >> F64_SIGN_SHIFT) & F64_SIGN_MASK;
    }

    static long f64_mant_enc(long v) {
        return ((v & F64_MANT_MASK) << F64_MANT_SHIFT);
    }

    static long f64_exp_enc(long v) {
        return ((v & F64_EXP_MASK) << F64_EXP_SHIFT);
    }

    static long f64_sign_enc(long v) {
        return ((v & F64_SIGN_MASK) << F64_SIGN_SHIFT);
    }

    /*
    static boolean f64_is_zero(double x) {
        return f64_exp_dec(x) == 0 && f64_mant_dec(x) == 0;
    }

    static boolean f64_is_inf(double x) {
        return f64_exp_dec(x) == F64_EXP_MASK && f64_mant_dec(x) == 0;
    }

    static boolean f64_is_nan(double x) {
        return f64_exp_dec(x) == F64_EXP_MASK && f64_mant_dec(x) != 0;
    }

    static boolean f64_is_denorm(double x) {
        return f64_exp_dec(x) == 0 && f64_mant_dec(x) != 0;
    }
    */

    static @NotNull F64Struct f64_unpack_float(double x) {
        return new F64Struct(f64_mant_dec(x), f64_exp_dec(x), f64_sign_dec(x));
    }

    static double f64_pack_float(@NotNull F64Struct s) {
        return f64_from_bits(f64_mant_enc(s.mant) | f64_exp_enc(s.exp) | f64_sign_enc(s.sign));
    }

    //

    private VFMethods() { }

    //

    public static final class F32Struct {

        public int mant;
        public int exp;
        public int sign;

        public F32Struct(int mant, int exp, int sign) {
            this.mant = mant;
            this.exp = exp;
            this.sign = sign;
        }

        public int sexp() {
            return this.exp - F32_EXP_BIAS;
        }

        public int frac() {
            return this.mant << (F32_EXP_SIZE + 1);
        }

    }

    public static final class F64Struct {

        public long mant;
        public long exp;
        public long sign;

        public F64Struct(long mant, long exp, long sign) {
            this.mant = mant;
            this.exp = exp;
            this.sign = sign;
        }

        public long sexp() {
            return this.exp - F64_EXP_BIAS;
        }

        public long frac() {
            return this.mant << (F64_EXP_SIZE + 1);
        }

    }

}
