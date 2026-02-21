package org.lwjgl.glfw;

import java.nio.*;

/** Stub for LWJGL GLFW. */
public class GLFW {
    // Window hints
    public static final int GLFW_VISIBLE    = 0x00020004;
    public static final int GLFW_RESIZABLE  = 0x00020003;
    public static final int GLFW_FOCUSED    = 0x00020001;
    public static final int GLFW_FLOATING   = 0x00020007;
    public static final int GLFW_MAXIMIZED  = 0x00020008;
    public static final int GLFW_DECORATED  = 0x00020005;
    public static final int GLFW_AUTO_ICONIFY = 0x00020006;

    // Context
    public static final int GLFW_CONTEXT_VERSION_MAJOR = 0x00022002;
    public static final int GLFW_CONTEXT_VERSION_MINOR = 0x00022003;
    public static final int GLFW_OPENGL_PROFILE        = 0x00022008;
    public static final int GLFW_OPENGL_CORE_PROFILE   = 0x00032001;
    public static final int GLFW_OPENGL_COMPAT_PROFILE = 0x00032002;
    public static final int GLFW_OPENGL_FORWARD_COMPAT = 0x00022006;
    public static final int GLFW_OPENGL_DEBUG_CONTEXT  = 0x00022007;
    public static final int GLFW_CLIENT_API            = 0x00022001;
    public static final int GLFW_NO_API                = 0;

    // Boolean values
    public static final int GLFW_TRUE  = 1;
    public static final int GLFW_FALSE = 0;

    // Actions
    public static final int GLFW_RELEASE = 0;
    public static final int GLFW_PRESS   = 1;
    public static final int GLFW_REPEAT  = 2;

    // Mouse buttons
    public static final int GLFW_MOUSE_BUTTON_1      = 0;
    public static final int GLFW_MOUSE_BUTTON_2      = 1;
    public static final int GLFW_MOUSE_BUTTON_3      = 2;
    public static final int GLFW_MOUSE_BUTTON_LEFT   = GLFW_MOUSE_BUTTON_1;
    public static final int GLFW_MOUSE_BUTTON_RIGHT  = GLFW_MOUSE_BUTTON_2;
    public static final int GLFW_MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_3;

    // Cursor modes
    public static final int GLFW_CURSOR         = 0x00033001;
    public static final int GLFW_CURSOR_NORMAL  = 0x00034001;
    public static final int GLFW_CURSOR_HIDDEN  = 0x00034002;
    public static final int GLFW_CURSOR_DISABLED= 0x00034003;

    // Mods
    public static final int GLFW_MOD_SHIFT    = 0x0001;
    public static final int GLFW_MOD_CONTROL  = 0x0002;
    public static final int GLFW_MOD_ALT      = 0x0004;
    public static final int GLFW_MOD_SUPER    = 0x0008;

