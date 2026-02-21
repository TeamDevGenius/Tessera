package org.lwjgl.nuklear;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/** Stub for LWJGL Nuklear static API. All methods are no-ops for compilation. */
public class Nuklear {

    // Constants
    public static final int NK_WINDOW_BORDER       = 1;
    public static final int NK_WINDOW_MOVABLE      = 2;
    public static final int NK_WINDOW_SCALABLE     = 4;
    public static final int NK_WINDOW_CLOSABLE     = 8;
    public static final int NK_WINDOW_MINIMIZABLE  = 16;
    public static final int NK_WINDOW_NO_SCROLLBAR = 32;
    public static final int NK_WINDOW_TITLE        = 64;
    public static final int NK_WINDOW_SCROLL_AUTO_HIDE = 128;
    public static final int NK_WINDOW_BACKGROUND   = 256;
    public static final int NK_WINDOW_SCALE_LEFT   = 512;
    public static final int NK_WINDOW_NO_INPUT     = 1024;

    public static final int NK_LEFT    = 17;
    public static final int NK_TEXT_LEFT            = 17;
    public static final int NK_TEXT_CENTERED        = 18;
    public static final int NK_TEXT_RIGHT           = 20;
    public static final int NK_TEXT_ALIGN_LEFT      = 1;
    public static final int NK_TEXT_ALIGN_CENTERED  = 2;
    public static final int NK_TEXT_ALIGN_RIGHT     = 4;
    public static final int NK_TEXT_ALIGN_TOP       = 8;
    public static final int NK_TEXT_ALIGN_MIDDLE    = 16;
    public static final int NK_TEXT_ALIGN_BOTTOM    = 32;

    public static final int NK_BUTTON_LEFT   = 0;
    public static final int NK_BUTTON_MIDDLE = 1;
    public static final int NK_BUTTON_RIGHT  = 2;
    public static final int NK_BUTTON_DOUBLE = 3;

    public static final int NK_KEY_NONE            = 0;
    public static final int NK_KEY_SHIFT           = 1;
    public static final int NK_KEY_CTRL            = 2;
    public static final int NK_KEY_DEL             = 3;
    public static final int NK_KEY_ENTER           = 4;
    public static final int NK_KEY_TAB             = 5;
    public static final int NK_KEY_BACKSPACE       = 6;
    public static final int NK_KEY_COPY            = 7;
    public static final int NK_KEY_CUT             = 8;
    public static final int NK_KEY_PASTE           = 9;
    public static final int NK_KEY_UP              = 10;
    public static final int NK_KEY_DOWN            = 11;
    public static final int NK_KEY_LEFT            = 12;
    public static final int NK_KEY_RIGHT           = 13;
    public static final int NK_KEY_TEXT_INSERT_MODE    = 14;
    public static final int NK_KEY_TEXT_REPLACE_MODE   = 15;
    public static final int NK_KEY_TEXT_RESET_MODE     = 16;
    public static final int NK_KEY_TEXT_LINE_START     = 17;
    public static final int NK_KEY_TEXT_LINE_END       = 18;
    public static final int NK_KEY_TEXT_START          = 19;
    public static final int NK_KEY_TEXT_END            = 20;
    public static final int NK_KEY_TEXT_UNDO           = 21;
    public static final int NK_KEY_TEXT_REDO           = 22;
    public static final int NK_KEY_TEXT_SELECT_ALL     = 23;
    public static final int NK_KEY_TEXT_WORD_LEFT      = 24;
    public static final int NK_KEY_TEXT_WORD_RIGHT     = 25;
    public static final int NK_KEY_SCROLL_START        = 26;
    public static final int NK_KEY_SCROLL_END          = 27;
    public static final int NK_KEY_SCROLL_DOWN         = 28;
    public static final int NK_KEY_SCROLL_UP           = 29;

    public static final int NK_VERTEX_POSITION      = 0;
    public static final int NK_VERTEX_TEXCOORD      = 1;
    public static final int NK_VERTEX_COLOR         = 2;
    public static final int NK_VERTEX_ATTRIBUTE_COUNT = 3;

    public static final int NK_FORMAT_FLOAT         = 0;
    public static final int NK_FORMAT_R8G8B8A8      = 1;
    public static final int NK_FORMAT_COUNT         = 2;

    public static final int NK_ANTI_ALIASING_OFF    = 0;
    public static final int NK_ANTI_ALIASING_ON     = 1;

