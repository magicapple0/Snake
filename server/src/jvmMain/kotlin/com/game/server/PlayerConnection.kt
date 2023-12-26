package com.game.server

import com.game.snake.Player
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class PlayerConnection(val session: DefaultWebSocketSession) {
    companion object {
        val ID = AtomicInteger()
    }

    val id = ID.getAndIncrement()

    var player = Player(id, arrayListOf())

    override fun toString(): String {
        return "PlayerConnection(id=$id)"
    }
}