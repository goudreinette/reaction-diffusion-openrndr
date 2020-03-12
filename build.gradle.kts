import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform


group = "org.openrndr.template"
version = "0.3.9"
val applicationMainClass = "TemplateProgramKt"


val openrndrUseSnapshot = true
val openrndrVersion = if (openrndrUseSnapshot) "0.4.0-SNAPSHOT" else "0.3.39"

val panelUseSnapshot = false
val panelVersion = if (panelUseSnapshot) "0.4.0-SNAPSHOT" else "0.3.21"

val orxUseSnapshot = true
val orxVersion = if (orxUseSnapshot) "0.4.0-SNAPSHOT" else "0.3.47"

// supported features are:
val orxFeatures = setOf(
    "orx-camera",
    "orx-compositor",
    "orx-easing",
    "orx-file-watcher",
    "orx-parameters",
    "orx-filter-extension",
    "orx-fx",
    "orx-glslify",
    "orx-gradient-descent",
    "orx-integral-image",
    "orx-interval-tree",
    "orx-jumpflood",
    "orx-gui",
    "orx-image-fit",
    "orx-kdtree",
    "orx-mesh-generators",
    "orx-midi",
    "orx-no-clear",
    "orx-noise",
    "orx-obj-loader",
    "orx-olive",
    "orx-osc",
    "orx-palette",
    "orx-poisson-fill",
    "orx-runway",
    "orx-shader-phrases",
    "orx-shade-styles",
    "orx-shapes",
    "orx-syphon",
    "orx-temporal-blur",
    "orx-kinect-v1"
)


// supported features are: video, panel
val openrndrFeatures = setOf("video", "panel")

// --------------------------------------------------------------------------------------------------------------------

val supportedPlatforms = setOf("windows", "macos", "linux-x64", "linux-arm64")

val openrndrOs = if (project.hasProperty("targetPlatform")) {
    val platform: String = project.property("targetPlatform") as String
    if (platform !in supportedPlatforms) {
        throw IllegalArgumentException("target platform not supported: $platform")
    } else {
        platform
    }
} else when (OperatingSystem.current()) {
    OperatingSystem.WINDOWS -> "windows"
    OperatingSystem.MAC_OS -> "macos"
    OperatingSystem.LINUX -> when (val h = DefaultNativePlatform("current").architecture.name) {
        "x86-64" -> "linux-x64"
        "aarch64" -> "linux-arm64"
        else -> throw IllegalArgumentException("architecture not supported: $h")
    }
    else -> throw IllegalArgumentException("os not supported")
}

enum class Logging {
    NONE,
    SIMPLE,
    FULL
}

val applicationLogging = Logging.SIMPLE

val kotlinVersion = "1.3.61"

plugins {
    java
    kotlin("jvm") version ("1.3.61")
}

repositories {
    mavenCentral()
    if (openrndrUseSnapshot || orxUseSnapshot || panelUseSnapshot) {
        mavenLocal()
    }
    maven(url = "https://dl.bintray.com/openrndr/openrndr")
}

fun DependencyHandler.orx(module: String): Any {
    return "org.openrndr.extra:$module:$orxVersion"
}

fun DependencyHandler.openrndr(module: String): Any {
    return "org.openrndr:openrndr-$module:$openrndrVersion"
}

fun DependencyHandler.openrndrNatives(module: String): Any {
    return "org.openrndr:openrndr-$module-natives-$openrndrOs:$openrndrVersion"
}

fun DependencyHandler.orxNatives(module: String): Any {
    return "org.openrndr.extra:$module-natives-$openrndrOs:$orxVersion"
}

val lwjglVersion = "3.2.3"
val lwjglNatives = "natives-macos"


dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-jemalloc:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-openal:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-stb:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-egl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-tinyexr:$lwjglVersion")
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-assimp")
    implementation("org.lwjgl:lwjgl-bgfx")
    implementation("org.lwjgl:lwjgl-cuda")
    implementation("org.lwjgl:lwjgl-egl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-jawt")
    implementation("org.lwjgl:lwjgl-jemalloc")
    implementation("org.lwjgl:lwjgl-libdivide")
    implementation("org.lwjgl:lwjgl-llvm")
    implementation("org.lwjgl:lwjgl-lmdb")
    implementation("org.lwjgl:lwjgl-lz4")
    implementation("org.lwjgl:lwjgl-meow")
    implementation("org.lwjgl:lwjgl-nanovg")
    implementation("org.lwjgl:lwjgl-nfd")
    implementation("org.lwjgl:lwjgl-nuklear")
    implementation("org.lwjgl:lwjgl-odbc")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opencl")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-opengles")
    implementation("org.lwjgl:lwjgl-openvr")
    implementation("org.lwjgl:lwjgl-opus")
    implementation("org.lwjgl:lwjgl-par")
    implementation("org.lwjgl:lwjgl-remotery")
    implementation("org.lwjgl:lwjgl-rpmalloc")
    implementation("org.lwjgl:lwjgl-shaderc")
    implementation("org.lwjgl:lwjgl-sse")
    implementation("org.lwjgl:lwjgl-stb")
    implementation("org.lwjgl:lwjgl-tinyexr")
    implementation("org.lwjgl:lwjgl-tinyfd")
    implementation("org.lwjgl:lwjgl-tootle")
    implementation("org.lwjgl:lwjgl-vma")
    implementation("org.lwjgl:lwjgl-vulkan")
    implementation("org.lwjgl:lwjgl-xxhash")
    implementation("org.lwjgl:lwjgl-yoga")
    implementation("org.lwjgl:lwjgl-zstd")
    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-bgfx::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-jemalloc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-libdivide::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-llvm::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-lmdb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-lz4::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-meow::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nanovg::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nfd::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nuklear::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengles::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openvr::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opus::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-par::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-remotery::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-rpmalloc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-shaderc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-sse::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-tinyexr::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-tinyfd::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-tootle::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-vma::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-vulkan::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-xxhash::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-yoga::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-zstd::$lwjglNatives")

    implementation(openrndr("gl3"))
    implementation(openrndrNatives("gl3"))
    implementation(openrndr("openal"))
    runtimeOnly(openrndrNatives("openal"))
    implementation(openrndr("core"))
    implementation(openrndr("svg"))
    implementation(openrndr("animatable"))
    implementation(openrndr("extensions"))
    implementation(openrndr("filter"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.3")

    implementation("io.github.microutils", "kotlin-logging", "1.7.8")

    when (applicationLogging) {
        Logging.NONE -> {
            runtimeOnly("org.slf4j", "slf4j-nop", "1.7.29")
        }
        Logging.SIMPLE -> {
            runtimeOnly("org.slf4j", "slf4j-simple", "1.7.29")
        }
        Logging.FULL -> {
            runtimeOnly("org.apache.logging.log4j", "log4j-slf4j-impl", "2.13.0")
            runtimeOnly("com.fasterxml.jackson.core", "jackson-databind", "2.10.1")
            runtimeOnly("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.10.1")
        }
    }

    if ("video" in openrndrFeatures) {
        implementation(openrndr("ffmpeg"))
        runtimeOnly(openrndrNatives("ffmpeg"))
    }

    if ("panel" in openrndrFeatures) {
        implementation("org.openrndr.panel:openrndr-panel:$panelVersion")
    }

    for (feature in orxFeatures) {
        implementation(orx(feature))
    }

    if ("orx-kinect-v1" in orxFeatures) {
//        runtimeOnly("orx-kinect-v1")
    }

    if ("orx-olive" in orxFeatures) {
        implementation("org.jetbrains.kotlin", "kotlin-scripting-compiler-embeddable")
    }

    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.12")
}

// --------------------------------------------------------------------------------------------------------------------

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = applicationMainClass
    }
    doFirst {
        from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }

    exclude(listOf("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA", "**/module-info*"))
    archiveFileName.set("application-$openrndrOs.jar")
}


tasks.create("zipDistribution", Zip::class.java) {
    archiveFileName.set("application-$openrndrOs.zip")
    from("./") {
        include("data/**")
    }
    from("$buildDir/libs/application-$openrndrOs.jar")
}.dependsOn(tasks.jar)

tasks.create("run", JavaExec::class.java) {
    main = applicationMainClass
    classpath = sourceSets.main.get().runtimeClasspath
}.dependsOn(tasks.build)