    public static final int NK_UTF_INVALID          = 0xFFFD;

    public static final int NK_EDIT_SIMPLE          = 1;
    public static final int NK_EDIT_FIELD           = 2;
    public static final int NK_EDIT_BOX             = 3;
    public static final int NK_EDIT_EDITOR          = 4;

    public static final int NK_EDIT_ALWAYS_INSERT_MODE   = 1;
    public static final int NK_EDIT_MULTILINE            = 2;
    public static final int NK_EDIT_READ_ONLY            = 4;
    public static final int NK_EDIT_ALLOW_TAB            = 8;
    public static final int NK_EDIT_NO_CURSOR           = 16;
    public static final int NK_EDIT_SELECTABLE          = 32;
    public static final int NK_EDIT_CLIPBOARD           = 64;
    public static final int NK_EDIT_CTRL_ENTER_NEWLINE  = 128;
    public static final int NK_EDIT_NO_HORIZONTAL_SCROLL = 256;
    public static final int NK_EDIT_ALWAYS_INSERT_MODE2 = 512;
    public static final int NK_EDIT_GOTO_END_ON_ACTIVATE = 1024;

    public static final int NK_EDIT_COMMITED = 1;
    public static final int NK_EDIT_CHANGED  = 2;
    public static final int NK_EDIT_ACTIVE   = 4;
    public static final int NK_EDIT_INACTIVE = 8;
    public static final int NK_EDIT_DEACTIVATED = 16;
    public static final int NK_EDIT_ACTIVATED   = 32;
    public static final int NK_EDIT_SIG_ENTER   = 64;

    public static final int NK_SYMBOL_NONE           = 0;
    public static final int NK_SYMBOL_X              = 1;
    public static final int NK_SYMBOL_UNDERSCORE     = 2;
    public static final int NK_SYMBOL_CIRCLE_SOLID   = 3;
    public static final int NK_SYMBOL_CIRCLE_OUTLINE = 4;
    public static final int NK_SYMBOL_RECT_SOLID     = 5;
    public static final int NK_SYMBOL_RECT_OUTLINE   = 6;
    public static final int NK_SYMBOL_TRIANGLE_UP    = 7;
    public static final int NK_SYMBOL_TRIANGLE_DOWN  = 8;
    public static final int NK_SYMBOL_TRIANGLE_LEFT  = 9;
    public static final int NK_SYMBOL_TRIANGLE_RIGHT = 10;
    public static final int NK_SYMBOL_PLUS           = 11;
    public static final int NK_SYMBOL_MINUS          = 12;

    public static final int NK_TREE_NODE   = 0;
    public static final int NK_TREE_TAB    = 1;

    public static final int NK_MAXIMIZED   = 1;
    public static final int NK_MINIMIZED   = 0;

    public static final int NK_COLOR_TEXT                  = 0;
    public static final int NK_COLOR_WINDOW                = 1;
    public static final int NK_COLOR_HEADER                = 2;
    public static final int NK_COLOR_BORDER                = 3;
    public static final int NK_COLOR_BUTTON                = 4;
    public static final int NK_COLOR_BUTTON_HOVER          = 5;
    public static final int NK_COLOR_BUTTON_ACTIVE         = 6;
    public static final int NK_COLOR_TOGGLE                = 7;
    public static final int NK_COLOR_TOGGLE_HOVER          = 8;
    public static final int NK_COLOR_TOGGLE_CURSOR         = 9;
    public static final int NK_COLOR_SELECT                = 10;
    public static final int NK_COLOR_SELECT_ACTIVE         = 11;
    public static final int NK_COLOR_SLIDER                = 12;
    public static final int NK_COLOR_SLIDER_CURSOR         = 13;
    public static final int NK_COLOR_SLIDER_CURSOR_HOVER   = 14;
    public static final int NK_COLOR_SLIDER_CURSOR_ACTIVE  = 15;
    public static final int NK_COLOR_PROPERTY              = 16;
    public static final int NK_COLOR_EDIT                  = 17;
    public static final int NK_COLOR_EDIT_CURSOR          = 18;
    public static final int NK_COLOR_COMBO                 = 19;
    public static final int NK_COLOR_CHART                 = 20;
    public static final int NK_COLOR_CHART_COLOR           = 21;
    public static final int NK_COLOR_CHART_COLOR_HIGHLIGHT = 22;
    public static final int NK_COLOR_SCROLLBAR             = 23;
    public static final int NK_COLOR_SCROLLBAR_CURSOR      = 24;
    public static final int NK_COLOR_SCROLLBAR_CURSOR_HOVER= 25;
    public static final int NK_COLOR_SCROLLBAR_CURSOR_ACTIVE=26;
    public static final int NK_COLOR_TAB_HEADER            = 27;
    public static final int NK_COLOR_COUNT                 = 28;

