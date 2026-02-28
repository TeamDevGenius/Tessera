#version 300 es
precision highp float;
precision highp int;

uniform sampler2DArray u_texture;
uniform vec4 u_fogColor;

in vec3 v_UV;
in float v_light;
in float v_fog;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(u_texture, v_UV);
    if (texColor.a < 0.1) discard;
    vec3 lit = texColor.rgb * clamp(v_light + 0.05, 0.0, 1.0);
    fragColor = mix(vec4(lit, texColor.a), u_fogColor, v_fog);
}
