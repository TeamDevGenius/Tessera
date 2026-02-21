package org.joml;

public class Vector2d {
    public double x, y;

    public Vector2d() {}
    public Vector2d(double x, double y) { this.x = x; this.y = y; }
    public Vector2d(Vector2d v) { this.x = v.x; this.y = v.y; }

    public Vector2d set(double x, double y) { this.x = x; this.y = y; return this; }
    public Vector2d set(Vector2d v) { this.x = v.x; this.y = v.y; return this; }
    public Vector2d add(double x, double y) { this.x += x; this.y += y; return this; }
    public Vector2d add(Vector2d v) { this.x += v.x; this.y += v.y; return this; }
    public Vector2d sub(Vector2d v) { this.x -= v.x; this.y -= v.y; return this; }
    public Vector2d mul(double s) { x *= s; y *= s; return this; }
    public double length() { return Math.sqrt(x * x + y * y); }
    public Vector2d normalize() { double l = length(); if (l != 0) { x /= l; y /= l; } return this; }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }
}
