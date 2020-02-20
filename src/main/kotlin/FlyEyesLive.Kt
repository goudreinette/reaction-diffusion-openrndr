@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.Program
import org.openrndr.draw.*
import org.openrndr.extra.fx.blur.BoxBlur
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.*
import org.openrndr.extra.gui.*

{ program: FlyEyes ->
    program.apply {
        class Hexagons: Filter(filterShaderFromUrl("file:data/glsl/fly-eyes.glsl"))

        val blur = BoxBlur()
        val hexagons = Hexagons()

        val composite = compose {
            draw {
                camera.draw(drawer)
            }

            post(blur) {
                window = 25
            }

            post(hexagons)
        }

        val gui = GUI()
        gui.add(blur)
        extend(gui)

        extend {
            composite.draw(drawer)
            drawer.image(mask, 0.0, 0.0, width.toDouble(), height.toDouble())
        }
    }
}