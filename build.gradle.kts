plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
}

description = "Utility for reading/writing variable-length primitives"
group = "io.github.wasabithumb"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    (options as CoreJavadocOptions)
        .addBooleanOption("Xdoclint:none", true)
}

centralPortal {
    name = "vario"
    jarTask = tasks.jar
    sourcesJarTask = tasks.sourcesJar
    javadocJarTask = tasks.javadocJar
    pom {
        name = "vario"
        description = project.description
        url = "https://github.com/WasabiThumb/vario"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "wasabithumb"
                email = "wasabithumbs@gmail.com"
                organization = "Wasabi Codes"
                organizationUrl = "https://wasabithumb.github.io/"
                timezone = "-5"
            }
        }
        scm {
            connection = "scm:git:git://github.com/WasabiThumb/vario.git"
            url = "https://github.com/WasabiThumb/vario"
        }
    }
}
