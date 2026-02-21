package org.joml;

import java.nio.FloatBuffer;

/**
 * JOML-compatible Vector3f backed by plain Java.
 * Provides JOML API without the LWJGL JOML dependency.
 */
public class Vector3f {
    public float x, y, z;

    public Vector3f() {}
    public Vector3f(float scalar) { this.x = scalar; this.y = scalar; this.z = scalar; }
    public Vector3f(float x, float y, float z) { this.x = x; this.y = y; this.z = z; }
    public Vector3f(Vector3f v) { this.x = v.x; this.y = v.y; this.z = v.z; }
    public Vector3f(Vector3i v) { this.x = v.x; this.y = v.y; this.z = v.z; }
    /** Reads 3 floats from the buffer at its current position. */
    public Vector3f(FloatBuffer buf) {
        if (buf != null && buf.remaining() >= 3) { this.x = buf.get(); this.y = buf.get(); this.z = buf.get(); }
    }

    public Vector3f set(float scalar) { this.x = scalar; this.y = scalar; this.z = scalar; return this; }
    public Vector3f set(int scalar) { return set((float)scalar); }
    public Vector3f set(float x, float y, float z) { this.x = x; this.y = y; this.z = z; return this; }
    public Vector3f set(double x, double y, double z) { this.x = (float)x; this.y = (float)y; this.z = (float)z; return this; }
    public Vector3f set(Vector3f v) { this.x = v.x; this.y = v.y; this.z = v.z; return this; }
    public Vector3f set(Vector3i v) { this.x = v.x; this.y = v.y; this.z = v.z; return this; }
    public Vector3f add(float x, float y, float z) { this.x += x; this.y += y; this.z += z; return this; }
    public Vector3f add(Vector3f v) { this.x += v.x; this.y += v.y; this.z += v.z; return this; }
    public Vector3f add(Vector3i v) { this.x += v.x; this.y += v.y; this.z += v.z; return this; }
    public Vector3f add(Vector3f v, Vector3f dest) { dest.x = x + v.x; dest.y = y + v.y; dest.z = z + v.z; return dest; }
    public Vector3f sub(Vector3f v) { this.x -= v.x; this.y -= v.y; this.z -= v.z; return this; }
    public Vector3f sub(Vector3i v) { this.x -= v.x; this.y -= v.y; this.z -= v.z; return this; }
    public Vector3f sub(float x, float y, float z) { this.x -= x; this.y -= y; this.z -= z; return this; }
    public Vector3f sub(Vector3f v, Vector3f dest) { dest.x = x - v.x; dest.y = y - v.y; dest.z = z - v.z; return dest; }
    public Vector3f mul(float scalar) { this.x *= scalar; this.y *= scalar; this.z *= scalar; return this; }
    public Vector3f mul(int scalar) { return mul((float)scalar); }
    public Vector3f mul(double scalar) { return mul((float)scalar); }
    public Vector3f mul(float scalar, Vector3f dest) { dest.x = x * scalar; dest.y = y * scalar; dest.z = z * scalar; return dest; }
    public Vector3f mul(Vector3f v) { this.x *= v.x; this.y *= v.y; this.z *= v.z; return this; }
    public Vector3f negate() { this.x = -x; this.y = -y; this.z = -z; return this; }
    public Vector3f negate(Vector3f dest) { dest.x = -x; dest.y = -y; dest.z = -z; return dest; }
    public float length() { return (float) Math.sqrt(x * x + y * y + z * z); }
    public float lengthSquared() { return x * x + y * y + z * z; }
    public float distance(float vx, float vy, float vz) { float dx = x-vx, dy = y-vy, dz = z-vz; return (float) Math.sqrt(dx*dx + dy*dy + dz*dz); }
    public float distance(int vx, int vy, int vz) { return distance((float)vx, (float)vy, (float)vz); }
    public float distance(double vx, double vy, double vz) { return distance((float)vx, (float)vy, (float)vz); }
    public float distance(Vector3f v) { float dx = x - v.x, dy = y - v.y, dz = z - v.z; return (float) Math.sqrt(dx*dx + dy*dy + dz*dz); }
    public Vector3f normalize() { float len = length(); if (len != 0) { x /= len; y /= len; z /= len; } return this; }
    public Vector3f normalize(Vector3f dest) { float len = length(); if (len != 0) { dest.x = x/len; dest.y = y/len; dest.z = z/len; } return dest; }
    public float dot(Vector3f v) { return x * v.x + y * v.y + z * v.z; }
    public float dot(float vx, float vy, float vz) { return x * vx + y * vy + z * vz; }
    public Vector3f cross(Vector3f v) {
        float rx = y * v.z - z * v.y; float ry = z * v.x - x * v.z; float rz = x * v.y - y * v.x;
        x = rx; y = ry; z = rz; return this;
    }
    public Vector3f cross(Vector3f v, Vector3f dest) {
        dest.x = y * v.z - z * v.y; dest.y = z * v.x - x * v.z; dest.z = x * v.y - y * v.x; return dest;
    }
    public float get(int index) {
        switch (index) { case 0: return x; case 1: return y; case 2: return z; default: throw new IndexOutOfBoundsException("index=" + index); }
    }
    public Vector3f lerp(Vector3f other, float t) {
        x += (other.x - x) * t; y += (other.y - y) * t; z += (other.z - z) * t; return this;
    }
    public Vector3f lerp(Vector3f other, float t, Vector3f dest) {
        dest.x = x + (other.x - x) * t; dest.y = y + (other.y - y) * t; dest.z = z + (other.z - z) * t; return dest;
    }
    public FloatBuffer get(FloatBuffer buffer) { buffer.put(x).put(y).put(z); return buffer; }
    public Vector3f get(Vector3f dest) { dest.x = x; dest.y = y; dest.z = z; return dest; }

    @Override
    public String toString() { return "(" + x + ", " + y + ", " + z + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3f)) return false;
        Vector3f v = (Vector3f) o;
        return Float.compare(x, v.x) == 0 && Float.compare(y, v.y) == 0 && Float.compare(z, v.z) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }
}
