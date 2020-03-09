import org.openrndr.application
import org.openrndr.color.ColorRGBa


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    fun grid(stepsX: Int, stepsY: Int, w: Int, h: Int, fn: (Int, Int, Double, Double) -> Unit) {
        for (x in 0 until stepsX) {
            for (y in 0 until stepsY) {
                fn(x, y, w / stepsX.toDouble(), w/stepsY.toDouble())
            }
        }
    }

    program {
        val f = width / 9.0
        extend {
            for (x in 0 until 9) {
                drawer.fill = if (x % 2 == 0) ColorRGBa.BLACK else ColorRGBa.WHITE
                drawer.rectangle(f * x.toDouble(), 0.0, f, f)
                drawer.rectangle(f * x.toDouble(), height - f, f, f)
            }

            for (y in 0 until 9) {
                drawer.fill = if (y % 2 == 0) ColorRGBa.BLACK else ColorRGBa.WHITE
                drawer.rectangle(0.0, f * y.toDouble(), f, f)
                drawer.rectangle(width - f, f * y.toDouble(), f, f)
            }
        }
    }
}
