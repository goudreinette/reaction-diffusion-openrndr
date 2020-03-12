package vector_control

import io.reactivex.subjects.PublishSubject
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.*
import org.openrndr.panel.style.LinearDimension
import org.openrndr.panel.style.fontFamily
import org.openrndr.panel.style.fontSize
import org.openrndr.shape.Rectangle
import org.openrndr.text.Writer
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
        get() {
            computedStyle.let { style ->
                val fontUrl = (root() as? Body)?.controlManager?.fontManager?.resolve(style.fontFamily) ?: "broken"
                val fontSize = (style.fontSize as? LinearDimension.PX)?.value ?: 14.0
                val fontMap = FontImageMap.fromUrl(fontUrl, fontSize)

                val writer = Writer(null)

                writer.box = Rectangle(0.0,
                    0.0,
                    Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY)

                writer.drawStyle.fontMap = fontMap
                writer.newLine()
                writer.text(label, visible = false)

                return writer.cursor.x
            }
        }

     override fun draw(drawer: Drawer) {

        computedStyle.let {

            drawer.pushTransforms()
            drawer.pushStyle()
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            drawer.strokeWeight = 0.0

            drawer.rectangle(0.0, 0.0, layout.screenWidth, layout.screenHeight)

            (root() as? Body)?.controlManager?.fontManager?.let {
                val font = it.font(computedStyle)
                val writer = Writer(drawer)
                drawer.fontMap = (font)
                val textWidth = writer.textWidth(label)
                val textHeight = font.ascenderLength

                val offset = round((layout.screenWidth - textWidth) / 2.0)
                val yOffset = round((layout.screenHeight / 2) + textHeight / 2.0 - 2.0) * 1.0

                drawer.fill =  ColorRGBa.WHITE.opacify(
                    if (disabled in pseudoClasses) 0.25 else 1.0
                )
                drawer.text(label, 0.0 + offset, 0.0 + yOffset)
            }

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
        var bgColor = ColorRGBa.PINK

        cm.body = layout(cm) {
            initElement(arrayOf(), VectorControl()) {

            }
        }

        extend(cm)
        extend {
            drawer.background(bgColor)
        }
    }
}
