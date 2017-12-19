package edu.uet.view

import javafx.scene.layout.Pane

class MainMenuView : BaseView() {
    override val root: Pane by fxml("/MainMenu.fxml")

    fun pvc() {
        find(MainMenuView::class).replaceWith(PVCGameView::class, sizeToScene = true, centerOnScreen = true)
    }

    fun pvp() {
        find(MainMenuView::class).replaceWith(PVPGameView::class, sizeToScene = true, centerOnScreen = true)
    }
}