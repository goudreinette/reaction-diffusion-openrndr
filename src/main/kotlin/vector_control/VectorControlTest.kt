package vector_control

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.button
import org.openrndr.panel.elements.layout
import org.openrndr.panel.elements.vector2
import org.openrndr.panel.elements.Vector2Control
import org.openrndr.panel.style.*


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val cm = ControlManager()
        var bgColor = ColorRGBa.BLACK
        var v: Vector2Control? = null

        cm.body = layout(cm) {
            v = vector2 {
                minX = 0.0
                maxX = width.toDouble()
                minY = 0.0
                maxY = height.toDouble()
            }

            button {

            }
        }

        extend(cm)
        extend {
            drawer.background(bgColor)
            drawer.circle(v!!.value, 50.0)
        }
    }
}
