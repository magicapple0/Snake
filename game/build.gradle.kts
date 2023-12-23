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
                implementation(libs.littlekt.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
            }
        }
        val jvmMain by getting
    }
}

dependencies{
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    commonMainImplementation("com.soywiz.korlibs.klock:klock:2.2.0")
}