package com.game.snake

import com.lehaine.littlekt.*
import com.lehaine.littlekt.file.vfs.readLDtkMapLoader
import com.lehaine.littlekt.file.vfs.readTexture
import com.lehaine.littlekt.graphics.Color
import com.lehaine.littlekt.graphics.Texture
import com.lehaine.littlekt.graphics.g2d.SpriteBatch
import com.lehaine.littlekt.graphics.g2d.use
import com.lehaine.littlekt.graphics.gl.ClearBufferMask
import com.lehaine.littlekt.input.Key
import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.util.viewport.ExtendViewport

class SnakeGame(context: Context) : com.lehaine.littlekt.Game<Scene>(context) {

    override suspend fun Context.start() {
        val viewport = ExtendViewport(368, 256)
        val camera = viewport.camera
        val batch = SpriteBatch(this)

        val background: Texture = resourcesVfs["back.png"].readTexture()
        val snakeTexture: Texture = resourcesVfs["snake.png"].readTexture()
        val snakeHead: Texture = resourcesVfs["head.png"].readTexture()
        val mapLoader = resourcesVfs["level1.ldtk"].readLDtkMapLoader()
        val ldtkLevel = mapLoader.loadLevel(levelIdx = 0)
        val level = GameLevel(ldtkLevel)

        val snake = SnakeEntity(arrayListOf(Vec2i(0, 3), Vec2i(1, 3),
            Vec2i(2, 3), Vec2i(3, 3), Vec2i(4, 3)),
                                snakeTexture, snakeHead, level)


        input.inputProcessor {
            onKeyDown {
                key -> when(key){
                    Key.ARROW_LEFT -> snake.move(Direction.LEFT)
                    Key.ARROW_UP -> snake.move(Direction.UP)
                    Key.ARROW_RIGHT -> snake.move(Direction.RIGHT)
                    Key.ARROW_DOWN -> snake.move(Direction.DOWN)
                    Key.A -> snake.move(Direction.LEFT)
                    Key.D -> snake.move(Direction.RIGHT)
                    Key.S -> snake.move(Direction.DOWN)
                    Key.W -> snake.move(Direction.UP)
                    else -> return@onKeyDown
                }
                logger.info { snake.getBody() }
            }
        }

        onResize { width, height ->
            println("${graphics.width},${graphics.height}")
            viewport.update(width, height, context, true)
        }

        onRender {
            gl.clearColor(Color.CLEAR)
            gl.clear(ClearBufferMask.COLOR_BUFFER_BIT)
            snake.update()
            batch.use(camera.viewProjection) {
                it.draw(background,  x = 0f, y = 0f)
                ldtkLevel.render(it, camera)
                snake.render(it)
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
