package Assets.Scenes

import korlibs.image.color.*
import korlibs.korge.scene.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*

class Menu : Scene() {
    override suspend fun SContainer.sceneMain() {
        uiVerticalStack(padding = 8.0) {
            uiButton("Start").also {
                it.width = 190.0
                it.bgColorOut = Colors["#a19afc"]
                it.bgColorOver = Colors["#01cecb"]
                it.textSize = 25.0
                it.background.radius = RectCorners(8f, 8f, 8f, 8f)
            }
            uiButton("Connect").also {
                it.width = 190.0
                it.bgColorOut = Colors["#a19afc"]
                it.bgColorOver = Colors["#01cecb"]
                it.textSize = 25.0
                it.background.radius = RectCorners(8f, 8f, 8f, 8f)
            }
            uiButton("Settings").also {
                it.width = 190.0
                it.bgColorOut = Colors["#a19afc"]
                it.bgColorOver = Colors["#01cecb"]
                it.textSize = 25.0
                it.background.radius = RectCorners(8f, 8f, 8f, 8f)
            }
        }


    }
}
