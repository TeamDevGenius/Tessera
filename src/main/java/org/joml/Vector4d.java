package org.joml;

public class Vector4d {
    public double x, y, z, w;

    public Vector4d() {}
    public Vector4d(double x, double y, double z, double w) { this.x = x; this.y = y; this.z = z; this.w = w; }
    public Vector4d(Vector4d v) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = v.w; }

    public Vector4d set(double x, double y, double z, double w) { this.x = x; this.y = y; this.z = z; this.w = w; return this; }
    public Vector4d add(Vector4d v) { x += v.x; y += v.y; z += v.z; w += v.w; return this; }
    public Vector4d mul(double s) { x *= s; y *= s; z *= s; w *= s; return this; }
    public double length() { return Math.sqrt(x*x + y*y + z*z + w*w); }

    @Override public String toString() { return "(" + x + ", " + y + ", " + z + ", " + w + ")"; }
}
