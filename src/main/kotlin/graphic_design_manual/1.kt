package graphic_design_manual

import org.openrndr.application
import org.openrndr.color.ColorRGBa


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        extend {
            drawLines(this)
        }
    }
}