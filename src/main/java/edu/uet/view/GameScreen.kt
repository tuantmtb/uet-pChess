package edu.uet.view

import javafx.scene.control.SplitPane
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import tornadofx.*

class GameScreen: View() {
    override val root: SplitPane by fxml("/GameScreen.fxml")
    init {
        this.titleProperty.set("Cờ điểm UET")
    }

    fun selectHorse(event: MouseEvent) {
        val image = event.target as ImageView
        val pane = image.parent as Pane
        println(pane)
    }
}