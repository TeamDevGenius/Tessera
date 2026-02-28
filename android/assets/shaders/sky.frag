#version 300 es
precision mediump float;

in vec3 v_dir;
uniform vec4 u_skyTop;
uniform vec4 u_skyBottom;
out vec4 fragColor;

void main() {
    float t = clamp(v_dir.y * 0.5 + 0.5, 0.0, 1.0);
    fragColor = mix(u_skyBottom, u_skyTop, t);
}