    // Static API stubs - all are no-ops
    public static boolean nk_begin(NkContext ctx, CharSequence title, NkRect bounds, int flags) { return false; }
    public static boolean nk_begin_titled(NkContext ctx, CharSequence name, CharSequence title, NkRect bounds, int flags) { return false; }
    public static void nk_end(NkContext ctx) {}
    public static void nk_label(NkContext ctx, CharSequence str, int align) {}
    public static void nk_label_colored(NkContext ctx, CharSequence str, int align, NkColor color) {}
    public static void nk_text(NkContext ctx, CharSequence str, int align) {}
    public static void nk_text(NkContext ctx, java.nio.ByteBuffer str, int align) {}
    public static void nk_style_from_table(NkContext ctx, NkColor.Buffer colors) {}
    public static boolean nk_button_label(NkContext ctx, CharSequence title) { return false; }
    public static boolean nk_button_text(NkContext ctx, CharSequence title) { return false; }
    public static boolean nk_button_symbol(NkContext ctx, int symbol) { return false; }
    public static boolean nk_button_image(NkContext ctx, NkImage img) { return false; }
    public static void nk_layout_row_static(NkContext ctx, float height, int item_width, int cols) {}
    public static void nk_layout_row_dynamic(NkContext ctx, float height, int cols) {}
    public static void nk_layout_row_begin(NkContext ctx, int fmt, float row_height, int cols) {}
    public static void nk_layout_row_push(NkContext ctx, float value) {}
    public static void nk_layout_row_end(NkContext ctx) {}
    public static void nk_layout_row(NkContext ctx, int fmt, float height, int cols, float[] ratio) {}
    public static NkRect nk_rect(float x, float y, float w, float h) { NkRect r = NkRect.create(); r.x(x).y(y).w(w).h(h); return r; }
    public static NkRect nk_rect(float x, float y, float w, float h, NkRect dest) { dest.x(x).y(y).w(w).h(h); return dest; }
    public static boolean nk_option_label(NkContext ctx, CharSequence label, boolean active) { return active; }
    public static boolean nk_option_text(NkContext ctx, ByteBuffer text, boolean active) { return active; }
    public static boolean nk_checkbox_label(NkContext ctx, CharSequence label, IntBuffer active) { return false; }
    public static boolean nk_checkbox_label(NkContext ctx, CharSequence label, java.nio.ByteBuffer active) { return false; }
    public static boolean nk_selectable_label(NkContext ctx, CharSequence str, int align, IntBuffer value) { return false; }
    public static boolean nk_slider_float(NkContext ctx, float min, float[] val, float max, float step) { return false; }
    public static boolean nk_slider_int(NkContext ctx, int min, IntBuffer val, int max, int step) { return false; }
    public static boolean nk_progress(NkContext ctx, long[] cur, long max, boolean is_modifyable) { return false; }
    public static boolean nk_progress(NkContext ctx, org.lwjgl.PointerBuffer cur, int max, boolean is_modifyable) { return false; }
    public static boolean nk_progress(NkContext ctx, org.lwjgl.PointerBuffer cur, long max, boolean is_modifyable) { return false; }
    public static void nk_property_int(NkContext ctx, CharSequence name, int min, IntBuffer val, int max, int step, float inc_per_pixel) {}
    public static void nk_property_float(NkContext ctx, CharSequence name, float min, float[] val, float max, float step, float inc_per_pixel) {}
    public static int nk_edit_string(NkContext ctx, int flags, ByteBuffer memory, IntBuffer len, int max, NkPluginFilterI filter) { return 0; }
    public static int nk_edit_string(NkContext ctx, int flags, ByteBuffer memory, IntBuffer len, int max, NkPluginFilter filter) { return 0; }
    public static boolean nk_combo_begin_label(NkContext ctx, CharSequence selected, NkVec2 size) { return false; }
    public static boolean nk_combo_begin_text(NkContext ctx, ByteBuffer selected, NkVec2 size) { return false; }
    public static void nk_combo_end(NkContext ctx) {}
    public static boolean nk_combo_item_label(NkContext ctx, CharSequence text, int alignment) { return false; }
    public static boolean nk_tree_push_hashed(NkContext ctx, int type, CharSequence title, int initial_state, ByteBuffer hash, int len, int seed) { return false; }
    public static boolean nk_tree_state_push(NkContext ctx, int type, CharSequence title, IntBuffer state) { return false; }
    public static void nk_tree_pop(NkContext ctx) {}
    public static void nk_input_begin(NkContext ctx) {}
    public static void nk_input_end(NkContext ctx) {}
    public static void nk_input_key(NkContext ctx, int key, boolean down) {}
    public static void nk_input_button(NkContext ctx, int button, int x, int y, boolean down) {}
    public static void nk_input_motion(NkContext ctx, int x, int y) {}
    public static void nk_input_scroll(NkContext ctx, NkVec2 val) {}
    public static void nk_input_unicode(NkContext ctx, int unicode) {}
    public static void nk_input_char(NkContext ctx, byte c) {}
    public static boolean nk_init(NkContext ctx, NkAllocator allocator, NkUserFont font) { return true; }
    public static void nk_free(NkContext ctx) {}
    public static void nk_clear(NkContext ctx) {}
    public static void nk_convert(NkContext ctx, NkBuffer cmds, NkBuffer vertices, NkBuffer elements, NkConvertConfig config) {}
    public static void nk_buffer_init(NkBuffer buf, NkAllocator allocator, long size) {}
    public static void nk_buffer_init_fixed(NkBuffer buf, java.nio.ByteBuffer memory) {}
    public static void nk_buffer_free(NkBuffer buf) {}
    public static void nk_buffer_clear(NkBuffer buf) {}
    public static NkDrawCommand nk__draw_begin(NkContext ctx, NkBuffer buf) { return null; }
    public static NkDrawCommand nk__draw_next(NkDrawCommand cmd, NkBuffer buf, NkContext ctx) { return null; }
    public static void nk_style_set_font(NkContext ctx, NkUserFont font) {}
    public static void nk_style_push_float(NkContext ctx, float[] address, float value) {}
    public static void nk_style_pop_float(NkContext ctx) {}
    public static void nk_style_push_vec2(NkContext ctx, NkVec2 address, NkVec2 value) {}
    public static void nk_style_pop_vec2(NkContext ctx) {}
    public static boolean nk_group_begin(NkContext ctx, CharSequence title, int flags) { return false; }
    public static void nk_group_end(NkContext ctx) {}
    public static void nk_image(NkContext ctx, NkImage img) {}
    public static boolean nk_image_is_subimage(NkImage img) { return false; }
    public static void nk_spacing(NkContext ctx, int cols) {}
    public static boolean nk_window_has_focus(NkContext ctx) { return true; }
    public static void nk_window_set_focus(NkContext ctx, CharSequence name) {}
    public static boolean nk_widget_is_hovered(NkContext ctx) { return false; }
    public static boolean nk_item_is_any_active(NkContext ctx) { return false; }
    public static NkRect nk_widget_bounds(NkContext ctx, NkRect out) { return out != null ? out : NkRect.create(); }
    public static NkRect nk_widget_bounds(NkContext ctx) { return NkRect.create(); }
    public static NkCommandBuffer nk_window_get_canvas(NkContext ctx) { return null; }
    public static void nk_group_set_scroll(NkContext ctx, CharSequence id, int xOffset, int yOffset) {}
    public static boolean nk_input_is_mouse_click_in_rect(NkInput input, int button, NkRect rect) { return false; }
    public static boolean nk_prog(NkContext ctx, int cur, long max, boolean modifiable) { return false; }
    public static boolean nk_prog(NkContext ctx, long cur, int max, boolean modifiable) { return false; }
    public static boolean nk_prog(NkContext ctx, long cur, long max, boolean modifiable) { return false; }
    public static boolean nk_prog(NkContext ctx, org.lwjgl.PointerBuffer cur, int max, boolean modifiable) { return false; }
    public static boolean nk_prog(NkContext ctx, org.lwjgl.PointerBuffer cur, long max, boolean modifiable) { return false; }
    public static void nk_style_push_color(NkContext ctx, NkColor address, NkColor value) {}
    public static void nk_style_pop_color(NkContext ctx) {}
    public static int nnk_strlen(long str) { return 0; }
    public static int nnk_textedit_paste(long edit, long text, int len) { return 0; }
    public static int nnk_utf_decode(long c, long u, int l) { return 0; }
    public static long nglfwGetClipboardString(long window) { return 0L; }
    // Filter functions - these are NkPluginFilterI implementations used as method references
    public static boolean nnk_filter_default(long edit, int unicode) { return true; }
    public static boolean nnk_filter_ascii(long edit, int unicode) { return unicode >= 0 && unicode < 128; }
    public static boolean nnk_filter_float(long edit, int unicode) { return (unicode >= '0' && unicode <= '9') || unicode == '.' || unicode == '-'; }
    public static boolean nnk_filter_decimal(long edit, int unicode) { return (unicode >= '0' && unicode <= '9') || unicode == '-'; }
    public static boolean nnk_filter_hex(long edit, int unicode) { return (unicode >= '0' && unicode <= '9') || (unicode >= 'a' && unicode <= 'f') || (unicode >= 'A' && unicode <= 'F'); }
    public static boolean nnk_filter_oct(long edit, int unicode) { return unicode >= '0' && unicode <= '7'; }
    public static boolean nnk_filter_binary(long edit, int unicode) { return unicode == '0' || unicode == '1'; }
    public static void nk_color_hex_rgba(ByteBuffer buf, NkColor color) {}
    public static NkColor nk_rgba(int r, int g, int b, int a) { return NkColor.create(); }
    public static NkColor nk_rgb(int r, int g, int b) { return NkColor.create(); }
    public static NkColor nk_rgba_f(float r, float g, float b, float a) { return NkColor.create(); }
    public static void nk_window_set_bounds(NkContext ctx, CharSequence name, NkRect bounds) {}
    public static NkRect nk_window_get_bounds(NkContext ctx) { return NkRect.create(); }
    public static NkVec2 nk_window_get_size(NkContext ctx) { return NkVec2.create(); }
    public static boolean nk_window_is_closed(NkContext ctx, CharSequence name) { return false; }
    public static boolean nk_window_is_hidden(NkContext ctx, CharSequence name) { return false; }
    public static boolean nk_window_is_collapsed(NkContext ctx, CharSequence name) { return false; }
    public static void nk_window_show(NkContext ctx, CharSequence name, int show) {}
    public static void nk_window_collapse(NkContext ctx, CharSequence name, int state) {}
    public static NkVec2 nk_layout_widget_bounds(NkContext ctx) { return NkVec2.create(); }
    public static boolean nk_menu_begin_label(NkContext ctx, CharSequence title, int align, NkVec2 size) { return false; }
    public static boolean nk_menu_item_label(NkContext ctx, CharSequence title, int align) { return false; }
    public static void nk_menu_end(NkContext ctx) {}
    public static void nk_menubar_begin(NkContext ctx) {}
    public static void nk_menubar_end(NkContext ctx) {}
    public static void nk_tooltip(NkContext ctx, CharSequence text) {}
    public static boolean nk_tooltip_begin(NkContext ctx, float width) { return false; }
    public static void nk_tooltip_end(NkContext ctx) {}
    public static boolean nk_contextual_begin(NkContext ctx, int flags, NkVec2 size, NkRect trigger_bounds) { return false; }
    public static boolean nk_contextual_item_label(NkContext ctx, CharSequence label, int align) { return false; }
    public static void nk_contextual_end(NkContext ctx) {}

    // Drawing commands on NkCommandBuffer
    public static void nk_fill_rect(NkCommandBuffer canvas, NkRect rect, float rounding, NkColor color) {}
    public static void nk_stroke_rect(NkCommandBuffer canvas, NkRect rect, float rounding, float lineThickness, NkColor color) {}
    public static void nk_draw_image(NkCommandBuffer canvas, NkRect rect, NkImage img, NkColor color) {}
    public static void nk_draw_text(NkCommandBuffer canvas, NkRect rect, CharSequence text, NkUserFont font, NkColor background, NkColor foreground) {}
    public static void nk_fill_circle(NkCommandBuffer canvas, NkRect rect, NkColor color) {}
    public static void nk_stroke_line(NkCommandBuffer canvas, float x0, float y0, float x1, float y1, float lineThickness, NkColor color) {}
    public static void nk_fill_triangle(NkCommandBuffer canvas, float x0, float y0, float x1, float y1, float x2, float y2, NkColor color) {}
}
