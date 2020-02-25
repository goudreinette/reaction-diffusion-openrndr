package graphic_design_manual

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer


fun drawLines(program: Program) {
    program.drawer.apply {
        background(ColorRGBa.WHITE)
        stroke = ColorRGBa.BLACK

        for (i in 0..50) {
            val x = width / 50.0 * i
            lineSegment(x, 0.0, x, program.height.toDouble())
        }
    }
}