package com.game.snake

import com.lehaine.littlekt.file.vfs.VfsFile
import com.lehaine.littlekt.file.vfs.readLDtkMapLoader

class Levels(vfsFile: VfsFile) {
    private val vfsFile: VfsFile
    private val levels: ArrayList<GameLevel> = ArrayList()

    init {
        this.vfsFile = vfsFile
    }

    suspend fun prepareLevels(){
        levels.add(GameLevel(vfsFile["world.ldtk"].readLDtkMapLoader().loadMap(loadAllLevels = true)["Level_0"]))
    }

    fun get(level: Int): GameLevel {
        return levels[level]
    }
}