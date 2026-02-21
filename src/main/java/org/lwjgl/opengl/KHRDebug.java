package org.lwjgl.opengl;

import java.nio.IntBuffer;

/** Stub for LWJGL KHRDebug. */
public class KHRDebug {
    public static final int GL_DEBUG_SOURCE_API             = 0x8246;
    public static final int GL_DEBUG_SOURCE_OTHER           = 0x824B;
    public static final int GL_DEBUG_TYPE_ERROR             = 0x824C;
    public static final int GL_DEBUG_TYPE_OTHER             = 0x8251;
    public static final int GL_DEBUG_SEVERITY_HIGH          = 0x9146;
    public static final int GL_DEBUG_SEVERITY_MEDIUM        = 0x9147;
    public static final int GL_DEBUG_SEVERITY_LOW           = 0x9148;
    public static final int GL_DEBUG_SEVERITY_NOTIFICATION  = 0x826B;
    public static final int GL_DEBUG_OUTPUT                 = 0x92E0;
    public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS     = 0x8242;

    public static void glDebugMessageControl(int source, int type, int severity, IntBuffer ids, boolean enabled) {}
    public static void glDebugMessageCallback(Object callback, long userParam) {}
}
