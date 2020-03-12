package random

import org.openrndr.application
import org.openrndr.draw.loadFont

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val f = loadFont("data/fonts/IBMPlexMono-Regular.ttf", 32.0)
        extend {
            drawer.text("hello", 100.0, 100.0)
        }
    }
}