    // Keys
    public static final int GLFW_KEY_UNKNOWN = -1;
    public static final int GLFW_KEY_SPACE   = 32;
    public static final int GLFW_KEY_APOSTROPHE = 39;
    public static final int GLFW_KEY_COMMA   = 44;
    public static final int GLFW_KEY_MINUS   = 45;
    public static final int GLFW_KEY_PERIOD  = 46;
    public static final int GLFW_KEY_SLASH   = 47;
    public static final int GLFW_KEY_0 = 48;
    public static final int GLFW_KEY_1 = 49;
    public static final int GLFW_KEY_2 = 50;
    public static final int GLFW_KEY_3 = 51;
    public static final int GLFW_KEY_4 = 52;
    public static final int GLFW_KEY_5 = 53;
    public static final int GLFW_KEY_6 = 54;
    public static final int GLFW_KEY_7 = 55;
    public static final int GLFW_KEY_8 = 56;
    public static final int GLFW_KEY_9 = 57;
    public static final int GLFW_KEY_SEMICOLON = 59;
    public static final int GLFW_KEY_EQUAL = 61;
    public static final int GLFW_KEY_A = 65;
    public static final int GLFW_KEY_B = 66;
    public static final int GLFW_KEY_C = 67;
    public static final int GLFW_KEY_D = 68;
    public static final int GLFW_KEY_E = 69;
    public static final int GLFW_KEY_F = 70;
    public static final int GLFW_KEY_G = 71;
    public static final int GLFW_KEY_H = 72;
    public static final int GLFW_KEY_I = 73;
    public static final int GLFW_KEY_J = 74;
    public static final int GLFW_KEY_K = 75;
    public static final int GLFW_KEY_L = 76;
    public static final int GLFW_KEY_M = 77;
    public static final int GLFW_KEY_N = 78;
    public static final int GLFW_KEY_O = 79;
    public static final int GLFW_KEY_P = 80;
    public static final int GLFW_KEY_Q = 81;
    public static final int GLFW_KEY_R = 82;
    public static final int GLFW_KEY_S = 83;
    public static final int GLFW_KEY_T = 84;
    public static final int GLFW_KEY_U = 85;
    public static final int GLFW_KEY_V = 86;
    public static final int GLFW_KEY_W = 87;
    public static final int GLFW_KEY_X = 88;
    public static final int GLFW_KEY_Y = 89;
    public static final int GLFW_KEY_Z = 90;
    public static final int GLFW_KEY_LEFT_BRACKET  = 91;
    public static final int GLFW_KEY_BACKSLASH      = 92;
    public static final int GLFW_KEY_RIGHT_BRACKET = 93;
    public static final int GLFW_KEY_GRAVE_ACCENT   = 96;
    public static final int GLFW_KEY_ESCAPE    = 256;
    public static final int GLFW_KEY_ENTER     = 257;
    public static final int GLFW_KEY_TAB       = 258;
    public static final int GLFW_KEY_BACKSPACE = 259;
    public static final int GLFW_KEY_INSERT    = 260;
    public static final int GLFW_KEY_DELETE    = 261;
    public static final int GLFW_KEY_RIGHT     = 262;
    public static final int GLFW_KEY_LEFT      = 263;
    public static final int GLFW_KEY_DOWN      = 264;
    public static final int GLFW_KEY_UP        = 265;
    public static final int GLFW_KEY_PAGE_UP   = 266;
    public static final int GLFW_KEY_PAGE_DOWN = 267;
    public static final int GLFW_KEY_HOME      = 268;
    public static final int GLFW_KEY_END       = 269;
    public static final int GLFW_KEY_F1  = 290;
    public static final int GLFW_KEY_F2  = 291;
    public static final int GLFW_KEY_F3  = 292;
    public static final int GLFW_KEY_F4  = 293;
    public static final int GLFW_KEY_F5  = 294;
    public static final int GLFW_KEY_F6  = 295;
    public static final int GLFW_KEY_F7  = 296;
    public static final int GLFW_KEY_F8  = 297;
    public static final int GLFW_KEY_F9  = 298;
    public static final int GLFW_KEY_F10 = 299;
    public static final int GLFW_KEY_F11 = 300;
    public static final int GLFW_KEY_F12 = 301;
    public static final int GLFW_KEY_LEFT_SHIFT    = 340;
    public static final int GLFW_KEY_LEFT_CONTROL  = 341;
    public static final int GLFW_KEY_LEFT_ALT      = 342;
    public static final int GLFW_KEY_LEFT_SUPER    = 343;
    public static final int GLFW_KEY_RIGHT_SHIFT   = 344;
    public static final int GLFW_KEY_RIGHT_CONTROL = 345;
    public static final int GLFW_KEY_RIGHT_ALT     = 346;
    public static final int GLFW_KEY_RIGHT_SUPER   = 347;
    public static final int GLFW_KEY_MENU          = 348;
    public static final int GLFW_KEY_LAST          = GLFW_KEY_MENU;

