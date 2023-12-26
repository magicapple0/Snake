package com.game.server

import com.game.snake.Direction
import com.game.snake.Player
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class PlayerConnection(val session: DefaultWebSocketSession) {
    companion object {
        val ID = AtomicInteger()
    }

    val id = ID.getAndIncrement()

    val head = Direction.LEFT

    var player = Player(id, arrayListOf(), head)

    override fun toString(): String {
        return "PlayerConnection(id=$id)"
    }
}