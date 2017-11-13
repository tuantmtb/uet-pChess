package edu.uet.view

import javafx.scene.layout.Pane

class MainMenuView : BaseView() {
    override val root: Pane by fxml("/MainMenu.fxml")

    fun start() {
        find(MainMenuView::class).replaceWith(GameView::class, sizeToScene = true, centerOnScreen = true)
    }
}