package com.game.snake

import com.lehaine.littlekt.*
import com.lehaine.littlekt.async.KtScope
import com.lehaine.littlekt.graphics.Color
import com.lehaine.littlekt.graphics.g2d.SpriteBatch
import com.lehaine.littlekt.graphics.g2d.font.BitmapFontCache
import com.lehaine.littlekt.graphics.g2d.use
import com.lehaine.littlekt.graphics.gl.ClearBufferMask
import com.lehaine.littlekt.input.Key
import com.lehaine.littlekt.math.Vec2i
import com.lehaine.littlekt.util.viewport.ExtendViewport
import com.soywiz.klock.DateTime
import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.time.*


class SnakeGame(context: Context) : Game<Scene>(context) {
    val json = Json
    var session: WebSocketSession? = null

    override suspend fun Context.start() {

        val viewport = ExtendViewport(368, 256)
        val camera = viewport.camera
        val batch = SpriteBatch(this)
        var level: GameLevel? = null
        var snakeId = 0
        val levelId = 0
        var lastSnakeHead = Vec2i(0, 0)
        var fontCache: BitmapFontCache
        ResourceManager.createInstance(this) {
            level = GameLevel(ResourceManager.world["Level_$levelId"])
        }
        var snakes = HashMap<Int, SnakeEntity>()
        val client = HttpClient {
            install(WebSockets)
        }

        var isFirstPlayer = true

        val runLambda: suspend DefaultClientWebSocketSession.() -> Unit = websocket@{
            session?.send( json.encodeToString((Packet(snakes[snakeId]!!.toPlayer()))))
            val incomingJob = launch {
                try {
                    for (message in incoming) {
                        println("Received frame!")
                        when (message) {
                            is Frame.Binary -> TODO()
                            is Frame.Text -> {
                                val text = message.readText()
                                println("Got text: $text")

                                val packet = json.decodeFromStringOrNull<Packet>(text) ?: continue
                                println("Recieved packet: $packet")

                                val packetLatency = (DateTime.nowUnixLong() - packet
                                    .creationTimeMillis).toDuration(DurationUnit.MILLISECONDS)

                                println("Packet latency: $packetLatency")

                                when (packet.data) {
                                    is Player -> {
                                        context.logger.info { 1}
                                        val player = packet.data
                                        if (snakes.containsKey(player.id)) {
                                            context.logger.info { "1 " + player.id }
                                            snakes[player.id]!!.move(player.snakeBody, player.headDirection)
                                        } else {
                                            context.logger.info { "2 " + player.id }
                                            snakeId = player.id
                                            snakes[player.id] = SnakeEntity(player.id, level!!)
                                            if (isFirstPlayer) {
                                                println("Added first player!")
                                                session = this@websocket
                                                isFirstPlayer = false
                                            }
                                        }
                                    }
                                    is PlayerRemoved -> {
                                        val data = packet.data
                                        println("Player to remove: $data")
                                        snakes.remove(data.id)
                                    }
                                }
                            }
                            is Frame.Close -> TODO()
                            is Frame.Ping -> TODO()
                            is Frame.Pong -> TODO()
                            else -> TODO()
                        }
                    }
                } catch (e: Exception) {
                    println("Error while receiving messages: $e")
                }
            }
            incomingJob.join()
        }

        input.inputProcessor {
            onKeyDown {
                    key -> when(key) {
                    Key.ARROW_LEFT -> level?.let { snakes[snakeId]!!.move(Direction.LEFT, it) }
                    Key.ARROW_UP -> level?.let { snakes[snakeId]!!.move(Direction.UP, it) }
                    Key.ARROW_RIGHT -> level?.let { snakes[snakeId]!!.move(Direction.RIGHT, it) }
                    Key.ARROW_DOWN -> level?.let { snakes[snakeId]!!.move(Direction.DOWN, it) }
                    Key.A -> level?.let { snakes[snakeId]!!.move(Direction.LEFT, it) }
                    Key.D -> level?.let { snakes[snakeId]!!.move(Direction.RIGHT, it) }
                    Key.S -> level?.let { snakes[snakeId]!!.move(Direction.DOWN, it) }
                    Key.W -> level?.let { snakes[snakeId]!!.move(Direction.UP, it) }
                    else -> return@onKeyDown
            }
                logger.info { snakes[snakeId]!!.getBody() }
            }
        }

        onResize { width, height ->
            println("${graphics.width},${graphics.height}")
            viewport.update(width, height, context, true)
        }

        var f = false
        onRender {
            //wait untill resource manager load assets
            if(!ResourceManager.isReady()) {
                ResourceManager.update()
                return@onRender
            }
            //start session
            snakes = level!!.getSnakes()
            fontCache = BitmapFontCache(ResourceManager.pixelFont)
            if (!f){
                KtScope.launch {
                    client.ws(
                        method = HttpMethod.Get,
                        host = "127.0.0.1",
                        port = 8080,
                        path = "/game"
                    ) websocket@{
                        runLambda()
                    }
                }
                f = true
            }
            //then snake move, send Player packet with new body location
            if (snakes.containsKey(snakeId) && snakes[snakeId]!!.head != lastSnakeHead){
                KtScope.launch {
                    session?.send(json.encodeToString((Packet(snakes[snakeId]!!.toPlayer()))))
                }
                lastSnakeHead = snakes[snakeId]!!.head
            }
            //render
            gl.clearColor(Color.CLEAR)
            gl.clear(ClearBufferMask.COLOR_BUFFER_BIT)
            level?.let { it1 -> snakes[snakeId]?.update(it1) }

            fontCache.setText("", 0f, 5f, scaleX = 1f, scaleY = 1f)

            batch.use(camera.viewProjection) {
                level?.getLevel()?.render(it, camera)
                for (snake in snakes){
                    snake.value.render(it)
                    fontCache.addText("Snake ${snake.key + 1} score: ${snakes[snake.key]?.score}",
                        10f + 200f * snake.key, 5f, scaleX = 1.5f, scaleY = 1.5f)
                }
                fontCache.draw(it)

            }
        }

        onPostRender {
            if (input.isKeyJustPressed(Key.P)) {
                logger.info { stats }
            }
        }
    }
}

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            val textFrame = message as? Frame.Text ?: continue
            println(textFrame.readText())
        }
    } catch (e: Exception) {
        println("Error while receiving messages: " + e.message)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages() {
    while (true) {
        println("Type your message: ")
        val message = readln()
        println("Got message: $message")
        if (message.equals("exit", true)) return
        try {
            send(message)
        } catch (e: Exception) {
            println("Error while sending: " + e.message)
            return
        }
    }
}

inline fun <reified T> Json.decodeFromStringOrNull(string: String): T? {
    return try {
        decodeFromString(serializersModule.serializer(), string)
    } catch (e: Exception) {
        null
    }
}
