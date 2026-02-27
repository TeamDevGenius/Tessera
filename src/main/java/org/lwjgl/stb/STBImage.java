package org.lwjgl.stb;

import com.badlogic.gdx.graphics.Pixmap;

import java.nio.*;

/** Stub for LWJGL STBImage — delegates to LibGDX Pixmap. */
public class STBImage {
    private static String lastFailure = "";

    public static ByteBuffer stbi_load_from_memory(ByteBuffer buffer, IntBuffer x, IntBuffer y, IntBuffer comp, int req_comp) {
        try {
            byte[] data = new byte[buffer.remaining()];
            int pos = buffer.position();
            buffer.get(data);
            buffer.position(pos);

            Pixmap pixmap = new Pixmap(data, 0, data.length);
            int w = pixmap.getWidth();
            int h = pixmap.getHeight();

            if (x != null) x.put(0, w);
            if (y != null) y.put(0, h);
            if (comp != null) comp.put(0, 4);

            ByteBuffer result = ByteBuffer.allocateDirect(w * h * 4).order(ByteOrder.nativeOrder());
            if (pixmap.getFormat() == Pixmap.Format.RGBA8888) {
                ByteBuffer pixels = pixmap.getPixels();
                pixels.rewind();
                result.put(pixels);
            } else {
                Pixmap converted = new Pixmap(w, h, Pixmap.Format.RGBA8888);
                converted.drawPixmap(pixmap, 0, 0);
                ByteBuffer convPixels = converted.getPixels();
                convPixels.rewind();
                result.put(convPixels);
                converted.dispose();
            }
            result.flip();
            pixmap.dispose();
            return result;
        } catch (Exception e) {
            lastFailure = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
            return null;
        }
    }

    public static ByteBuffer stbi_load(CharSequence filename, IntBuffer x, IntBuffer y, IntBuffer comp, int req_comp) {
        return null;
    }

    public static void stbi_image_free(ByteBuffer image) {}

    public static String stbi_failure_reason() { return lastFailure; }

    public static boolean stbi_is_hdr_from_memory(ByteBuffer buffer) { return false; }

    public static void stbi_set_flip_vertically_on_load(boolean flag) {}
}
