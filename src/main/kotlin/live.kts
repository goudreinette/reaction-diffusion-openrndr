@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.draw.Filter
import org.openrndr.draw.filterShaderFromCode
import org.openrndr.math.Vector2

/*
|--------------------------------------------------------------------------
|
|--------------------------------------------------------------------------
*/
{ program: Program ->
    program.apply {
        val initShader = """
            #version 330
            // -- part of the filter interface, every filter has these
            in vec2 v_texCoord0;
            uniform sampler2D tex0;
            out vec4 o_color;
            
            uniform vec2 start;
    
            void main() {
                o_color = vec4(vec3(0.), .1);
                o_color.x = 1.;

                if (distance(vec2(0.5), v_texCoord0) < .02) {
                    o_color.y = 1.;
                }
            }
        """

        val updateShader = """
            #version 330
            // -- part of the filter interface, every filter has these
            in vec2 v_texCoord0;
            uniform sampler2D tex0;
            out vec4 o_color;
    
    
            float dX = 1.0;
            float dY = 0.5;
            float feed = 0.055;
            float k = 0.062;
            float s = 0.001;

    
            void main() {
                o_color = texture(tex0, v_texCoord0);
                
                vec2 spot = texture(tex0, v_texCoord0).xy;
                
                float x = spot.x;
                float y = spot.y;
                
                float laplaceX = 0;
                laplaceX += x*-1;
                laplaceX += texture(tex0, v_texCoord0 + vec2(s, 0.)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 - vec2(s, 0.)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(0., s)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(0., s)).x*0.2;
                laplaceX += texture(tex0, v_texCoord0 + vec2(-s, -s)).x*0.05;
                laplaceX += texture(tex0, v_texCoord0 + vec2(s, -s)).x*0.05;
                laplaceX += texture(tex0, v_texCoord0 + vec2(-s, s)).x*0.05;
                laplaceX += texture(tex0, v_texCoord0 + vec2(s, s)).x*0.05;
                
                float laplaceY = 0;
                laplaceY += y*-1;
                laplaceY += texture(tex0, v_texCoord0 + vec2(s, 0.)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 - vec2(s, 0.)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(0., s)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(0., s)).y*0.2;
                laplaceY += texture(tex0, v_texCoord0 + vec2(-s, -s)).y*0.05;
                laplaceY += texture(tex0, v_texCoord0 + vec2(s, -s)).y*0.05;
                laplaceY += texture(tex0, v_texCoord0 + vec2(-s, s)).y*0.05;
                laplaceY += texture(tex0, v_texCoord0 + vec2(s, s)).y*0.05;
                
                o_color.x = x + (dX * laplaceX - x*y*y + feed * (1-x)) * 1;
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
                o_color = texture(tex0, v_texCoord0);
            }
        """

        class ReactionDiffusionInit: Filter(filterShaderFromCode(initShader)) {
            var start: Vector2 by parameters

            init {
                start = Vector2(300.0, 300.0)
            }
        }

        class ReactionDiffusionUpdate: Filter(filterShaderFromCode(updateShader))
        class ReactionDiffusionDraw: Filter(filterShaderFromCode(drawShader))

        val init = ReactionDiffusionInit()
        val update = ReactionDiffusionUpdate()
        val draw = ReactionDiffusionDraw()

        val offscreen = renderTarget(width, height) {
            colorBuffer()
        }

        init.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))


        extend {
            update.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            draw.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            drawer.image(offscreen.colorBuffer(0))
        }
    }
}