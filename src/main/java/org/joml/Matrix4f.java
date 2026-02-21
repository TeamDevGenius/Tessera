package org.joml;

import com.badlogic.gdx.math.Matrix4;
import java.nio.FloatBuffer;

/**
 * JOML-compatible Matrix4f backed by LibGDX Matrix4.
 * Provides JOML API without the LWJGL JOML dependency.
 */
public class Matrix4f {
    private final float[] m = new float[16];

    public Matrix4f() { identity(); }

    public Matrix4f(Matrix4f src) { System.arraycopy(src.m, 0, m, 0, 16); }

    public Matrix4f(float[] values) {
        if (values != null && values.length >= 16) System.arraycopy(values, 0, m, 0, 16);
        else identity();
    }

    public Matrix4f identity() {
        for (int i = 0; i < 16; i++) m[i] = 0;
        m[0] = m[5] = m[10] = m[15] = 1f;
        return this;
    }

    public Matrix4f set(Matrix4f other) { System.arraycopy(other.m, 0, m, 0, 16); return this; }

    public Matrix4f set(float[] values) { System.arraycopy(values, 0, m, 0, 16); return this; }

    public Matrix4f perspective(float fovY, float aspect, float zNear, float zFar) {
        float tanHalfFov = (float) Math.tan(fovY / 2.0);
        for (int i = 0; i < 16; i++) m[i] = 0;
        m[0] = 1f / (aspect * tanHalfFov);
        m[5] = 1f / tanHalfFov;
        m[10] = -(zFar + zNear) / (zFar - zNear);
        m[11] = -1f;
        m[14] = -(2f * zFar * zNear) / (zFar - zNear);
        return this;
    }

