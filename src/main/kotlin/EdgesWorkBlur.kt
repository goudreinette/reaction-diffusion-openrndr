import org.openrndr.application
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.loadImage
import org.openrndr.draw.renderTarget
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.edges.EdgesWork
import org.openrndr.extras.imageFit.FitMethod
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.clamp
import org.openrndr.math.map


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
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

//        extend(ScreenRecorder())
        extend {
            // Mouse means radius
            ew.radius = clamp(map(0.0, width.toDouble(), 10.0, 800.0, mouse.position.x), 10.0, 400.0).toInt()

            drawer.isolatedWithTarget(rt) {
                c.draw(drawer)
            }

            drawer.image(rt.colorBuffer(0))
        }
    }
}
