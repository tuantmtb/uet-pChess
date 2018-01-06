package edu.uet.view.components

import edu.uet.GameDispatcher
import edu.uet.GameMaster
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.view.Styles
import javafx.application.Platform
import javafx.scene.Cursor
import javafx.scene.image.ImageView
import java.beans.PropertyChangeEvent

class ChessPieceUI(piece: ChessPiece, val game: GameMaster): ImageView(if (piece.chessSide == ChessSide.BLACK) "BN.png" else "WN.png") {
    private var listener: ((PropertyChangeEvent) -> Unit)? = null

    init {
        fitHeight = Styles.GRID_SIZE
        fitWidth = Styles.GRID_SIZE
        userData = piece
        bind()
    }

    override fun getUserData(): ChessPiece {
        return super.getUserData() as ChessPiece
    }

    private fun bind() {
        listener = GameDispatcher.on("TURN_SWITCHED", {
            Platform.runLater {
                cursor = if (it.newValue == userData.chessSide && !(game.pvc && game.ai!!.color == userData.chessSide)) Cursor.OPEN_HAND else Cursor.DEFAULT
            }
        })
    }

    fun unbind() {
        if (listener != null) {
            GameDispatcher.unbind("TURN_SWITCHED", listener!!)
            listener = null
        }
    }
}