    public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
        identity();
        m[0] = 2f / (right - left);
        m[5] = 2f / (top - bottom);
        m[10] = -2f / (zFar - zNear);
        m[12] = -(right + left) / (right - left);
        m[13] = -(top + bottom) / (top - bottom);
        m[14] = -(zFar + zNear) / (zFar - zNear);
        return this;
    }

    public Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
        float fx = center.x - eye.x, fy = center.y - eye.y, fz = center.z - eye.z;
        float flen = (float) Math.sqrt(fx*fx + fy*fy + fz*fz);
        if (flen != 0) { fx /= flen; fy /= flen; fz /= flen; }
        float sx = fy * up.z - fz * up.y, sy = fz * up.x - fx * up.z, sz = fx * up.y - fy * up.x;
        float slen = (float) Math.sqrt(sx*sx + sy*sy + sz*sz);
        if (slen != 0) { sx /= slen; sy /= slen; sz /= slen; }
        float ux = sy * fz - sz * fy, uy = sz * fx - sx * fz, uz = sx * fy - sy * fx;
        m[0] = sx; m[4] = sy; m[8] = sz; m[12] = -(sx*eye.x + sy*eye.y + sz*eye.z);
        m[1] = ux; m[5] = uy; m[9] = uz; m[13] = -(ux*eye.x + uy*eye.y + uz*eye.z);
        m[2] = -fx; m[6] = -fy; m[10] = -fz; m[14] = (fx*eye.x + fy*eye.y + fz*eye.z);
        m[3] = 0; m[7] = 0; m[11] = 0; m[15] = 1f;
        return this;
    }

    public Matrix4f translate(float x, float y, float z) {
        m[12] += m[0]*x + m[4]*y + m[8]*z;
        m[13] += m[1]*x + m[5]*y + m[9]*z;
        m[14] += m[2]*x + m[6]*y + m[10]*z;
        m[15] += m[3]*x + m[7]*y + m[11]*z;
        return this;
    }

    public Matrix4f translate(Vector3f v) { return translate(v.x, v.y, v.z); }

    /** Sets only the translation part; leaves rotation/scale unchanged. */
    public Matrix4f setTranslation(float x, float y, float z) { m[12] = x; m[13] = y; m[14] = z; return this; }
    public Matrix4f setTranslation(int x, int y, int z) { return setTranslation((float)x, (float)y, (float)z); }
    public Matrix4f setTranslation(Vector3f v) { return setTranslation(v.x, v.y, v.z); }

    /** Gets the translation part of this matrix. */
    public Vector3f getTranslation(Vector3f dest) { dest.x = m[12]; dest.y = m[13]; dest.z = m[14]; return dest; }

    /** Returns a new identity matrix with this translation set. */
    public Matrix4f translation(float x, float y, float z) { identity(); m[12] = x; m[13] = y; m[14] = z; return this; }
    public Matrix4f translation(int x, int y, int z) { return translation((float)x, (float)y, (float)z); }
    public Matrix4f translation(Vector3f v) { return translation(v.x, v.y, v.z); }

    public Matrix4f rotateX(float angle) { return rotate(angle, 1, 0, 0); }
    public Matrix4f rotateY(float angle) { return rotate(angle, 0, 1, 0); }
    public Matrix4f rotateZ(float angle) { return rotate(angle, 0, 0, 1); }

    public float m00() { return m[0]; } public float m10() { return m[1]; } public float m20() { return m[2]; } public float m30() { return m[3]; }
    public float m01() { return m[4]; } public float m11() { return m[5]; } public float m21() { return m[6]; } public float m31() { return m[7]; }
    public float m02() { return m[8]; } public float m12() { return m[9]; } public float m22() { return m[10]; } public float m32() { return m[11]; }
    public float m03() { return m[12]; } public float m13() { return m[13]; } public float m23() { return m[14]; } public float m33() { return m[15]; }
    public Matrix4f m00(float v) { m[0]=v; return this; } public Matrix4f m10(float v) { m[1]=v; return this; }
    public Matrix4f m20(float v) { m[2]=v; return this; } public Matrix4f m30(float v) { m[3]=v; return this; }
    public Matrix4f m01(float v) { m[4]=v; return this; } public Matrix4f m11(float v) { m[5]=v; return this; }
    public Matrix4f m21(float v) { m[6]=v; return this; } public Matrix4f m31(float v) { m[7]=v; return this; }
    public Matrix4f m02(float v) { m[8]=v; return this; } public Matrix4f m12(float v) { m[9]=v; return this; }
    public Matrix4f m22(float v) { m[10]=v; return this; } public Matrix4f m32(float v) { m[11]=v; return this; }
    public Matrix4f m03(float v) { m[12]=v; return this; } public Matrix4f m13(float v) { m[13]=v; return this; }
    public Matrix4f m23(float v) { m[14]=v; return this; } public Matrix4f m33(float v) { m[15]=v; return this; }

    public Matrix4f rotate(float angle, float ax, float ay, float az) {
        float c = (float) Math.cos(angle), s = (float) Math.sin(angle), t = 1 - c;
        float len = (float) Math.sqrt(ax*ax + ay*ay + az*az);
        if (len != 0) { ax /= len; ay /= len; az /= len; }
        float[] rot = new float[16];
        rot[0] = t*ax*ax + c;   rot[4] = t*ax*ay - s*az; rot[8]  = t*ax*az + s*ay; rot[12] = 0;
        rot[1] = t*ax*ay + s*az; rot[5] = t*ay*ay + c;   rot[9]  = t*ay*az - s*ax; rot[13] = 0;
        rot[2] = t*ax*az - s*ay; rot[6] = t*ay*az + s*ax; rot[10] = t*az*az + c;   rot[14] = 0;
        rot[3] = 0; rot[7] = 0; rot[11] = 0; rot[15] = 1;
        mulInternal(rot);
        return this;
    }

    public Matrix4f rotate(float angle, Vector3f axis) { return rotate(angle, axis.x, axis.y, axis.z); }

    public Matrix4f scale(float x, float y, float z) {
        m[0] *= x; m[1] *= x; m[2] *= x; m[3] *= x;
        m[4] *= y; m[5] *= y; m[6] *= y; m[7] *= y;
        m[8] *= z; m[9] *= z; m[10] *= z; m[11] *= z;
        return this;
    }

    public Matrix4f scale(float s) { return scale(s, s, s); }

    public Matrix4f mul(Matrix4f right) {
        mulInternal(right.m);
        return this;
    }

    private void mulInternal(float[] r) {
        float[] l = m.clone();
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                m[col*4 + row] = l[row] * r[col*4] + l[4+row] * r[col*4+1] + l[8+row] * r[col*4+2] + l[12+row] * r[col*4+3];
            }
        }
    }

    public Matrix4f invert() {
        float[] inv = new float[16];
        float[] src = m;
        inv[0]  =  src[5]*src[10]*src[15] - src[5]*src[11]*src[14] - src[9]*src[6]*src[15] + src[9]*src[7]*src[14] + src[13]*src[6]*src[11] - src[13]*src[7]*src[10];
        inv[4]  = -src[4]*src[10]*src[15] + src[4]*src[11]*src[14] + src[8]*src[6]*src[15] - src[8]*src[7]*src[14] - src[12]*src[6]*src[11] + src[12]*src[7]*src[10];
        inv[8]  =  src[4]*src[9]*src[15]  - src[4]*src[11]*src[13] - src[8]*src[5]*src[15] + src[8]*src[7]*src[13] + src[12]*src[5]*src[11] - src[12]*src[7]*src[9];
        inv[12] = -src[4]*src[9]*src[14]  + src[4]*src[10]*src[13] + src[8]*src[5]*src[14] - src[8]*src[6]*src[13] - src[12]*src[5]*src[10] + src[12]*src[6]*src[9];
        float det = src[0]*inv[0] + src[1]*inv[4] + src[2]*inv[8] + src[3]*inv[12];
        if (det == 0) return this;
        det = 1f / det;
        inv[1]  = -src[1]*src[10]*src[15] + src[1]*src[11]*src[14] + src[9]*src[2]*src[15] - src[9]*src[3]*src[14] - src[13]*src[2]*src[11] + src[13]*src[3]*src[10];
        inv[5]  =  src[0]*src[10]*src[15] - src[0]*src[11]*src[14] - src[8]*src[2]*src[15] + src[8]*src[3]*src[14] + src[12]*src[2]*src[11] - src[12]*src[3]*src[10];
        inv[9]  = -src[0]*src[9]*src[15]  + src[0]*src[11]*src[13] + src[8]*src[1]*src[15] - src[8]*src[3]*src[13] - src[12]*src[1]*src[11] + src[12]*src[3]*src[9];
        inv[13] =  src[0]*src[9]*src[14]  - src[0]*src[10]*src[13] - src[8]*src[1]*src[14] + src[8]*src[2]*src[13] + src[12]*src[1]*src[10] - src[12]*src[2]*src[9];
        inv[2]  =  src[1]*src[6]*src[15]  - src[1]*src[7]*src[14]  - src[5]*src[2]*src[15] + src[5]*src[3]*src[14] + src[13]*src[2]*src[7]  - src[13]*src[3]*src[6];
        inv[6]  = -src[0]*src[6]*src[15]  + src[0]*src[7]*src[14]  + src[4]*src[2]*src[15] - src[4]*src[3]*src[14] - src[12]*src[2]*src[7]  + src[12]*src[3]*src[6];
        inv[10] =  src[0]*src[5]*src[15]  - src[0]*src[7]*src[13]  - src[4]*src[1]*src[15] + src[4]*src[3]*src[13] + src[12]*src[1]*src[7]  - src[12]*src[3]*src[5];
        inv[14] = -src[0]*src[5]*src[14]  + src[0]*src[6]*src[13]  + src[4]*src[1]*src[14] - src[4]*src[2]*src[13] - src[12]*src[1]*src[6]  + src[12]*src[2]*src[5];
        inv[3]  = -src[1]*src[6]*src[11]  + src[1]*src[7]*src[10]  + src[5]*src[2]*src[11] - src[5]*src[3]*src[10] - src[9]*src[2]*src[7]   + src[9]*src[3]*src[6];
        inv[7]  =  src[0]*src[6]*src[11]  - src[0]*src[7]*src[10]  - src[4]*src[2]*src[11] + src[4]*src[3]*src[10] + src[8]*src[2]*src[7]   - src[8]*src[3]*src[6];
        inv[11] = -src[0]*src[5]*src[11]  + src[0]*src[7]*src[9]   + src[4]*src[1]*src[11] - src[4]*src[3]*src[9]  - src[8]*src[1]*src[7]   + src[8]*src[3]*src[5];
        inv[15] =  src[0]*src[5]*src[10]  - src[0]*src[6]*src[9]   - src[4]*src[1]*src[10] + src[4]*src[2]*src[9]  + src[8]*src[1]*src[6]   - src[8]*src[2]*src[5];
        for (int i = 0; i < 16; i++) m[i] = inv[i] * det;
        return this;
    }

    public FloatBuffer get(FloatBuffer buffer) {
        for (float v : m) buffer.put(v);
        buffer.rewind();
        return buffer;
    }

    public float[] get(float[] arr) { System.arraycopy(m, 0, arr, 0, 16); return arr; }
    public float[] getValues() { return m; }

    public float get(int row, int col) { return m[col * 4 + row]; }
    public void set(int row, int col, float v) { m[col * 4 + row] = v; }

    public Matrix4 toGdxMatrix() { return new Matrix4(m); }

    @Override
    public String toString() {
        return "[" + m[0]+","+m[4]+","+m[8]+","+m[12]+"\n"
                   + m[1]+","+m[5]+","+m[9]+","+m[13]+"\n"
                   + m[2]+","+m[6]+","+m[10]+","+m[14]+"\n"
                   + m[3]+","+m[7]+","+m[11]+","+m[15]+"]";
    }
}
