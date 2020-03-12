package reaction_diffusion

import org.openrndr.draw.Filter
import org.openrndr.draw.filterShaderFromCode

val noiseShader = """
        #version 330
        // -- part of the filter interface, every filter has these
        in vec2 v_texCoord0;
        uniform sampler2D tex0;
        out vec4 o_color;

        // -- user parameters
        uniform float gain;
        uniform float time;

        #define HASHSCALE 443.8975
        vec2 hash22(vec2 p) {
            vec3 p3 = fract(vec3(p.xyx) * HASHSCALE);
            p3 += dot(p3, p3.yzx+19.19);
            return fract(vec2((p3.x + p3.y)*p3.z, (p3.x+p3.z)*p3.y));
        }

        void main() {
            float n = hash22(v_texCoord0+vec2(time)).x;
            // here we read from the input image and add noise
            vec4 color = texture(tex0, v_texCoord0) + vec4(vec3(n), 0.0) * gain;
            o_color = color;
        }
        """

class Noise : Filter(filterShaderFromCode(noiseShader)) {
    // -- note the 'by parameters' here, this is what wires the fields up to the uniforms
    var gain: Double by parameters
    var time: Double by parameters
    init {
        gain = 1.0
        time = 0.0
    }
}
