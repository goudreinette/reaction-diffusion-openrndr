package vector_control

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.*
import org.openrndr.panel.style.*


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val cm = ControlManager()
        var bgColor = ColorRGBa.BLACK
        var pp: XYPad? = null

        cm.body = layout(cm) {
            pp = xyPad {
                minX = 0.0
                maxX = width.toDouble()
                minY = 0.0
                maxY = height.toDouble()
                precision = 3
                showVector = true
            }

            button {

            }
        }

        extend(cm)
        extend {
            drawer.background(bgColor)
            drawer.circle(pp!!.value, 50.0)
        }
    }
}
