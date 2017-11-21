package edu.uet.view

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App
import tornadofx.FX
import tornadofx.reloadViewsOnFocus

class ChessApplication : App(MainMenuView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        with(FX.primaryStage) {
            icons += Image("knight_black.png")
            resizableProperty().set(false)
        }
        reloadViewsOnFocus()
    }
}

