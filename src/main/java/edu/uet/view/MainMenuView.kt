package edu.uet.view

import edu.uet.GameDispatcher
import edu.uet.GameMaster
import javafx.scene.layout.Pane

class MainMenuView : BaseView() {
    override val root: Pane by fxml("/MainMenu.fxml")

    fun pvc() {
        find(this.javaClass).replaceWith(PVCGameView::class, sizeToScene = true, centerOnScreen = true)
    }

    fun pvp() {
        find(this.javaClass).replaceWith(PVPGameView::class, sizeToScene = true, centerOnScreen = true)
        GameMaster.newGame()
    }
}