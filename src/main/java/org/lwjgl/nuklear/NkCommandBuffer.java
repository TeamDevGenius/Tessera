package org.lwjgl.nuklear;

/** Stub for LWJGL NkCommandBuffer. */
public class NkCommandBuffer {
    private NkCommandBuffer() {}
    public static NkCommandBuffer create() { return new NkCommandBuffer(); }

    public void nk_fill_rect(NkRect rect, float rounding, NkColor color) {}
    public void nk_stroke_rect(NkRect rect, float rounding, float lineThickness, NkColor color) {}
    public void nk_draw_image(NkRect rect, NkImage img, NkColor color) {}
    public void nk_fill_circle(NkRect rect, NkColor color) {}
    public void nk_stroke_circle(NkRect rect, float lineThickness, NkColor color) {}
    public void nk_stroke_line(float x0, float y0, float x1, float y1, float lineThickness, NkColor color) {}
    public void nk_fill_triangle(float x0, float y0, float x1, float y1, float x2, float y2, NkColor color) {}
    public void nk_stroke_triangle(float x0, float y0, float x1, float y1, float x2, float y2, float lineThickness, NkColor color) {}
}
