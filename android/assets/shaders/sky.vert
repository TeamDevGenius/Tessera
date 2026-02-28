#version 300 es
precision mediump float;

in vec3 a_position;
uniform mat4 u_MVP;
out vec3 v_dir;

void main() {
    v_dir = a_position;
    gl_Position = u_MVP * vec4(a_position, 1.0);
}
