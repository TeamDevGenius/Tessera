package org.joml;

public class Vector2i {
    public int x, y;

    public Vector2i() {}
    public Vector2i(int x, int y) { this.x = x; this.y = y; }
    public Vector2i(Vector2i v) { this.x = v.x; this.y = v.y; }

    public Vector2i set(int x, int y) { this.x = x; this.y = y; return this; }
    public Vector2i set(Vector2i v) { this.x = v.x; this.y = v.y; return this; }
    public Vector2i add(int x, int y) { this.x += x; this.y += y; return this; }
    public Vector2i sub(Vector2i v) { this.x -= v.x; this.y -= v.y; return this; }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2i)) return false;
        Vector2i v = (Vector2i) o;
        return x == v.x && y == v.y;
    }

    @Override
    public int hashCode() { return 31 * x + y; }
}
