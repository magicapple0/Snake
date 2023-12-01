package com.game.snake

import com.lehaine.littlekt.graphics.g2d.tilemap.ldtk.*
import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.math.geom.Point

class GameLevel(level: LDtkLevel){
    val level: LDtkLevel
    val snakesSpawn: ArrayList<ArrayList<Vec2i>> = ArrayList()
    val collisionMask: LDtkIntGridLayer
    val tiles: LDtkLayer

    init {
        this.level = level
        this.collisionMask = level["Colliders"] as LDtkIntGridLayer
        tiles = level["Tiles"]
        prepareSnakes()
    }

    private fun prepareSnakes(){
        val snake = level.entities("Snake")[0].fieldArray<Point>("Body").values
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


    fun isColliding(point: Vec2i): Boolean{
        var x = collisionMask.intGrid
        return !collisionMask.isCoordValid(point.x, point.y)
                || collisionMask.getInt(point.x, point.y) == 1
    }
    fun getSnakeSpawn(snake: Int): ArrayList<Vec2i>{
        return snakesSpawn[snake]
    }
}