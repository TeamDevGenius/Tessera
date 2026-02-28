#version 300 es
precision mediump float;

in vec3 a_position;
in vec2 a_texCoord;
uniform mat4 u_MVP;
out vec2 v_texCoord;

void main() {
    v_texCoord = a_texCoord;
    gl_Position = u_MVP * vec4(a_position, 1.0);
}
