import org.openrndr.application
import org.openrndr.draw.loadImage

fun main() = application {
    configure {
        width = 1200
        height = 1200
    }

    program {
//        val img = loadImage("screenshots/OPENRNDR-2020-03-09-22.05.41.png")
//        val img = loadImage("screenshots/OPENRNDR-2020-03-09-22.05.45.png")
//        val img = loadImage("screenshots/OPENRNDR-2020-03-09-22.05.56.png")
        val img = loadImage("screenshots/OPENRNDR-2020-03-09-22.06.00.png")

        extend {
            drawer.image(img, 200.0, 200.0, 400.0, 400.0)
            drawer.image(img, 600.0, 200.0, 400.0, 400.0)
            drawer.image(img, 600.0, 600.0, 400.0, 400.0)
            drawer.image(img, 200.0, 600.0, 400.0, 400.0)
        }
    }
}
