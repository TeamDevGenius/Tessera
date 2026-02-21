package org.joml;

import java.nio.FloatBuffer;

public class Vector2f {
    public float x, y;

    public Vector2f() {}
    public Vector2f(float x, float y) { this.x = x; this.y = y; }
    public Vector2f(Vector2f v) { this.x = v.x; this.y = v.y; }

    public Vector2f set(float x, float y) { this.x = x; this.y = y; return this; }
    public Vector2f set(Vector2f v) { this.x = v.x; this.y = v.y; return this; }
    public Vector2f add(float x, float y) { this.x += x; this.y += y; return this; }
    public Vector2f add(Vector2f v) { this.x += v.x; this.y += v.y; return this; }
    public Vector2f sub(Vector2f v) { this.x -= v.x; this.y -= v.y; return this; }
    public Vector2f sub(float x, float y) { this.x -= x; this.y -= y; return this; }
    public Vector2f mul(float s) { x *= s; y *= s; return this; }
    public float length() { return (float) Math.sqrt(x * x + y * y); }
    public float lengthSquared() { return x * x + y * y; }
    public Vector2f normalize() { float l = length(); if (l != 0) { x /= l; y /= l; } return this; }
    public float dot(Vector2f v) { return x * v.x + y * v.y; }
    public float get(int index) {
        switch (index) { case 0: return x; case 1: return y; default: throw new IndexOutOfBoundsException("index=" + index); }
    }
    public FloatBuffer get(FloatBuffer buffer) { buffer.put(x).put(y); return buffer; }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }
}
