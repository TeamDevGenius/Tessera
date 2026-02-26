package org.lwjgl.stb;

import com.badlogic.gdx.graphics.Pixmap;
import java.nio.*;

/** Stub for LWJGL STBImage. Uses LibGDX Pixmap for decoding. */
public class STBImage {
    public static ByteBuffer stbi_load_from_memory(ByteBuffer buffer, IntBuffer x, IntBuffer y, IntBuffer comp, int req_comp) {
        try {
            byte[] bytes = new byte[buffer.remaining()];
            int pos = buffer.position();
            buffer.get(bytes);
            buffer.position(pos);
            Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
            int w = pixmap.getWidth();
            int h = pixmap.getHeight();
            if (x != null) { x.clear(); x.put(0, w); }
            if (y != null) { y.clear(); y.put(0, h); }
            if (comp != null) { comp.clear(); comp.put(0, 4); }
            ByteBuffer result = pixmap.getPixels();
            ByteBuffer copy = ByteBuffer.allocateDirect(result.remaining()).order(ByteOrder.nativeOrder());
            copy.put(result);
            copy.flip();
            pixmap.dispose();
            return copy;
        } catch (Exception e) {
            return ByteBuffer.allocateDirect(0);
        }
    }
    public static ByteBuffer stbi_load(CharSequence filename, IntBuffer x, IntBuffer y, IntBuffer comp, int req_comp) {
        return ByteBuffer.allocateDirect(0);
    }
    public static void stbi_image_free(ByteBuffer image) {}
    public static String stbi_failure_reason() { return ""; }
    public static boolean stbi_is_hdr_from_memory(ByteBuffer buffer) { return false; }
    public static void stbi_set_flip_vertically_on_load(boolean flag) {}
}
