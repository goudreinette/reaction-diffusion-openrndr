package key_tests

import org.openrndr.Extension
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.draw.Drawer
import org.openrndr.extra.noise.lerp
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.math.Vector2
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo

class InputAxes: Extension {
    override var enabled = true

    var axes = Vector2(0.0, 0.0)
    var targetAxes = Vector2(0.0, 0.0)

    @DoubleParameter("Acceleration", 0.1, 1.0)
    var acceleration = 0.9

    @DoubleParameter("Max speed", 0.0, 50.0)
    var speed = 10.0

    override fun beforeDraw(drawer: Drawer, program: Program) {
        program.keyboard.pressedKeys.let {
            targetAxes = Vector2(0.0, 0.0)

            if (it.contains("arrow-up") || it.contains("w")) {
                targetAxes = Vector2(targetAxes.x, targetAxes.y - speed)
            }

            if (it.contains("arrow-down") || it.contains("s")) {
                targetAxes = Vector2(targetAxes.x, targetAxes.y + speed)
            }

            if (it.contains("arrow-left") || it.contains("a")) {
                targetAxes = Vector2(targetAxes.x - speed, targetAxes.y)
            }

            if (it.contains("arrow-right") || it.contains("d")) {
                targetAxes = Vector2(targetAxes.x + speed, targetAxes.y)
            }

            axes = Vector2(lerp(targetAxes.x, axes.x, acceleration),
                           lerp(targetAxes.y, axes.y, acceleration))
        }
    }
}


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val gui = GUI()
        val input = InputAxes().apply {
            speed = 10.0
        }

        input.addTo(gui)

        var position = drawer.bounds.center

        extend(input)
        extend(gui)
        extend {
            position += input.axes
            drawer.circle(position, 100.0)
        }
    }
}
