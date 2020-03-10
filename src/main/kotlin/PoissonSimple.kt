import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.math.Vector2
import org.openrndr.poissonfill.PoissonFill
import kotlin.math.sin

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val c = compose {
            layer {
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = ColorRGBa.TRANSPARENT

                    drawer.hexagon(drawer.bounds.center, 250.0 + sin(seconds) * 50.0)
                    drawer.fill = ColorRGBa.BLUE

                    val w = 10.0
                    drawer.rectangle(0.0, 0.0, width.toDouble(), w)
                    drawer.rectangle(0.0, height - w, width.toDouble(), w)
                    drawer.rectangle(0.0, 0.0, w, height.toDouble())
                    drawer.rectangle(width - w, 0.0, width.toDouble(), height.toDouble())
                }

                post(PoissonFill())
            }

            layer {
                draw {
                    drawer.fill = ColorRGBa.PINK
                    drawer.stroke = ColorRGBa.WHITE
                    drawer.hexagon(drawer.bounds.center, 100.0)
                }

            }

        }


        extend {
            c.draw(drawer)
        }
    }
}
