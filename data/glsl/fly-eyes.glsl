#version 330

// -- part of the filter interface, every filter has these
in vec2 v_texCoord0;
uniform sampler2D tex0;
out vec4 o_color;


void main() {
    o_color = texture(tex0, v_texCoord0) - vec4(.5);
}