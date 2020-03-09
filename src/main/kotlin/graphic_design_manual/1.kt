package graphic_design_manual

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import kotlin.random.Random


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        var positions: List<Vector2> = listOf()
        var random = Random(0)

        for (i in 0..50) {
            positions += Vector2(random.nextInt(50).toDouble(), random.nextInt(50).toDouble())
        }

        extend {
            drawLines(this)

            // White blocks
            drawer.stroke = ColorRGBa.WHITE
            for (p in positions) {
                drawer.rectangle(p.x / 50 * width - width / 100, p.y / 50 * width - width / 100,
                            width / 50.0, height / 50.0)
            }
        }
    }
}