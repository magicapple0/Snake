package com.game.snake

import com.lehaine.littlekt.file.vfs.readLDtkMapLoader
import com.lehaine.littlekt.graphics.Texture
import com.lehaine.littlekt.graphics.g2d.Batch
import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.math.geom.Angle

class SnakeEntity(id: Int, levelId: Int){//, snakeBody: ArrayList<Vec2i>, tailTexture: Texture, headTexture: Texture, level: GameLevel) {
    private val id: Int
    private val tailTexture: Texture
    private val headTexture: Texture
    //from end to head
    private var snakeBody: ArrayList<Vec2i> = arrayListOf()
    private var headRotation: Angle = Angle.fromDegrees(270)
    //head rotation point is left bottom point. So rotation breaks head
    private var headOffset: Vec2i = Vec2i(0, 1)
    private val level: GameLevel

    init{
        this.id = id
        this.level = GameLevel(ResourceManager.world["Level_0"])
        this.tailTexture = ResourceManager.tailTexture
        this.headTexture = ResourceManager.headTexture
        this.snakeBody = level.getSnakeSpawn(id)
    }

    fun update() {
        snakeBody.forEach {
            if (level.isColliding(Vec2i(it.x, it.y + 1)))
                return
        }
        for (i in 0..<snakeBody.size)
            snakeBody[i] = Vec2i(snakeBody[i].x, snakeBody[i].y + 1)
    }

    private fun moveBody(vec: Vec2i){
        if ((snakeBody[snakeBody.size - 1].y + vec.y == snakeBody[snakeBody.size - 2].y &&
            snakeBody[snakeBody.size - 1].x + vec.x == snakeBody[snakeBody.size - 2].x) ||
            level.isColliding(Vec2i(snakeBody[snakeBody.size - 1].x + vec.x, snakeBody[snakeBody.size - 1].y + vec.y)) ||
            snakeBody.contains(Vec2i(snakeBody[snakeBody.size - 1].x + vec.x, snakeBody[snakeBody.size - 1].y + vec.y))                    )
            return
        var i = 0
        while(i < snakeBody.size - 1) {
            snakeBody[i] = snakeBody[i + 1]
            i += 1
        }
        snakeBody[i] = Vec2i(snakeBody[i].x + vec.x, snakeBody[i].y + vec.y)
    }

    fun move(dir: Direction) {
        when (dir){
            Direction.UP -> {
                moveBody(Vec2i(0, -1))
                headRotation = Angle.fromDegrees(180)
                headOffset = Vec2i(1, 1)
                return
            }
            Direction.DOWN -> {
                moveBody(Vec2i(0, 1))
                headRotation = Angle.fromDegrees(0)
                headOffset = Vec2i(0, 0)
                return
            }
            Direction.RIGHT -> {
                moveBody(Vec2i(1, 0))
                headRotation = Angle.fromDegrees(270)
                headOffset = Vec2i(0, 1)
                return
            }
            Direction.LEFT -> {
                moveBody(Vec2i(-1, 0))
                headRotation = Angle.fromDegrees(90)
                headOffset = Vec2i(1, 0)
                return
            }
            else -> return
        }
    }

    fun render(batch: Batch) {
        val snakeSize = snakeBody.size
        for (i in 0..<snakeSize - 1)
            batch.draw(tailTexture, x = snakeBody[i].x * 16f, y = snakeBody[i].y * 16f, width = 16f, height = 16f)
        batch.draw(headTexture, x = snakeBody[snakeSize - 1].x * 16f + 16f * headOffset.x,
            y = snakeBody[snakeSize - 1].y * 16f + 16f * headOffset.y, width = 16f, height = 16f, rotation = headRotation)
    }

    fun getBody(): List<Vec2i>{
        return snakeBody
    }
}