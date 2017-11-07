package edu.uet.view

import javafx.scene.layout.Pane
import tornadofx.View

class MainMenu : View() {
    override val root: Pane by fxml("/MainMenu.fxml")
    init {
        this.titleProperty.set("Cờ điểm UET")
    }

    fun start() {
        find(MainMenu::class).replaceWith(GameScreen::class, sizeToScene = true, centerOnScreen = true)
    }
}