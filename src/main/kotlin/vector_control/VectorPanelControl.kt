package vector_control

import io.reactivex.subjects.PublishSubject
import org.openrndr.MouseEvent
import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.math.Vector2
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.*
import org.openrndr.panel.style.*
import org.openrndr.shape.Rectangle
import org.openrndr.shape.contour
import org.openrndr.text.Writer
import kotlin.and
import kotlin.math.round
import org.openrndr.math.map
import org.openrndr.math.clamp
import org.openrndr.shape.LineSegment


class VectorControl : Element(ElementType("vector")) {
    class VectorControlEvent(val source: VectorControl)
    class Events(val clicked: PublishSubject<VectorControlEvent> = PublishSubject.create())

    val events = Events()

    var v = Vector2(-0.5, 0.8)

    init {
        mouse.clicked.subscribe {
            it.cancelPropagation();
            pick(it);
        }

        mouse.dragged.subscribe {
            it.cancelPropagation();
            pick(it);
        }

        mouse.pressed.subscribe {
            it.cancelPropagation()
        }
    }

    private fun pick(e: MouseEvent) {
        val dx = e.position.x - layout.screenX
        var dy = e.position.y - layout.screenY

        val x = clamp(dx / layout.screenWidth * 2.0 - 1.0, -1.0, 1.0)
        val y = clamp(dy / layout.screenHeight * 2.0 - 1.0, -1.0, 1.0)

        v = Vector2(x, y)

        draw.dirty = true
    }

    override val widthHint: Double?
        get() = 200.0

    val computedBallPosition: Vector2
        get() = Vector2(
            map(-1.0, 1.0, 0.0, layout.screenWidth, v.x),
            map(-1.0, 1.0, 0.0, layout.screenHeight, v.y)
        )

    override fun draw(drawer: Drawer) {
        computedStyle.let {
            drawer.pushTransforms()
            drawer.pushStyle()
            drawer.fill = ColorRGBa.GRAY
            drawer.stroke = null
            drawer.strokeWeight = 0.0

            drawer.rectangle(0.0, 0.0, layout.screenWidth, layout.screenHeight)


            // lines grid
            drawer.stroke = ColorRGBa.GRAY.shade(1.2)
            drawer.strokeWeight = 1.0

            for (y in 0 until 20) {
                drawer.lineSegment(
                    0.0,
                    layout.screenHeight / 20 * y,
                    layout.screenWidth - 1.0,
                    layout.screenHeight / 20 * y
                )
            }

            for (x in 0 until 20) {
                drawer.lineSegment(
                    layout.screenWidth / 20 * x,
                    0.0,
                    layout.screenWidth / 20 * x,
                    layout.screenHeight - 1.0
                )
            }

            // cross
            drawer.stroke = ColorRGBa.GRAY.shade(1.6)
            drawer.lineSegment(0.0, layout.screenHeight / 2.0, layout.screenWidth, layout.screenHeight / 2.0)
            drawer.lineSegment(layout.screenWidth / 2.0, 0.0, layout.screenWidth / 2.0, layout.screenHeight)

            // ball
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = ColorRGBa.WHITE
            drawer.circle(computedBallPosition, 8.0)

            drawer.popStyle()
            drawer.popTransforms()
        }
    }
}


fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val cm = ControlManager()
        var bgColor = ColorRGBa.BLACK
        var vectorControl = VectorControl()

        // TODO: move to default stylesheet
        cm.layouter.styleSheets.add(styleSheet(has type "vector") {
            display = Display.BLOCK
            background = Color.RGBa(ColorRGBa.GRAY)
            width = 200.px
            height = 200.px
            paddingLeft = 10.px
            paddingRight = 10.px
            marginLeft = 5.px
            marginRight = 5.px
            marginTop = 5.px
            marginBottom = 5.px

            and(has state "hover") {
                display = Display.BLOCK
                background = Color.RGBa(ColorRGBa.GRAY.shade(1.5))
            }
        })

        cm.body = layout(cm) {
            initElement(arrayOf(), vectorControl) {
            }

            button {

            }
        }

        extend(cm)
        extend {
            drawer.background(bgColor)

            drawer.circle(vectorControl.v * width.toDouble() / 2.0 + Vector2(width / 2.0, width / 2.0), 50.0)
        }
    }
}
