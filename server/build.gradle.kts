val ktor_version = "2.3.7"
val logback_version= "1.2.3"

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.22"
}


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.littlekt.core)

                //implementation("io.ktor:ktor-server-core:$ktor_version")
                //implementation("io.ktor:ktor-server-netty:$ktor_version")


                implementation("io.ktor:ktor-server-auth:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
                implementation("ch.qos.logback:logback-classic:$logback_version")
                //implementation("io.ktor:ktor-server-cors:$ktor_version")
                implementation("com.soywiz.korlibs.klock:klock:2.2.0")
                implementation("io.ktor:ktor-server-default-headers:$ktor_version")
                implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-server-websockets:$ktor_version")
                implementation(project(":game"))
            }
        }
        val jvmMain by getting
    }
}

