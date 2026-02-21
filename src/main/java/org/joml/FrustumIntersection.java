package org.joml;

/**
 * Frustum intersection test stub. JOML's FrustumIntersection for view-frustum culling.
 */
public class FrustumIntersection {
    public static final int INSIDE  = -1;
    public static final int INTERSECT = 0;
    public static final int OUTSIDE  = 1;

    public FrustumIntersection() {}
    public FrustumIntersection(Matrix4f m) { set(m); }

    public FrustumIntersection set(Matrix4f m) { return this; }

    public boolean testAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) { return true; }
    public boolean testAab(Vector3f min, Vector3f max) { return true; }
    public boolean testSphere(float x, float y, float z, float r) { return true; }
    public boolean testSphere(Vector3f center, float r) { return true; }
    public boolean testPoint(float x, float y, float z) { return true; }
    public int intersectAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) { return INSIDE; }
    public int intersectAab(Vector3f min, Vector3f max) { return INSIDE; }
}
