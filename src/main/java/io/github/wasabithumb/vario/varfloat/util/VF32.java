package io.github.wasabithumb.vario.varfloat.util;

import io.github.wasabithumb.vario.sequence.ByteSequence;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static io.github.wasabithumb.vario.varfloat.util.VFBitManipulation.*;
import static io.github.wasabithumb.vario.varfloat.util.VFConstants.*;
import static io.github.wasabithumb.vario.varfloat.util.VFMethods.*;

/**
 * 32-bit variable length floating point math
 * based on <a href="https://github.com/michaeljclark/vf128">vf128</a>
 */
@ApiStatus.Internal
public final class VF32 {

    public static float read(@NotNull ByteSequence data) throws IllegalArgumentException {
        VFInput in = new VFInput.Sequence(data);
        try {
            return readInternal(in);
        } catch (VFInputException e) {
            e.rethrowArg();
            return 0f;
        }
    }

    public static float read(@NotNull InputStream data) throws IOException {
        VFInput in = new VFInput.Stream(data);
        try {
            return readInternal(in);
        } catch (VFInputException e) {
            e.rethrowIO();
            return 0f;
        }
    }

    public static byte @NotNull [] write(float value) {
        VFOutput out = new VFOutput(6);
        writeInternal(out, value);
        return out.array();
    }

    //

    private static float readInternal(@NotNull VFInput in) throws VFInputException {
        byte pre;
        float v;
        boolean vfInl;
        boolean vfSgn;
        int vfExp;
        int vfMan;
        int vrMan = 0;
        int vrExp = 0;
        int vpMan;
        int vpExp;

        pre = in.readByte();

        vfInl = ((pre >> 7) & 1) == 0;
        vfSgn = ((pre >> 6) & 1) == 1;
        vfExp =  (pre >> 4) & 3;
        vfMan =   pre       & 15;

        if (!vfInl) {
            if (vfExp != 0) {
                vrExp = (int) in.readInt64SE(vfExp);
            }
            if (vfMan != 0) {
                long r = in.readInt64(vfMan);
                int lz = clz(r);
                int sh = lz < 32 ? 32 - lz : 0;
                vrMan = (int) (r >>> sh);
            }
        }

        if (vfInl) {
            if (vfExp == 0) {
                if (vfMan > 0) {
                    int lz = clz(vfMan);
                    vpExp = F32_EXP_BIAS + 27 - lz;
                    vpMan = (vfMan << (lz + 1)) >>> (F32_EXP_SIZE + 1);
                } else {
                    vpExp = 0;
                    vpMan = 0;
                }
            } else if (vfExp == 3) {
                vpExp = F32_EXP_MASK;
                vpMan = vfMan << (F32_MANT_SIZE - 4);
            } else {
                vpExp = F32_EXP_BIAS + vfExp - 1;
                vpMan = vfMan << (F32_MANT_SIZE - 4);
            }
        } else {
            int lz = clz(vrMan);
            int tz = ctz(vrMan);
            if (vrExp <= -F32_EXP_BIAS) {
                int sh = F32_EXP_BIAS + vrExp + lz - F32_EXP_SIZE;
                vpExp = 0;
                vpMan = vrMan << sh;
            } else {
                if (vfExp == 0) vrExp = -tz - 1;
                vpExp = F32_EXP_BIAS + vrExp;
                vpMan = vrMan << (lz + 1) >>> (F32_EXP_SIZE + 1);
            }
        }

        v = f32_pack_float(new F32Struct(vpMan, vpExp, vfSgn ? 1 : 0));
        return v;
    }

    private static void writeInternal(@NotNull VFOutput out, final float v) {
        byte pre;
        F32Struct d = f32_unpack_float(v);
        int sexp = d.sexp();
        int frac = d.frac();
        int vfExp = 0;
        int vfMan = 0;
        int vwMan = 0;
        int vwExp = 0;

        if (sexp == (F32_EXP_BIAS + 1)) {                                                        // Inf/NaN
            vfExp = 3;
            vfMan = (frac != 0) ? 8 : 0;
            pre = (byte) ((d.sign << 6) | (vfExp << 4) | vfMan);
        } else if (sexp == -F32_EXP_BIAS && frac == 0) {                                         // Zero
            pre = (byte) (d.sign << 6);
        } else if (sexp <= 1 && sexp >= 0 && (frac & U32_MSN) == frac) {                         // Inline (normal)
            pre = (byte) ((d.sign << 6) | ((byte) ((sexp + 1) << 4)) | ((byte) (frac >>> 28)));
        } else if (sexp <= -1 && sexp >= -4 && ((frac >> -sexp) & U32_MSN) == (frac >> -sexp)) { // Inline (subnormal)
            pre = (byte) ((d.sign << 6) | (((byte) (0x10 | (frac >>> 28))) >>> -sexp));
        } else {                                                                                 // Out-of-line
            int tz = ctz(frac);
            int lz = clz(frac);

            if (sexp == -F32_EXP_BIAS) {
                vwMan = frac >>> tz;
                vwExp = sexp - lz - 1;
                vfExp = blens(vwExp) & 0xFF;
                vfMan = blen(vwMan) & 0xFF;
                pre = (byte) (0x80 | (d.sign << 6) | (vfExp << 4) | vfMan);
            } else if (frac == 0) {
                vwExp = sexp;
                vfExp = blens(vwExp) & 0xFF;
                pre = (byte) (0x80 | (d.sign << 6) | (vfExp << 4));
            } else if (sexp < 0 && sexp >= -8) {
                int sh = -sexp - 1;
                int vwManA = (frac >>> tz) | (U32_MSB >>> (tz - 1));
                int vwManB = ((frac >>> tz) << sh) | ((U32_MSB >>> (tz - 1)) << sh);
                int vfExpA = blens(sexp) & 0xFF;
                int vfManA = blen(vwManA) & 0xFF;
                int vfManB = blen(vwManB) & 0xFF;
                if (vfManA + vfExpA < vfManB) {
                    vwMan = vwManA;
                    vwExp = sexp;
                    vfExp = vfExpA;
                    vfMan = vfManA;
                } else {
                    vwMan = vwManB;
                    vfMan = vfManB;
                }
                pre = (byte) (0x80 | (d.sign << 6) | (vfExp << 4) | vfMan);
            } else {
                vwMan = (frac >>> tz) | (U32_MSB >>> (tz - 1));
                vwExp = sexp;
                vfExp = blens(vwExp) & 0xFF;
                vfMan = blen(vwMan) & 0xFF;
                pre = (byte) (0x80 | (d.sign << 6) | (vfExp << 4) | vfMan);
            }
        }

        out.put(pre);

        if ((pre & ((byte) 0x80)) != ((byte) 0)) {
            if (vfExp != 0) {
                out.putInt64(vfExp, vwExp);
            }
            if (vfMan != 0) {
                out.putInt64(vfMan, vwMan);
            }
        }
    }

    //

    private VF32() { }

}
