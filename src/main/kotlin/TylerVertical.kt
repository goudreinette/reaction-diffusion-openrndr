import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.math.clamp
import org.openrndr.math.map




fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        drawer.apply {
            strokeWeight = 0.0
            stroke = ColorRGBa.TRANSPARENT
        }

        val ew = EdgesWork()

        val rt = renderTarget(800, 800) {
            colorBuffer()
        }

        val c = compose {
            layer {
                draw {
                    drawer.image(rt.colorBuffer(0))
                }

                post(ew)
            }
        }

        extend(Screenshots())
        extend {
            ew.radius = clamp(map(0.0, width.toDouble(), 10.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()

            drawer.apply {
                isolatedWithTarget(rt) {
                    drawGrid(ff = map(10.0, 400.0, 1.0, 20.0, ew.radius.toDouble()))

                    c.draw(drawer)

                    drawer.apply {
                        strokeWeight = 0.0
                        stroke = ColorRGBa.TRANSPARENT
                    }
                }

                drawer.image(rt.colorBuffer(0), 200.0, 000.0, 400.0, 400.0)
                drawer.image(rt.colorBuffer(0), 200.0, 400.0, 400.0, 400.0)
            }
        }
    }
}
