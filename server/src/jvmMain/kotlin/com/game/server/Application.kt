package com.game.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.game.server.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        configureHTTP()
        configureRouting()
        configureSerialization()
        configureSockets()
    }.start(wait = true)
}


