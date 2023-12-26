package com.game.snake

import com.lehaine.littlekt.math.Vec2i
import com.soywiz.klock.DateTime
import kotlinx.serialization.*

@Serializable
sealed class PacketData


@Serializable
data class Packet(
        val data: PacketData,
        val creationTimeMillis: Long = DateTime.nowUnixLong()
)

@Serializable
data class Player(val id: Int, var snakeBody: ArrayList<@Serializable(Vec2iSerializer::class)Vec2i>,
                  val headDirection: Direction) : PacketData()

@Serializable
data class PlayerRemoved(val id: Int) : PacketData()

@Serializable
data class AppleEaten(val playerId: Int, var newApple: @Serializable(Vec2iSerializer::class)Vec2i?) : PacketData()


