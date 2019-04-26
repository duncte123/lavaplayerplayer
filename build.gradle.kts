plugins {
    java
    idea
}

group = "me.duncte123"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    implementation("com.sedmelluq", "lavaplayer", "1.3.17")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}