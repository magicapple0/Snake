package com.game.snake

import com.lehaine.littlekt.*
import com.lehaine.littlekt.async.KtScope
import com.lehaine.littlekt.graphics.Color
import com.lehaine.littlekt.graphics.g2d.SpriteBatch
import com.lehaine.littlekt.graphics.g2d.use
import com.lehaine.littlekt.graphics.gl.ClearBufferMask
import com.lehaine.littlekt.input.Key
import com.lehaine.littlekt.util.viewport.ExtendViewport
import kotlinx.coroutines.launch

class SnakeGame(context: Context) : Game<Scene>(context) {

    override suspend fun Context.start() {
        val viewport = ExtendViewport(368, 256)
        val camera = viewport.camera
        val levelId = 0
        val batch = SpriteBatch(this)
        var level: GameLevel? = null
        var snake: SnakeEntity? = null
        ResourceManager.createInstance(this){
            KtScope.launch {
                level = GameLevel(ResourceManager.world["Level_0"])
                snake = SnakeEntity(0, levelId)
            }
        }

        input.inputProcessor {
            onKeyDown {
                    key -> when(key) {
                    Key.ARROW_LEFT -> snake?.move(Direction.LEFT)
                    Key.ARROW_UP -> snake?.move(Direction.UP)
                    Key.ARROW_RIGHT -> snake?.move(Direction.RIGHT)
                    Key.ARROW_DOWN -> snake?.move(Direction.DOWN)
                    Key.A -> snake?.move(Direction.LEFT)
                    Key.D -> snake?.move(Direction.RIGHT)
                    Key.S -> snake?.move(Direction.DOWN)
                    Key.W -> snake?.move(Direction.UP)
                    else -> return@onKeyDown
            }
                logger.info { snake?.getBody() }
            }
        }



        onResize { width, height ->
            println("${graphics.width},${graphics.height}")
            viewport.update(width, height, context, true)
        }

        onRender {
            gl.clearColor(Color.CLEAR)
            gl.clear(ClearBufferMask.COLOR_BUFFER_BIT)
            snake?.update()
            batch.use(camera.viewProjection) {
                level?.level?.render(it, camera)
                snake?.render(it)
            }
        }

        onPostRender {
            if (input.isKeyJustPressed(Key.P)) {
                logger.info { stats }
            }
        }

        onDispose {
        }
    }
}
