import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.internal.gl3.ColorBufferGL3
import kotlin.math.sin
import jsyphon.JSyphonServer
import org.openrndr.Extension
import org.openrndr.ExtensionStage
import org.openrndr.Program
import org.openrndr.draw.Drawer
import org.openrndr.draw.RenderTarget

class Syphon(val name: String = "OPENRNDR"): Extension {
    override var enabled = true
    val server = JSyphonServer()
    var target: RenderTarget? = null


    override fun setup(program: Program) {
        server.initWithName(name)
        target = renderTarget(program.width, program.height) {
            colorBuffer()
            depthBuffer()
        }
    }

    override fun beforeDraw(drawer: Drawer, program: Program) {
        target?.bind()
    }

    override fun afterDraw(drawer: Drawer, program: Program) {
        target?.unbind()
        drawer.image(target?.colorBuffer(0)!!)
        val glBuffer = target?.colorBuffer(0) as ColorBufferGL3

        // Send to Syphon
        server.publishFrameTexture(
            glBuffer.texture, glBuffer.target, 0, 0,
            program.width, program.height, program.width, program.height, false
        );
    }

    override fun shutdown(program: Program) {
        // Cleanup
        server.stop()
    }
}










fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        extend(Syphon())

        extend {
            drawer.background(ColorRGBa.RED)
            drawer.circle(width/2.0, height/2.0, sin(seconds) * width / 2.0)
        }
    }
}