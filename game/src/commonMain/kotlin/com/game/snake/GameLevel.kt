package com.game.snake

import com.lehaine.littlekt.graphics.g2d.tilemap.ldtk.*
import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.math.geom.Point

class GameLevel(level: LDtkLevel){
    private val level: LDtkLevel
    private val snakesSpawn: ArrayList<ArrayList<Vec2i>> = ArrayList()
    private val collisionMask: LDtkIntGridLayer
    private val tiles: LDtkLayer
    private val snakes = HashMap<Int, SnakeEntity>()
    var lastApple: Vec2i?
    val apples = ArrayList<Vec2i>()

    init {
        this.level = level
        lastApple = null
        this.collisionMask = level["Colliders"] as LDtkIntGridLayer
        tiles = level["Tiles"]
        prepareSnakes()
    }

    private fun prepareSnakes(){
        var snake = level.entities("Snake")[0].fieldArray<Point>("Body").values
        snakesSpawn.add(convertLdtkPointToVec2i(snake))
        snake = level.entities("Snake")[1].fieldArray<Point>("Body").values
        snakesSpawn.add(convertLdtkPointToVec2i(snake))
    }

    private fun convertLdtkPointToVec2i(pointArray: List<LDtkField<Point>>): ArrayList<Vec2i>{
        val vecList: ArrayList<Vec2i> = ArrayList()
        pointArray.forEach {
            val point = (it as LDtkValueField<Point>).value
            vecList.add(Vec2i(point.x.toInt(), point.y.toInt()))
        }
        return vecList.reversed() as ArrayList<Vec2i>
    }

    fun isColliding(point: Vec2i, id: Int): Boolean{
        return isCollidingSnakes(point, id) || isCollidingWalls(point)
    }

    private fun isCollidingSnakes(point:Vec2i, id: Int): Boolean{
        snakes.forEach{ it ->
            if (it.key != id)
                it.value.getBody().forEach {
                    if (it.y == point.y && it.x == point.x)
                        return true
                }
        }
        return false
    }

    fun isCollidingApple(point:Vec2i): Boolean{
        apples.forEach {
            if (it.x == point.x && it.y == point.y){
                return true
            }
        }
        return false
    }

    private fun isCollidingWalls(point: Vec2i): Boolean{
        return !collisionMask.isCoordValid(point.x, point.y)
                || collisionMask.getInt(point.x, point.y) == 1
    }

    fun getSnakeSpawn(snake: Int): ArrayList<Vec2i>{
        return snakesSpawn[snake]
    }

    fun getLevel(): LDtkLevel{
        return level
    }

    fun getSnakes(): HashMap<Int, SnakeEntity>{
        return snakes
    }

    fun addApple(){
        var x = (0..collisionMask.gridWidth - 1).random()
        var y = (5..collisionMask.gridHeight - 1).random()
        while (isColliding(Vec2i(x,y), 0)){
            x = (0..collisionMask.gridWidth).random()
            y = (0..collisionMask.gridHeight).random()
        }
        apples.add(Vec2i(x,y))
        lastApple = Vec2i(x,y)
    }

    fun removeApple(point: Vec2i){
        var apple = Vec2i(0,0)
        apples.forEach {
            if (it.x == point.x && it.y == point.y)
                apple = it
        }
        apples.remove(apple)
    }
}