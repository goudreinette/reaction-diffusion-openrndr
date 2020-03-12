package shape_tests

import org.openrndr.application
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.shapes.*
import org.openrndr.math.map
import kotlin.math.sin

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        extend(Screenshots())
        extend {
            drawer.contour(regularPolygonRounded(6, map(-1.0,1.0, 0.0, 10.0, sin(seconds)), drawer.bounds.center, 250.0, 30.0))
        }
    }
}
