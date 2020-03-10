import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.loadImage
import org.openrndr.draw.renderTarget
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.extra.noise.lerp
import org.openrndr.extras.imageFit.FitMethod
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.clamp
import org.openrndr.math.map
import kotlin.math.sin


fun main() = application {
    configure {
        width = 800
        height = 800
//        hideCursor = true
    }

    program {
        var mouseRadius = 0.0
        var mouseDown = false

        val i = loadImage("data/images/pm5544.png")
        val ew = EdgesWork()

        val rt = renderTarget(800, 800) {
            colorBuffer()
        }

        drawer.isolatedWithTarget(rt) {
            drawer.image(i)
        }

        val c = compose {
            layer {
                draw {
                    drawer.image(rt.colorBuffer(0))
                }

                post(ew)
            }
        }

        mouse.buttonDown.listen { mouseDown = true }
        mouse.buttonUp.listen { mouseDown = false }

//        extend(ScreenRecorder())
        extend {
            ew.radius = map(-1.0, 1.0, 14.0, 400.0, sin(seconds / 10)).toInt()

            drawer.isolatedWithTarget(rt) {
                c.draw(drawer)
            }

            // Mouse means radius
            val r = clamp(ew.radius.toDouble(), 100.0, 200.0)

            mouseRadius = if (mouseDown) {
                lerp(r, mouseRadius, .5)
            } else {
                lerp(0.0, mouseRadius, .5)
            }

            drawer.isolatedWithTarget(rt) {
                drawer.fill = ColorRGBa.WHITE
                drawer.strokeWeight = mouseRadius / 4
                drawer.stroke = ColorRGBa.BLACK
                drawer.circle(mouse.position, mouseRadius)

                drawer.fill = ColorRGBa.BLACK
                drawer.circle(mouse.position, mouseRadius / 2.0)
            }

            drawer.image(rt.colorBuffer(0))
        }
    }
}
