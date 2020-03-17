import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.loadImage
import org.openrndr.draw.renderTarget
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.antialias.FXAA
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blend.Normal
import org.openrndr.extra.fx.blend.Overlay
import org.openrndr.extra.fx.blend.Subtract
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.math.clamp
import org.openrndr.math.map

fun main() = application {
    configure {
        width = 108 * 8
        height = 175 * 8
    }

    program {
        val i = loadImage("data/images/pm5544.png")
        val ew1 = EdgesWork()
        val ew2 = EdgesWork()
        val ew3 = EdgesWork()

        val rt = renderTarget(width, height) {
            colorBuffer()
        }

        val c1 = compose {
            draw {
                drawer.image(rt.colorBuffer(0))
            }

            post(ew1)
        }

        val c2 = compose {
            draw {
                drawer.image(rt.colorBuffer(0))
            }

            post(ew2)
        }

        val c3 = compose {
            draw {
                drawer.image(rt.colorBuffer(0))
            }

            post(ew3)
        }

        val combined = compose {
            layer {
                draw {
                    c1.draw(drawer)
                }

                layer {
                    blend(Multiply()) {
                        clip = true
                    }

                    draw {
                        c2.draw(drawer)
                    }

                    layer {
                        blend(Multiply()) {
                            clip = true
                        }

                        draw {
                            c3.draw(drawer)
                        }
                    }
                }
            }
        }

        extend {
            // Mouse means radius
            ew1.radius = clamp(map(0.0, width.toDouble(), 10.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()
            ew2.radius = clamp(map(0.0, width.toDouble(), 100.0, 1600.0, mouse.position.x), 10.0, 400.0).toInt()
            ew3.radius = clamp(map(0.0, width.toDouble(), 400.0, 3200.0, mouse.position.x), 10.0, 400.0).toInt()

            drawer.isolatedWithTarget(rt) {
                combined.draw(drawer)
            }

            drawer.image(rt.colorBuffer(0))
        }
    }
}
