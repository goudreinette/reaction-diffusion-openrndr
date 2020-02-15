import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Filter
import org.openrndr.draw.filterShaderFromCode
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.olive.Olive
import org.openrndr.shape.Circle

/**
 *  This is a template for a live program.
 *  The first you will run this program it will create a script file at src/main/kotlin/live.kts
 *  This script file can be modified while the program is running.
 *
 *  Please refer to https://guide.openrndr.org/#/10_OPENRNDR_Extras/C03_Live_coding for more
 *  instructions on using the live coding environment.
 */

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val updateShader = """
            #version 330
            // -- part of the filter interface, every filter has these
            in vec2 v_texCoord0;
            uniform sampler2D tex0;
            out vec4 o_color;
    
    
            float dX = -.5;
            float dY = 0.5;
            float feed = 0.055;
            float k = 0.62; // 0.062
            float s = 0.005;

    
            void main() {
                o_color = texture(tex0, v_texCoord0);
                
                vec2 spot = texture(tex0, v_texCoord0).xy;
                
                float x = spot.x;
                float y = spot.y;
                
                float laplaceX = 0;
                laplaceX += x*-1;
                laplaceX += texture(tex0, v_texCoord0 + vec2(s, 0.)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(-s, 0.)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(0., s)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(0., -s)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(-s, -s)).x*0.05;
                laplaceX += texture(tex0, v_texCoord0 + vec2(s, -s)).x*0.05;
                laplaceX += texture(tex0, v_texCoord0 + vec2(-s, s)).x*0.05;
                laplaceX += texture(tex0, v_texCoord0 + vec2(s, s)).x*0.05;
                
                float laplaceY = 0;
                laplaceY += y*-1;
                laplaceY += texture(tex0, v_texCoord0 + vec2(s, 0.)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(-s, 0.)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(0., s)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(0., -s)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(-s, -s)).y*0.05;
                laplaceY += texture(tex0, v_texCoord0 + vec2(s, -s)).y*0.05;
                laplaceY += texture(tex0, v_texCoord0 + vec2(-s, s)).y*0.05;
                laplaceY += texture(tex0, v_texCoord0 + vec2(s, s)).y*0.05;
                
                o_color.x = x + (dX * laplaceX - x*y*y + feed*(1-x))*1;
                o_color.y = y + (dY * laplaceY + x*y*y - (k+feed)*y)*1;
                
                o_color.x = clamp(o_color.x, 0., 1.);
                o_color.y = clamp(o_color.y, 0., 1.);
            }
        """

        val drawShader = """
            #version 330 
            
            in vec2 v_texCoord0;
            uniform sampler2D tex0;
            out vec4 o_color;

            void main() {
                vec2 spot = texture(tex0, v_texCoord0).xy;
                o_color = vec4(spot.x, spot.y, 1., 1.);
            }
        """

        class ReactionDiffusionUpdate: Filter(filterShaderFromCode(updateShader))
        class ReactionDiffusionDraw: Filter(filterShaderFromCode(drawShader))

        val update = ReactionDiffusionUpdate()
        val draw = ReactionDiffusionDraw()

        val offscreen = renderTarget(width, height) {
            colorBuffer()
        }

        drawer.isolatedWithTarget(offscreen) {
            drawer.fill = ColorRGBa.WHITE
            val circles = List(20) {
                Circle(Math.random() * width, Math.random() * height, Math.random() * 10.0 + 10.0)
            }
            drawer.circles(circles)
        }


        extend {
            update.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            draw.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            drawer.image(offscreen.colorBuffer(0))
        }
    }
}