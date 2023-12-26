package com.game.server

import com.game.snake.AppleEaten
import com.game.snake.Direction
import com.game.snake.Player
import com.lehaine.littlekt.math.Vec2i
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class PlayerConnection(val session: DefaultWebSocketSession) {
    companion object {
        val ID = AtomicInteger()
    }

    val id = ID.getAndIncrement()

    val head = Direction.LEFT

    var player = Player(id, arrayListOf(), head)

    var apple = AppleEaten( 0, Vec2i(0, 0))

    override fun toString(): String {
        return "PlayerConnection(id=$id)"
    }
}