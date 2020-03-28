import org.openrndr.application
import org.openrndr.color.ColorHSLa
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.parameters.ColorParameter
import org.openrndr.math.Vector2

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val g = GUI()
        val s = object {
            @ColorParameter("Main color")
            var mainColor = ColorRGBa.PINK
        }

        g.add(s)
        extend(g)

        extend {
            val complementary =  s.mainColor.toHSLa().complement.toRGBa()

            drawer.apply {
                background(s.mainColor)
                stroke = null

                fill = complementary
                circle(drawer.bounds.center - Vector2(250.0, 0.0), 50.0)

                fill = complementary.opacify(0.6)
                circle(drawer.bounds.center, 50.0)

                fill = complementary.opacify(0.3)
                circle(drawer.bounds.center + Vector2(250.0, 0.0), 50.0)
            }
        }
    }
}
