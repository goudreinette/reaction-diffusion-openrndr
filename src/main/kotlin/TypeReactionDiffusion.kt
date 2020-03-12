import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.loadFont
import org.openrndr.draw.renderTarget
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.antialias.FXAA
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.math.clamp
import org.openrndr.math.map
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer


fun main() = application {
    configure {
        width = 1500
        height = 800
    }

    program {
        val f = loadFont("data/fonts/neue plak text bold.ttf", 320.0)

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
                        box = Rectangle(40.0, 200.0, width.toDouble(), height.toDouble())
                        leading = 20.0

                        text("Reaction")
                        newLine()
                        text("Diffusion")
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

//                post(ew) // Apply EdgesWork...
            }
        }

//        val c2 = compose {
//            draw {
//
//
//                drawer.image(rt.colorBuffer(0))
//            }
//
//            post(FXAA())
//        }

        extend(Screenshots())
//        extend(TemporalBlur())
        extend {
            drawer.isolatedWithTarget(rt) {
                c.draw(drawer)
            }

            drawer.fontMap = f

            ew.radius = clamp(map(0.0, width.toDouble(), 10.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()
            c.draw(drawer)
        }
    }
}
