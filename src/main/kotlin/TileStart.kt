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


fun Drawer.drawGrid(ff: Double = 8.0) {
    val f = width / 9.0

    for (x in 0 until 9) {
        fill = if (x % 2 == 0) ColorRGBa.BLACK else ColorRGBa.WHITE
        rectangle(f * x.toDouble(), -f + ff, f, f)
        rectangle(f * x.toDouble(), height - ff, f, f)
    }

    for (y in 0 until 9) {
        fill = if (y % 2 == 0) ColorRGBa.BLACK else ColorRGBa.WHITE
        rectangle(0.0, f * y.toDouble(), ff, f)
        rectangle(width - ff, f * y.toDouble(), ff, f)
    }
}


fun main() = application {
    configure {
        width = 1200
        height = 1200
    }

    program {
        drawer.apply {
            strokeWeight = 0.0
            stroke = ColorRGBa.TRANSPARENT
        }

        val ew = EdgesWork()

        val rt = renderTarget(width, height) {
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
                    drawGrid()

                    c.draw(drawer)

                    drawer.apply {
                        strokeWeight = 0.0
                        stroke = ColorRGBa.TRANSPARENT
                    }
                }

                drawer.image(rt.colorBuffer(0), 200.0, 200.0, 400.0, 400.0)
                drawer.image(rt.colorBuffer(0), 600.0, 200.0, 400.0, 400.0)
                drawer.image(rt.colorBuffer(0), 600.0, 600.0, 400.0, 400.0)
                drawer.image(rt.colorBuffer(0), 200.0, 600.0, 400.0, 400.0)
            }
        }
    }
}
