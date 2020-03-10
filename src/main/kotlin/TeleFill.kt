import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadImage
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.post
import org.openrndr.extras.imageFit.FitMethod
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.poissonfill.PoissonFill


// TODO: After Effects with RotoBrush isolated!!

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val img = loadImage("data/images/baby.png")

        val composition = compose {
            draw {
                drawer.imageFit(img, 0.0, 0.0, width.toDouble(), height.toDouble(), 0.0, 0.0, FitMethod.Contain)

                drawer.fill = ColorRGBa.fromHex("#dddcab") // ColorRGBa.fromHex("#8fbcd3")
                drawer.stroke = ColorRGBa.TRANSPARENT

                val w = 1.0
                drawer.rectangle(0.0, 0.0, width.toDouble(), w)
                drawer.rectangle(0.0, height - w, width.toDouble(), w)
                drawer.rectangle(0.0, 0.0, w, height.toDouble())
                drawer.rectangle(width - w, 0.0, width.toDouble(), height.toDouble())
            }

            post(PoissonFill())
        }

        extend(Screenshots())
        extend {
            composition.draw(drawer)
        }
    }
}
