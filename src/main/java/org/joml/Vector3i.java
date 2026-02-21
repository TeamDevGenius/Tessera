package org.joml;

public class Vector3i {
    public int x, y, z;

    public Vector3i() {}
    public Vector3i(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
    public Vector3i(Vector3i v) { this.x = v.x; this.y = v.y; this.z = v.z; }
    /** Reads 3 ints from the buffer at its current position. */
    public Vector3i(java.nio.IntBuffer buf) {
        if (buf != null && buf.remaining() >= 3) { this.x = buf.get(); this.y = buf.get(); this.z = buf.get(); }
    }

    public Vector3i set(int x, int y, int z) { this.x = x; this.y = y; this.z = z; return this; }
    public Vector3i set(Vector3i v) { this.x = v.x; this.y = v.y; this.z = v.z; return this; }
    public Vector3i add(int x, int y, int z) { this.x += x; this.y += y; this.z += z; return this; }
    public Vector3i add(Vector3i v) { this.x += v.x; this.y += v.y; this.z += v.z; return this; }
    public Vector3i sub(Vector3i v) { this.x -= v.x; this.y -= v.y; this.z -= v.z; return this; }
    public Vector3i sub(int x, int y, int z) { this.x -= x; this.y -= y; this.z -= z; return this; }
    public Vector3i mul(int scalar) { x *= scalar; y *= scalar; z *= scalar; return this; }
    public float distance(int vx, int vy, int vz) { int dx = x-vx, dy = y-vy, dz = z-vz; return (float) Math.sqrt(dx*dx + dy*dy + dz*dz); }
    public float distance(float vx, float vy, float vz) { float dx = x-vx, dy = y-vy, dz = z-vz; return (float) Math.sqrt(dx*dx + dy*dy + dz*dz); }
    public float distance(Vector3i v) { return distance(v.x, v.y, v.z); }
    public float length() { return (float) Math.sqrt((double)x*x + (double)y*y + (double)z*z); }
    public int get(int index) {
        switch (index) { case 0: return x; case 1: return y; case 2: return z; default: throw new IndexOutOfBoundsException("index=" + index); }
    }

    @Override
    public String toString() { return "(" + x + ", " + y + ", " + z + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3i)) return false;
        Vector3i v = (Vector3i) o;
        return x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() { return 31 * (31 * x + y) + z; }
}
