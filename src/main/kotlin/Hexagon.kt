import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.shape.Color
import org.openrndr.shape.contour
import java.util.*
import kotlin.math.sin
import kotlin.math.sqrt

enum class HexagonRotation {
    Pointy,
    Flat
}

fun Drawer.hexagon(center: Vector2, radius: Double, rotation: HexagonRotation = HexagonRotation.Flat, seconds: Double = 0.0) {
    isolated {
        val h = sqrt(3.0) * radius / 2.0
        translate(center)

        if (rotation == HexagonRotation.Pointy) rotate(30.0)

        rotate(sin(seconds) * 180)
        val c = contour {
            moveTo(-radius/2, -h)
            lineTo(radius/2, -h)
            lineTo(radius, 0.0)
            lineTo(radius/2, h)
            lineTo(-radius/2, h)
            lineTo(-radius, 0.0)
            close()
        }

        contour(c)
    }
}


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        extend {
            drawer.fill = ColorRGBa.TRANSPARENT
            drawer.stroke = ColorRGBa.WHITE

            drawer.hexagon(drawer.bounds.center, 350.0, HexagonRotation.Pointy)
//            drawer.hexagon(drawer.bounds.center , 100.0, HexagonRotation.Pointy)
//            drawer.hexagon(drawer.bounds.center - Vector2(175.0, 0.0), 100.0, HexagonRotation.Pointy)

            drawer.circle(drawer.bounds.center, 350.0)
        }
    }
}
