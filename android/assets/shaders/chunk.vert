#version 300 es
precision highp float;
precision highp int;

layout(location = 0) in ivec3 a_vertex;

uniform mat4 u_MVP;
uniform float u_fogStart;
uniform float u_fogEnd;

const float maxMult12bits = 122.0;
const float maxMult10bits = 31.0;

out vec3 v_UV;
out float v_light;
out float v_fog;

void main() {
    int i0 = a_vertex.x;
    int i1 = a_vertex.y;
    int i2 = a_vertex.z;

    // Unpack positions: subtract 1.0 because Java packing adds 1 offset
    float x = float((i0 >> 20) & 0xFFF) / maxMult12bits - 1.0;
    float y = float((i0 >> 8) & 0xFFF) / maxMult12bits - 1.0;
    int zi12 = (i1 >> 20) & 0xFFF;
    float z = float(zi12) / maxMult12bits - 1.0;

    float u = float((i1 >> 10) & 0x3FF) / maxMult10bits;
    float v = float(i1 & 0x3FF) / maxMult10bits;

    int texLayer = (i2 >> 16) & 0xFFFF;

    // Light is packed as a byte: bits 4-7 = sun (0-15), bits 0-3 = torch (0-15)
    int lightByte = i2 & 0xFF;
    float sunLight = float((lightByte >> 4) & 0xF) / 15.0;
    float torchLight = float(lightByte & 0xF) / 15.0;
    v_light = max(sunLight, torchLight);

    v_UV = vec3(u, v, float(texLayer));

    vec4 worldPos = vec4(x, y, z, 1.0);
    gl_Position = u_MVP * worldPos;

    float dist = length(gl_Position.xyz);
    v_fog = clamp((dist - u_fogStart) / (u_fogEnd - u_fogStart), 0.0, 1.0);
}
