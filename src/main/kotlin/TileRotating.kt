import org.openrndr.application
import org.openrndr.color.ColorRGBa


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val f = width / 9.0

        extend {
            val s = (seconds * f)

            for (x in -1 until 10) {
                drawer.fill = if (x % 2 == 0) ColorRGBa.BLACK else ColorRGBa.WHITE
                drawer.rectangle(f * x.toDouble() + s, 0.0, f, f)
                drawer.rectangle(f * x.toDouble() - s, height - f, f, f)
            }

            for (y in -1 until 10) {
                drawer.fill = if (y % 2 == 0) ColorRGBa.BLACK else ColorRGBa.WHITE
                drawer.rectangle(0.0, f * y.toDouble() - s, f, f)
                drawer.rectangle(width - f, f * y.toDouble() + s, f, f)
            }
        }
    }
}
