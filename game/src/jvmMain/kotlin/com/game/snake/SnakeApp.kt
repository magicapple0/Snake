package com.game.snake

import com.lehaine.littlekt.createLittleKtApp

fun main() {
    createLittleKtApp {
        width = 3680/3
        height = 2560/3
        vSync = true
        title = "Snake"
    }.start {
        SnakeGame(it)
    }
}

/*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }.start(wait = true)
}*/