    // Method stubs
    public static boolean glfwInit() { return true; }
    public static void glfwTerminate() {}
    public static void glfwDefaultWindowHints() {}
    public static void glfwWindowHint(int hint, int value) {}
    public static long glfwCreateWindow(int width, int height, CharSequence title, long monitor, long share) { return 1L; }
    public static void glfwDestroyWindow(long window) {}
    public static boolean glfwWindowShouldClose(long window) { return false; }
    public static void glfwSetWindowShouldClose(long window, boolean value) {}
    public static void glfwMakeContextCurrent(long window) {}
    public static void glfwSwapBuffers(long window) {}
    public static void glfwPollEvents() {}
    public static void glfwWaitEvents() {}
    public static void glfwSwapInterval(int interval) {}
    public static void glfwShowWindow(long window) {}
    public static void glfwHideWindow(long window) {}
    public static void glfwSetWindowTitle(long window, CharSequence title) {}
    public static void glfwSetWindowPos(long window, int xpos, int ypos) {}
    public static void glfwGetWindowPos(long window, IntBuffer xpos, IntBuffer ypos) {}
    public static void glfwGetWindowSize(long window, IntBuffer width, IntBuffer height) {}
    public static void glfwSetWindowSize(long window, int width, int height) {}
    public static void glfwGetFramebufferSize(long window, IntBuffer width, IntBuffer height) {}
    public static long glfwGetPrimaryMonitor() { return 1L; }
    public static long glfwGetWindowMonitor(long window) { return 0L; }
    public static void glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {}
    public static GLFWVidMode glfwGetVideoMode(long monitor) { return new GLFWVidMode(1920, 1080, 8, 8, 8, 60); }
    public static int glfwGetKey(long window, int key) { return GLFW_RELEASE; }
    public static int glfwGetMouseButton(long window, int button) { return GLFW_RELEASE; }
    public static void glfwGetCursorPos(long window, DoubleBuffer xpos, DoubleBuffer ypos) {}
    public static void glfwSetCursorPos(long window, double xpos, double ypos) {}
    public static void glfwSetInputMode(long window, int mode, int value) {}
    public static int glfwGetWindowAttrib(long window, int attrib) { return 0; }
    public static void glfwSetWindowIcon(long window, GLFWImage.Buffer images) {}
    public static String glfwGetClipboardString(long window) { return ""; }
    public static void glfwSetClipboardString(long window, CharSequence string) {}
    public static long nglfwGetClipboardString(long window) { return 0L; }
    public static double glfwGetTime() { return System.nanoTime() / 1e9; }
    public static void glfwSetTime(double time) {}

    public static long glfwGetCurrentContext() { return 1L; }
    public static void glfwIconifyWindow(long window) {}
    public static void glfwRestoreWindow(long window) {}
    public static void glfwMaximizeWindow(long window) {}
    public static void glfwFocusWindow(long window) {}

    // Callback setters
    public static void glfwSetFramebufferSizeCallback(long window, GLFWFramebufferSizeCallback cb) {}
    public static void glfwSetKeyCallback(long window, GLFWKeyCallback cb) {}
    public static void glfwSetMouseButtonCallback(long window, GLFWMouseButtonCallback cb) {}
    public static void glfwSetCursorPosCallback(long window, GLFWCursorPosCallback cb) {}
    public static void glfwSetScrollCallback(long window, GLFWScrollCallback cb) {}
    public static void glfwSetCharCallback(long window, GLFWCharCallback cb) {}
    public static void glfwSetWindowFocusCallback(long window, GLFWWindowFocusCallback cb) {}
    public static GLFWErrorCallback glfwSetErrorCallback(GLFWErrorCallback cb) { return null; }

    // Interfaces for callbacks
    public interface GLFWKeyCallback { void invoke(long window, int key, int scancode, int action, int mods); }
    public interface GLFWMouseButtonCallback { void invoke(long window, int button, int action, int mods); }
    public interface GLFWCursorPosCallback { void invoke(long window, double xpos, double ypos); }
    public interface GLFWScrollCallback { void invoke(long window, double xoffset, double yoffset); }
    public interface GLFWCharCallback { void invoke(long window, int codepoint); }
}
