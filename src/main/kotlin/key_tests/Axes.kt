package key_tests

import org.openrndr.Extension
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.draw.Drawer
import org.openrndr.extra.noise.lerp
import org.openrndr.math.Vector2


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val input = InputAxes().apply {
            speed = 10.0
        }

        var position = drawer.bounds.center

        extend(input)
        extend {
            position += input.axes
            drawer.circle(position, 100.0)
        }
    }
}
