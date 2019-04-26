plugins {
    java
    idea
    id("org.openjfx.javafxplugin") version "0.0.7"
}

val jfxVersion = "11.0.1"

group = "me.duncte123"
version = "1.0-SNAPSHOT"

javafx {
    version = jfxVersion
    modules = arrayListOf("javafx.base", "javafx.controls", "javafx.graphics")
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.sedmelluq", "lavaplayer", "1.3.16")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")

    implementation("org.openjfx", "javafx-base", jfxVersion)
    implementation("org.openjfx", "javafx-controls", jfxVersion)
    implementation("org.openjfx", "javafx-graphics", jfxVersion, classifier = "win")
    implementation("org.openjfx", "javafx-graphics", jfxVersion, classifier = "linux")
    implementation("org.openjfx", "javafx-graphics", jfxVersion, classifier = "mac")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}