package com.game.snake

import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.math.geom.Angle


class Util {
    var headRotation = hashMapOf(
        Direction.RIGHT to Angle.fromDegrees(270), //right
        Direction.LEFT to Angle.fromDegrees(90), //left
        Direction.DOWN to Angle.fromDegrees(0), //down
        Direction.UP to Angle.fromDegrees(180)
    )//up

    var headOffset = hashMapOf(
        Direction.RIGHT to Vec2i(0, 1), //right
        Direction.LEFT to Vec2i(1, 0), //left
        Direction.DOWN to Vec2i(0, 0), //down
        Direction.UP to Vec2i(1, 1)
    )//up
}