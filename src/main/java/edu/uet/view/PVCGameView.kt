package edu.uet.view

import edu.uet.GameDispatcher
import edu.uet.GameMaster
import edu.uet.entity.ChessSide
import javafx.scene.layout.Pane

class PVCGameView : BaseView() {
    override val root: Pane by fxml("/PVCMenu.fxml")

    fun pvcw() {
        find(this.javaClass).replaceWith(PVCWGameView::class, sizeToScene = true, centerOnScreen = true)
        GameMaster.newGame(true, ChessSide.WHITE)
    }

    fun pvcb() {
        find(this.javaClass).replaceWith(PVCBGameView::class, sizeToScene = true, centerOnScreen = true)
        GameMaster.newGame(true, ChessSide.BLACK)
    }

    fun back() {
        find(this.javaClass).replaceWith(MainMenuView::class, sizeToScene = true, centerOnScreen = true)
    }
}