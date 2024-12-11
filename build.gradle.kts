plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("it.unimi.dsi:fastutil:8.5.8")
    implementation("org.openjdk.jmh:jmh-core:1.36")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}



tasks.test {
    useJUnitPlatform()
}