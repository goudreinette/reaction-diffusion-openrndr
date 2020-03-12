//import javafx.application.Platform
//import kotlinx.coroutines.GlobalScope
//import org.openrndr.*
//import org.openrndr.color.ColorRGBa
//import tornadofx.*
//import kotlin.math.sin
//
//class MyView: View() {
//    override val root = vbox {
//        button("Press me")
//        label("Waiting")
//    }
//}
//
//class MyApp: App(MyView::class)
//
//
//class FXTest: Program() {
//    override fun setup() {
//
//    }
//
//    override fun draw() {
//        drawer.background(ColorRGBa.RED)
//        drawer.circle(200.0, 200.0, 50.0 + sin(seconds))
//    }
//}
//
//fun main(args: Array<String>) {
//    Application.run(FXTest(), Configuration())
//    tornadofx.launch<MyApp>(args)
//}
