repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations {
            val main by getting

            val mainClass = (findProperty("jvm.mainClass") as? String)?.plus("Kt")
                ?: project.logger.log(
                    LogLevel.ERROR,
                    "Property 'jvm.mainClass' has either changed or has not been set. Check 'gradle.properties' and ensure it is properly set!"
                )
            tasks {
                register<Copy>("copyResources") {
                    group = "package"
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    from(main.output.resourcesDir)
                    destinationDir = File("$buildDir/publish")
                }
                register<Jar>("packageFatJar") {
                    group = "package"
                    archiveClassifier.set("all")
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    dependsOn(named("jvmJar"))
                    dependsOn(named("copyResources"))
                    manifest {
                        attributes["Main-Class"] = mainClass
                    }
                    destinationDirectory.set(File("$buildDir/publish/"))
                    from(
                        main.runtimeDependencyFiles.map { if (it.isDirectory) it else zipTree(it) },
                        main.output.classesDirs
                    )
                    doLast {
                        project.logger.lifecycle("[LittleKt] The packaged jar is available at: ${outputs.files.first().parent}")
                    }
                }
            }
        }
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.littlekt.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
            }
        }
        val jvmMain by getting
        //val serverMain by getting
    }
}

