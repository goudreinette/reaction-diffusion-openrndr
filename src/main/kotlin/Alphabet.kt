import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.loadFont
import org.openrndr.draw.renderTarget
import org.openrndr.draw.shadeStyle
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.BooleanParameter
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.clamp
import org.openrndr.math.map
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer


fun main() = application {
    configure {
        width = 1200
        height = 800
    }

    program {
        val upperCase = ('A'..'Z').joinToString(separator = " ")
        val lowerCase = ('a'..'z').joinToString(separator = " ")
        val numbers = ('0'..'9').joinToString(separator = " ")

        var f = loadFont("data/fonts/neue plak text bold.ttf", 320.0)
        val g = GUI()

        val s = object {
            @BooleanParameter("Uppercase")
            var showUpperCase = true

            @BooleanParameter("Numbers")
            var showNumbers = true

            @DoubleParameter("Font Size", 10.0, 320.0)
            var fontSize = 320.0

            @ActionParameter("Update font size")
            fun updateFontSize() {
                f = loadFont("data/fonts/neue plak text bold.ttf", fontSize)
            }
        }

        g.add(s)


        val ew = EdgesWork()
        ew.radius = 15


        val rt = renderTarget(width, height) {
            colorBuffer()
        }

        val c = compose {
            layer {
                draw {
                    drawer.background(ColorRGBa.BLACK)
                    drawer.fontMap = f
                    drawer.fill = ColorRGBa.WHITE

                    writer {
                        box = Rectangle(300.0, 200.0, width.toDouble() - 350.0, height.toDouble())
                        leading = 0.0

                        text(if (s.showNumbers) numbers else (if (s.showUpperCase) upperCase else lowerCase))
                    }
                }
            }

            layer {
                blend(Multiply()) {
                    clip = true
                }

                draw {
                    drawer.image(rt.colorBuffer(0))
                }

                post(ew) // Apply EdgesWork...
            }
        }

        extend(g)
        extend(Screenshots())
        extend(ScreenRecorder())
        extend {
            drawer.fontMap = f
            drawer.stroke = null
            drawer.isolatedWithTarget(rt) {
                c.draw(drawer)
            }

            ew.radius = clamp(map(0.0, width.toDouble(), 1.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()
            drawer.shadeStyle = shadeStyle {
                fragmentTransform = """
                    vec2 texCoord = vec2(c_boundsPosition.x, 1.0 - c_boundsPosition.y);
                    x_fill.rgb = vec3(1.0) - texture(p_image, texCoord).rgb;
                """.trimIndent()
                parameter("image", rt.colorBuffer(0))
            }

            drawer.rectangle(drawer.bounds)
        }
    }
}
