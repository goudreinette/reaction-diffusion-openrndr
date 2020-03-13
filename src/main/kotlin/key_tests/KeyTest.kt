package key_tests

import org.openrndr.application
import org.openrndr.math.Vector2

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        var position = drawer.bounds.center
        val speed = 10.0

        extend {
            keyboard.pressedKeys.let {
                if (it.contains("arrow-up") || it.contains("w")) {
                    position = Vector2(position.x, position.y - speed)
                }

                if (it.contains("arrow-down") || it.contains("s")) {
                    position = Vector2(position.x, position.y + speed)
                }

                if (it.contains("arrow-left") || it.contains("a")) {
                    position = Vector2(position.x - speed, position.y)
                }

                if (it.contains("arrow-right") || it.contains("d")) {
                    position = Vector2(position.x + speed, position.y)
                }
            }

            drawer.circle(position, 100.0)
        }
    }
}
