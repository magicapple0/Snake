package com.game.snake

import com.lehaine.littlekt.graphics.Texture
import com.lehaine.littlekt.graphics.g2d.Batch
import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.math.geom.Angle

class SnakeEntity(id: Int , level: GameLevel){
    private val id: Int
    private val util = Util()
    private var tailTexture: Texture
    private var headTexture: Texture
    var score = 0
    //from end to head
    private var snakeBody: ArrayList<Vec2i> = arrayListOf()
    private var headDirection = Direction.RIGHT
    var head: Vec2i get() = snakeBody[snakeBody.size - 1]

    init{
        this.id = id
        this.tailTexture = ResourceManager.tailTexture
        this.headTexture = ResourceManager.headTexture
        this.snakeBody = level.getSnakeSpawn(id)
        head = snakeBody[snakeBody.size - 1]
    }

    fun update(level: GameLevel) {
        snakeBody.forEach {
            if (level.isColliding(Vec2i(it.x, it.y + 1), id))
                return
        }
        for (i in 0..<snakeBody.size)
            snakeBody[i] = Vec2i(snakeBody[i].x, snakeBody[i].y + 1)
    }

    private fun moveBody(vec: Vec2i, level: GameLevel){
        if ((snakeBody[snakeBody.size - 1].y + vec.y == snakeBody[snakeBody.size - 2].y &&
            snakeBody[snakeBody.size - 1].x + vec.x == snakeBody[snakeBody.size - 2].x) ||
            level.isColliding(Vec2i(snakeBody[snakeBody.size - 1].x + vec.x, snakeBody[snakeBody.size - 1].y + vec.y), id) ||
            snakeBody.contains(Vec2i(snakeBody[snakeBody.size - 1].x + vec.x, snakeBody[snakeBody.size - 1].y + vec.y))                    )
            return
        var i = 0
        while(i < snakeBody.size - 1) {
            snakeBody[i] = snakeBody[i + 1]
            i += 1
        }
        snakeBody[i] = Vec2i(snakeBody[i].x + vec.x, snakeBody[i].y + vec.y)
    }

    fun move(dir: Direction, level: GameLevel) {
        when (dir){
            Direction.UP -> {
                moveBody(Vec2i(0, -1), level)
                headDirection = Direction.UP
                return
            }
            Direction.DOWN -> {
                moveBody(Vec2i(0, 1), level)
                headDirection = Direction.DOWN
                return
            }
            Direction.RIGHT -> {
                moveBody(Vec2i(1, 0), level)
                headDirection = Direction.RIGHT
                return
            }
            Direction.LEFT -> {
                moveBody(Vec2i(-1, 0), level)
                headDirection = Direction.LEFT
                return
            }
            else -> return
        }
    }

    fun render(batch: Batch) {
        val snakeSize = snakeBody.size
        val headRotation = util.headRotation[headDirection]!!
        val headOffset = util.headOffset[headDirection]!!
        for (i in 0..<snakeSize - 1)
            batch.draw(tailTexture, x = snakeBody[i].x * 16f, y = snakeBody[i].y * 16f, width = 16f, height = 16f)
        batch.draw(headTexture, x = snakeBody[snakeSize - 1].x * 16f + 16f * headOffset.x,
            y = snakeBody[snakeSize - 1].y * 16f + 16f * headOffset.y, width = 16f, height = 16f, rotation = headRotation)
    }

    fun getBody(): List<Vec2i>{
        return snakeBody
    }

    fun toPlayer(): Player {
        return Player(id, snakeBody, headDirection)
    }

    fun move(snakeBody: ArrayList<Vec2i>, headDirection: Direction) {
        this.snakeBody = snakeBody
        this.headDirection = headDirection
    }
}