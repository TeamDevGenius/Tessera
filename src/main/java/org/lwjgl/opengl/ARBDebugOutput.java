package org.lwjgl.opengl;

import java.nio.IntBuffer;

/** Stub for LWJGL ARBDebugOutput. */
public class ARBDebugOutput {
    public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB  = 0x8242;
    public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB = 0x8243;
    public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_ARB  = 0x9143;
    public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_ARB = 0x9144;
    public static final int GL_DEBUG_LOGGED_MESSAGES_ARB     = 0x9145;
    public static final int GL_DEBUG_SEVERITY_HIGH_ARB       = 0x9146;
    public static final int GL_DEBUG_SEVERITY_MEDIUM_ARB     = 0x9147;
    public static final int GL_DEBUG_SEVERITY_LOW_ARB        = 0x9148;
    public static final int GL_DEBUG_SOURCE_API_ARB          = 0x8246;
    public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 0x8247;
    public static final int GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 0x8248;
    public static final int GL_DEBUG_SOURCE_THIRD_PARTY_ARB  = 0x8249;
    public static final int GL_DEBUG_SOURCE_APPLICATION_ARB  = 0x824A;
    public static final int GL_DEBUG_SOURCE_OTHER_ARB        = 0x824B;
    public static final int GL_DEBUG_TYPE_ERROR_ARB          = 0x824C;
    public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 0x824D;
    public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB  = 0x824E;
    public static final int GL_DEBUG_TYPE_PORTABILITY_ARB    = 0x824F;
    public static final int GL_DEBUG_TYPE_PERFORMANCE_ARB    = 0x8250;
    public static final int GL_DEBUG_TYPE_OTHER_ARB          = 0x8251;

    public static void glDebugMessageControlARB(int source, int type, int severity, IntBuffer ids, boolean enabled) {}
    public static void glDebugMessageCallbackARB(Object callback, long userParam) {}
}
