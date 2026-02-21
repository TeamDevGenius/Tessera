package org.joml;

import java.nio.FloatBuffer;

public class Vector4f {
    public float x, y, z, w;

    public Vector4f() {}
    public Vector4f(float scalar) { this.x = scalar; this.y = scalar; this.z = scalar; this.w = scalar; }
    public Vector4f(int scalar) { this.x = scalar; this.y = scalar; this.z = scalar; this.w = scalar; }
    public Vector4f(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; }
    public Vector4f(Vector4f v) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = v.w; }
    public Vector4f(Vector3f v, float w) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = w; }

    public Vector4f set(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; return this; }
    public Vector4f set(Vector4f v) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = v.w; return this; }
    public Vector4f add(Vector4f v) { x += v.x; y += v.y; z += v.z; w += v.w; return this; }
    public Vector4f sub(Vector4f v) { x -= v.x; y -= v.y; z -= v.z; w -= v.w; return this; }
    public Vector4f mul(float s) { x *= s; y *= s; z *= s; w *= s; return this; }
    public float length() { return (float) Math.sqrt(x * x + y * y + z * z + w * w); }
    public Vector4f normalize() { float l = length(); if (l != 0) { x /= l; y /= l; z /= l; w /= l; } return this; }
    public float dot(Vector4f v) { return x * v.x + y * v.y + z * v.z + w * v.w; }
    public float get(int index) {
        switch (index) { case 0: return x; case 1: return y; case 2: return z; case 3: return w; default: throw new IndexOutOfBoundsException("index=" + index); }
    }
    public FloatBuffer get(FloatBuffer buffer) { buffer.put(x).put(y).put(z).put(w); return buffer; }

    @Override
    public String toString() { return "(" + x + ", " + y + ", " + z + ", " + w + ")"; }
}
