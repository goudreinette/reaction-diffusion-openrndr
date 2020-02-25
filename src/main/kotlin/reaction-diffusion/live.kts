@file:Suppress("UNUSED_LAMBDA_EXPRESSION")

package `reaction-diffusion`
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import kotlin.math.cos
import kotlin.math.sin


/*
|--------------------------------------------------------------------------
|
|--------------------------------------------------------------------------
*/
{ program: Program ->
    program.apply {
        val crossHatchShader = """
            #version 330
            
            in vec2 v_texCoord0;
            uniform sampler2D tex0;
            out vec4 o_color;
            
            uniform float t1;
            uniform float t2;
            uniform float t3;
            uniform float t4;
            
            // glsl-luma            
            float luma(vec3 color) {
              return dot(color, vec3(0.299, 0.587, 0.114));
            }
            
            float luma(vec4 color) {
              return dot(color.rgb, vec3(0.299, 0.587, 0.114));
            }
            
            // glsl-crosshatch
            vec3 crosshatch(vec3 texColor, float t1, float t2, float t3, float t4) {
              float lum = luma(texColor);
              vec3 color = vec3(1.0);
              if (lum < t1) {
                  if (mod(gl_FragCoord.x + gl_FragCoord.y, 10.0) == 0.0) {
                      color = vec3(0.0);
                  }
              }
              if (lum < t2) {
                  if (mod(gl_FragCoord.x - gl_FragCoord.y, 10.0) == 0.0) {
                      color = vec3(0.0);
                  }
              }
              if (lum < t3) {
                  if (mod(gl_FragCoord.x + gl_FragCoord.y - 5.0, 10.0) == 0.0) {
                      color = vec3(0.0);
                  }
              }
              if (lum < t4) {
                  if (mod(gl_FragCoord.x - gl_FragCoord.y - 5.0, 10.0) == 0.0) {
                      color = vec3(0.0);
                  }
              }
              return color;
            }

            void main() {
                vec4 color = texture(tex0, v_texCoord0);
                o_color.rgb = crosshatch(color.rgb, t1, t2, t3, t4);
                o_color.a = color.a;
            }
        """

        val offscreen = renderTarget(width, height) {
            colorBuffer()
        }

        class CrossHatch: Filter(filterShaderFromCode(crossHatchShader)) {
            var t1: Double by parameters
            var t2: Double by parameters
            var t3: Double by parameters
            var t4: Double by parameters

            init {
                t1 = 1.0
                t2 = 0.75
                t3 = 0.5
                t4 = 0.3
            }
        }

        val crosshatch = CrossHatch()
        val image = loadImage("data/images/cheetah.png")

        extend {
            drawer.isolatedWithTarget(offscreen) {
                drawer.background(ColorRGBa.BLACK)
                image(image, 0.0, 0.0, width.toDouble())
                drawer.fill = ColorRGBa.PINK
                drawer.circle(cos(seconds) * width / 2.0 + width / 2.0, sin(0.5 * seconds) * height / 2.0 + height / 2.0, 140.0)
            }

            crosshatch.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            drawer.image(offscreen.colorBuffer(0))
        }
    }
}