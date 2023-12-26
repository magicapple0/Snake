package com.game.snake

import com.lehaine.littlekt.AssetProvider
import com.lehaine.littlekt.Context
import com.lehaine.littlekt.Disposable
import com.lehaine.littlekt.file.ldtk.LDtkMapLoader
import com.lehaine.littlekt.graphics.Texture
import com.lehaine.littlekt.graphics.g2d.tilemap.ldtk.LDtkWorld
import kotlin.jvm.Volatile

class ResourceManager private constructor(context: Context) : Disposable {
    private val assets = AssetProvider(context)
    private val tailTexture: Texture by assets.load<Texture>(context.resourcesVfs["snake.png"])
    private val headTexture: Texture by assets.load<Texture>(context.resourcesVfs["head.png"])
    private val mapLoader: LDtkMapLoader by assets.load(context.resourcesVfs["world.ldtk"])
    private val world: LDtkWorld by assets.prepare { mapLoader.loadMap(false) }

    override fun dispose() {
    }

    companion object {
        @Volatile
        private var instance: ResourceManager? = null
        private val INSTANCE: ResourceManager get() = instance ?: error("Instance has not been created!")

        val mapLoader: LDtkMapLoader get() = INSTANCE.mapLoader
        val tailTexture: Texture get() = INSTANCE.tailTexture
        val headTexture: Texture get() = INSTANCE.headTexture
        val world: LDtkWorld get() = INSTANCE.world

        fun createInstance(context: Context, onLoad: () -> Unit): ResourceManager {
            check(instance == null) { "Instance already created!" }
            val newInstance = ResourceManager(context)
            instance = newInstance
            INSTANCE.assets.onFullyLoaded = onLoad
//            context.onRender {
//                while(!INSTANCE.assets.prepared) {
//                    INSTANCE.assets.update()
//                }
//            }
            context.onRender { INSTANCE.assets.update() }
            return newInstance
        }

        fun dispose() {
            instance?.dispose()
        }

        fun update() {
            instance?.assets?.update()
        }

        fun isReady(): Boolean{
            return INSTANCE.assets.fullyLoaded
        }
    }
}