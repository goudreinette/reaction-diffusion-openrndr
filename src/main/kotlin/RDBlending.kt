import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.ColorRGBa.Companion.RED
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
import org.openrndr.extra.fx.color.ColorCorrection
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.extra.gui.GUI
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
        val c = ColorCorrection()
        val gui = GUI()

        gui.add(c)

        val rt = renderTarget(width, height) {
            colorBuffer()
        }

        val c1 = compose {
            draw {
                drawer.image(rt.colorBuffer(0))
            }

            post(ew1)
        }

        val combined = compose {
            layer {
                draw {
                    drawer.background(RED)
                }

                layer {
                    blend(Multiply()) {
                        clip = true
                    }

                    draw {
                        c1.draw(drawer)
                    }

                    post(c)
                }
            }
        }

        extend(gui)
        extend {
            // Mouse means radius
            ew1.radius = clamp(map(0.0, width.toDouble(), 10.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()

            drawer.isolatedWithTarget(rt) {
                combined.draw(drawer)
            }

            drawer.image(rt.colorBuffer(0))
        }
    }
}
