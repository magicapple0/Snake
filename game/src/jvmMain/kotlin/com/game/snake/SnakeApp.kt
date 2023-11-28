package com.game.snake

import com.lehaine.littlekt.createLittleKtApp

fun main() {
    createLittleKtApp {
        width = 3680/3
        height = 2560/3
        vSync = true
        title = "Snake"
    }.start {
        SnakeGame(it)
    }
}