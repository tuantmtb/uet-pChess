package edu.uet.view.components

import edu.uet.GameDispatcher
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.view.Styles
import javafx.application.Platform
import javafx.scene.Cursor
import javafx.scene.image.ImageView

open class ChessPieceUI(piece: ChessPiece): ImageView(if (piece.chessSide == ChessSide.BLACK) "BN.png" else "WN.png") {
    init {
        fitHeight = Styles.GRID_SIZE
        fitWidth = Styles.GRID_SIZE
        userData = piece

        GameDispatcher.listen("TURN_SWITCHED", {
            cursor = if (it.newValue == userData.chessSide) Cursor.OPEN_HAND else Cursor.DEFAULT
        })
    }

    override fun getUserData(): ChessPiece {
        return super.getUserData() as ChessPiece
    }
}