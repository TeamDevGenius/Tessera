package org.lwjgl.stb;

import java.nio.*;

/** Stub for LWJGL STBImage. */
public class STBImage {
    public static ByteBuffer stbi_load_from_memory(ByteBuffer buffer, IntBuffer x, IntBuffer y, IntBuffer comp, int req_comp) {
        return ByteBuffer.allocateDirect(0);
    }
    public static ByteBuffer stbi_load(CharSequence filename, IntBuffer x, IntBuffer y, IntBuffer comp, int req_comp) {
        return ByteBuffer.allocateDirect(0);
    }
    public static void stbi_image_free(ByteBuffer image) {}
    public static String stbi_failure_reason() { return ""; }
    public static boolean stbi_is_hdr_from_memory(ByteBuffer buffer) { return false; }
    public static void stbi_set_flip_vertically_on_load(boolean flag) {}
}
