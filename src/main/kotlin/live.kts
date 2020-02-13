@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.fx.blur.BoxBlur
import org.openrndr.extra.fx.blur.GaussianBlur
import kotlin.math.cos
import kotlin.math.sin
import org.openrndr.draw.Filter
import org.openrndr.draw.filterShaderFromCode

/*
|--------------------------------------------------------------------------
|
|--------------------------------------------------------------------------
*/
{ program: Program ->
    program.apply {
        val sharpenShader = """
            #version 330
            // -- part of the filter interface, every filter has these
            in vec2 v_texCoord0;
            uniform sampler2D tex0;
            out vec4 o_color;
    
            // -- user parameters
            uniform float strength;
    
            
            vec3 texSample(const int x, const int y, vec2 resolution) {
                vec2 uv = v_texCoord0.xy;
                uv = (uv + vec2(x, y)) / resolution;
                return texture(tex0, uv).xyz;       
            } 
            
                                                     
            vec3 sharpenFilter(vec2 resolution){
                vec3 f =
                texSample(-1,-1, resolution) *  -1. +                     
                texSample( 0,-1, resolution) *  -1. +                    
                texSample( 1,-1, resolution) *  -1. +                      
                texSample(-1, 0, resolution) *  -1. +                    
                texSample( 0, 0, resolution) *   9. +                     
                texSample( 1, 0, resolution) *  -1. +                      
                texSample(-1, 1, resolution) *  -1. +                     
                texSample( 0, 1, resolution) *  -1. +                     
                texSample( 1, 1, resolution) *  -1.;                                              
                return mix(texture(tex0, v_texCoord0).xyz, f, .8);    
            }
    
            void main() {
                vec2 resolution = textureSize(tex0, 0).xy;
                o_color = vec4(sharpenFilter(resolution), 1.0);
            }
        """


        class Sharpen : Filter(filterShaderFromCode(sharpenShader)) {
            var strength: Double by parameters

            init {
                strength = 1.0
            }
        }


        val image = loadImage("data/images/cheetah.png")

        var lastFrame = colorBuffer(width, height)
        val offscreen = renderTarget(width, height) {
            colorBuffer()
        }

        val blur = BoxBlur()
        val noise = Noise()
        val sharpen = Sharpen()


        extend {
            // -- draw to offscreen buffer
            drawer.isolatedWithTarget(offscreen) {
                background(ColorRGBa.BLACK)
                fill = ColorRGBa.PINK
                stroke = null
                circle(cos(seconds) * 100.0 + width / 2, sin(seconds) * 100.0 + height / 2.0, 100.0 + 100.0 * cos(seconds * 2.0))
                image(image, 0.0, 0.0, width.toDouble(), height.toDouble())
            }


            blur.window = 5
            blur.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            sharpen.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            drawer.image(offscreen.colorBuffer(0))

            lastFrame = offscreen.colorBuffer(0)
        }
    }
}