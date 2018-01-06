package edu.uet.view

import edu.uet.ChessConfig
import edu.uet.GameDispatcher
import edu.uet.GameMaster
import edu.uet.entity.ChessSide
import edu.uet.view.components.ChessBoardUI
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.*
import javafx.scene.paint.Paint
import tornadofx.hide
import tornadofx.information
import tornadofx.show

open class GameView(val pvc: Boolean) : BaseView() {
    final override val root: BorderPane by fxml("/Game.fxml")

    private var game = GameMaster()

    private val hbox = root.center as HBox
    private val boardUI = ChessBoardUI(game)
    private val bName: Label by fxid("bName")
    private val wName: Label by fxid("wName")
    private val bPoint: Label by fxid("bPoint")
    private val wPoint: Label by fxid("wPoint")
    private val bCDSection: Pane by fxid("bCDSection")
    private val bCDLabel: Label by fxid("bCDLabel")
    private val bCDProgress: ProgressBar by fxid("bCDProgress")
    private val wCDSection: Pane by fxid("wCDSection")
    private val wCDLabel: Label by fxid("wCDLabel")
    private val wCDProgress: ProgressBar by fxid("wCDProgress")
    private val rightSideBar: VBox by fxid("rightSideBar")

    init {
        rightSideBar.prefHeight = boardUI.prefHeight
        rightSideBar.children.map { it as VBox }.forEach { it.prefHeight = rightSideBar.prefHeight/2 }

        hbox.children.add(0, boardUI)

        bind()
        newGame()
    }

    private fun bind() {
        GameDispatcher.on("WHITE_POINT_CHANGED", { Platform.runLater { wPoint.text = it.newValue.toString() } })
        GameDispatcher.on("BLACK_POINT_CHANGED", { Platform.runLater { bPoint.text = it.newValue.toString() } })
        GameDispatcher.on("TURN_SWITCHED", { Platform.runLater { updateTurn() } })
        GameDispatcher.on("WINNER", {
            information("Người chiến thắng: " + game.winner()!!.name)
        })
        GameDispatcher.on("COUNT_DOWN_TICK", {
            Platform.runLater {
                val value = it.newValue as Int
                val progress = value.toDouble() / ChessConfig.COUNT_DOWN
                if (game.turn == ChessSide.WHITE) {
                    wCDLabel.text = value.toString()
                    wCDProgress.progress = progress
                } else {
                    bCDLabel.text = value.toString()
                    bCDProgress.progress = progress
                }
            }
        })
    }

    private fun updateTurn() {
        if (game.turn == ChessSide.BLACK) {
            bName.textFill = Paint.valueOf("red")
            wName.textFill = Paint.valueOf("black")
            bCDSection.show()
            wCDSection.hide()
        } else {
            bName.textFill = Paint.valueOf("black")
            wName.textFill = Paint.valueOf("red")
            bCDSection.hide()
            wCDSection.show()
        }
    }

    fun newGame() {
        game.newGame(pvc)
    }

    fun menuClose() {
        Platform.exit()
    }
}