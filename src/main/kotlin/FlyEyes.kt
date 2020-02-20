import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.loadImage
import org.openrndr.extra.olive.Olive
import org.openrndr.ffmpeg.VideoPlayerFFMPEG


class FlyEyes : Program() {
    lateinit var camera: VideoPlayerFFMPEG
    lateinit var mask: ColorBuffer
}

fun main() = application {
    configure {
        width = 1280
        height = 720
    }
    program(FlyEyes()) {
        camera = VideoPlayerFFMPEG.fromDevice()
        mask = loadImage("file:data/images/mask.png")
        camera.play()

        extend(Olive<FlyEyes>()) {
            script = "src/main/kotlin/FlyEyesLive.Kt"
        }
    }
}
