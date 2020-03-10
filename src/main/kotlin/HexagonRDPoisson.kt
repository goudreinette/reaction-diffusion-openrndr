import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.antialias.FXAA
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.math.clamp
import org.openrndr.math.map
import org.openrndr.poissonfill.PoissonFill


fun main() = application {
    configure {
        width = 1200
        height = 1200
    }

    program {
        val ew = EdgesWork()
        ew.radius = 15

        val rt = renderTarget(width, height) {
            colorBuffer()
        }

        val c = compose {
            layer {
                draw {
                    drawer.apply {
                        background(ColorRGBa.BLACK)
                        fill = ColorRGBa.WHITE
                        hexagon(drawer.bounds.center, 350.0, HexagonRotation.Pointy, seconds / 5.0)
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

        val c2 = compose {
            draw {
                drawer.isolatedWithTarget(rt) {
                    c.draw(drawer)
                }

                drawer.image(rt.colorBuffer(0))
            }

            post(FXAA())
        }

//        extend(ScreenRecorder())
        extend {
            ew.radius = clamp(map(0.0, width.toDouble(), 10.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()
            c2.draw(drawer)
        }
    }
}
