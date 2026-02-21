package org.joml;

public class Vector4i {
    public int x, y, z, w;

    public Vector4i() {}
    public Vector4i(int x, int y, int z, int w) { this.x = x; this.y = y; this.z = z; this.w = w; }
    public Vector4i(Vector4i v) { this.x = v.x; this.y = v.y; this.z = v.z; this.w = v.w; }

    public Vector4i set(int x, int y, int z, int w) { this.x = x; this.y = y; this.z = z; this.w = w; return this; }
    public Vector4i add(Vector4i v) { x += v.x; y += v.y; z += v.z; w += v.w; return this; }
    public int get(int index) {
        switch (index) { case 0: return x; case 1: return y; case 2: return z; case 3: return w; default: throw new IndexOutOfBoundsException(); }
    }

    @Override public String toString() { return "(" + x + ", " + y + ", " + z + ", " + w + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector4i)) return false;
        Vector4i v = (Vector4i) o;
        return x == v.x && y == v.y && z == v.z && w == v.w;
    }

    @Override public int hashCode() { return 31 * (31 * (31 * x + y) + z) + w; }
}
