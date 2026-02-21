package org.joml;

public class Vector3d {
    public double x, y, z;

    public Vector3d() {}
    public Vector3d(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public Vector3d(Vector3d v) { this.x = v.x; this.y = v.y; this.z = v.z; }
    public Vector3d(Vector3f v) { this.x = v.x; this.y = v.y; this.z = v.z; }

    public Vector3d set(double x, double y, double z) { this.x = x; this.y = y; this.z = z; return this; }
    public Vector3d set(Vector3d v) { this.x = v.x; this.y = v.y; this.z = v.z; return this; }
    public Vector3d add(double x, double y, double z) { this.x += x; this.y += y; this.z += z; return this; }
    public Vector3d add(Vector3d v) { this.x += v.x; this.y += v.y; this.z += v.z; return this; }
    public Vector3d sub(Vector3d v) { this.x -= v.x; this.y -= v.y; this.z -= v.z; return this; }
    public Vector3d sub(double x, double y, double z) { this.x -= x; this.y -= y; this.z -= z; return this; }
    public Vector3d mul(double s) { x *= s; y *= s; z *= s; return this; }
    public double length() { return Math.sqrt(x * x + y * y + z * z); }
    public double lengthSquared() { return x * x + y * y + z * z; }
    public Vector3d normalize() { double l = length(); if (l != 0) { x /= l; y /= l; z /= l; } return this; }
    public double dot(Vector3d v) { return x * v.x + y * v.y + z * v.z; }
    public Vector3f toVector3f() { return new Vector3f((float) x, (float) y, (float) z); }

    @Override
    public String toString() { return "(" + x + ", " + y + ", " + z + ")"; }
}
