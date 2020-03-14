package vector_control

import org.openrndr.application
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extra.parameters.Vector2Parameter
import org.openrndr.math.Vector2

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val gui = GUI()

        val settings = @Description("Vector parameter!") object {
            @Vector2Parameter("Position", 0.0, 0.0, 800.0, 800.0)
            var position: Vector2 = Vector2(0.0,0.0)

            @ActionParameter("Test")
            fun test() {

            }
        }

        gui.add(settings)

        extend(gui)
        extend {
            drawer.circle(settings.position, 50.0)
        }
    }
}
