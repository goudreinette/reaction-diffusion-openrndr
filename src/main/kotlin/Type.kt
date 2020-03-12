import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.renderTarget
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.edges.EdgesWork

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val f = loadFont("data/fonts/neue plak text bold.ttf", 64.0)
        val ew = EdgesWork()
        ew.radius=15

        val rt = renderTarget(width, height) {
            colorBuffer()
        }

        drawer.fontMap = f

        val c = compose {
            layer {
                draw {
                    drawer.apply {
                        background(ColorRGBa.BLACK)
                        fill = ColorRGBa.WHITE
                        text("Reaction Diffusion")
                        circle(drawer.bounds.center, 100.0)
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

        extend {
            c.draw(drawer)
        }
    }
}
