package vector_control

import io.reactivex.subjects.PublishSubject
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.*
import org.openrndr.panel.style.*
import org.openrndr.shape.Rectangle
import org.openrndr.shape.contour
import org.openrndr.text.Writer
import kotlin.and
import kotlin.math.round

class VectorControl : Element(ElementType("vector")) {

    var label: String = "OK"

    class VectorControlEvent(val source: VectorControl)
    class Events(val clicked: PublishSubject<VectorControlEvent> = PublishSubject.create())

    var data: Any? = null

    val events = Events()

    init {
        mouse.pressed.subscribe {
            it.cancelPropagation()
        }

        mouse.clicked.subscribe {
            if (disabled !in pseudoClasses) {
                events.clicked.onNext(VectorControlEvent(this))
            }
            it.cancelPropagation()
        }

        keyboard.pressed.subscribe {
            if (it.key == 32) {
                it.cancelPropagation()
                if (disabled !in pseudoClasses) {
                    events.clicked.onNext(VectorControlEvent(this))
                }
            }
        }
    }

    override val widthHint: Double
        get() = 200.0


     override fun draw(drawer: Drawer) {
        computedStyle.let {
            drawer.pushTransforms()
            drawer.pushStyle()
            drawer.fill = ColorRGBa.GRAY
            drawer.stroke = null
            drawer.strokeWeight = 0.0

            drawer.rectangle(0.0, 0.0, layout.screenWidth, layout.screenHeight)

            drawer.stroke = ColorRGBa.GRAY.shade(1.4)
            drawer.strokeWeight = 1.0
            drawer.lineSegment(0.0, layout.screenHeight / 2.0, layout.screenWidth, layout.screenHeight / 2.0)
            drawer.lineSegment(layout.screenWidth / 2.0, 0.0, layout.screenWidth / 2.0, layout.screenHeight)

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

        // TODO: move to default stylesheet
        cm.layouter.styleSheets.add(styleSheet(has type "vector") {
            display = Display.BLOCK
            background = Color.RGBa(ColorRGBa.GRAY)
            width = LinearDimension.Auto
            height = 96.px
            paddingLeft = 10.px
            paddingRight = 10.px
            marginLeft = 5.px
            marginRight = 5.px
            marginTop = 5.px
            marginBottom = 5.px

//            and(has state "selected") {
//                display = Display.BLOCK
//                background = controlActiveColor
//            }
            and(has state "hover") {
                display = Display.BLOCK
                background  = Color.RGBa(ColorRGBa.GRAY.shade(1.5))
            }
        })

        cm.body = layout(cm) {
            initElement(arrayOf(), VectorControl()) {

            }

            button {

            }
        }

        extend(cm)
        extend {
            drawer.background(bgColor)
        }
    }
}
