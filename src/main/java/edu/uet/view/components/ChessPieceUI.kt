package edu.uet.view.components

import edu.uet.GameDispatcher
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.view.Styles
import javafx.scene.Cursor
import javafx.scene.image.ImageView
import java.beans.PropertyChangeEvent

open class ChessPieceUI(piece: ChessPiece): ImageView(if (piece.chessSide == ChessSide.BLACK) "BN.png" else "WN.png") {
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
            cursor = if (it.newValue == userData.chessSide) Cursor.OPEN_HAND else Cursor.DEFAULT
        })
    }

    fun unbind() {
        if (listener != null) {
            GameDispatcher.unbind("TURN_SWITCHED", listener!!)
            listener = null
        }
    }
}