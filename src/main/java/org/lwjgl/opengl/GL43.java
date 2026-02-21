package org.lwjgl.opengl;

import java.nio.IntBuffer;

/** Stub for LWJGL GL43. */
public class GL43 extends GL33 {
    public static final int GL_DEBUG_SOURCE_API             = 0x8246;
    public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM   = 0x8247;
    public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248;
    public static final int GL_DEBUG_SOURCE_THIRD_PARTY      = 0x8249;
    public static final int GL_DEBUG_SOURCE_APPLICATION     = 0x824A;
    public static final int GL_DEBUG_SOURCE_OTHER           = 0x824B;
    public static final int GL_DEBUG_TYPE_ERROR             = 0x824C;
    public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D;
    public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR  = 0x824E;
    public static final int GL_DEBUG_TYPE_PORTABILITY        = 0x824F;
    public static final int GL_DEBUG_TYPE_PERFORMANCE        = 0x8250;
    public static final int GL_DEBUG_TYPE_OTHER             = 0x8251;
    public static final int GL_DEBUG_SEVERITY_HIGH          = 0x9146;
    public static final int GL_DEBUG_SEVERITY_MEDIUM        = 0x9147;
    public static final int GL_DEBUG_SEVERITY_LOW           = 0x9148;
    public static final int GL_DEBUG_SEVERITY_NOTIFICATION  = 0x826B;

    public interface GLDebugMessageCallback { void invoke(int source, int type, int id, int severity, int length, long message, long userParam); }

    public static void glDebugMessageControl(int source, int type, int severity, IntBuffer ids, boolean enabled) {}
    public static void glDebugMessageCallback(GLDebugMessageCallback callback, long userParam) {}
}
