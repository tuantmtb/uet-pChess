package edu.uet.view.components

import edu.uet.ChessConfig
import edu.uet.GameDispatcher
import edu.uet.GameMaster
import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.view.Styles
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import tornadofx.add

class ChessPositionUI(row: Int, col: Int, game: GameMaster): AnchorPane() {
    class EarnedPointsPositionLayer(wEarn: Int, bEarn: Int): ImageView(
            when (wEarn) {
                ChessConfig.POINT_1 -> "W1.png"
                ChessConfig.POINT_2 -> "W2.png"
                else -> when (bEarn) {
                    ChessConfig.POINT_1 -> "B1.png"
                    ChessConfig.POINT_2 -> "B2.png"
                    else -> null // won't happen
                }
            }
    ) {
        init {
            fitWidth = Styles.GRID_SIZE
            fitHeight = Styles.GRID_SIZE
            isMouseTransparent = true
        }
    }

    init {
        prefHeight = Styles.GRID_SIZE
        prefWidth = Styles.GRID_SIZE
        userData = ChessPiece.Position(game.board.size.width - 1 - col, game.board.size.height - 1 - row)
        val color = if ((row + col) % 2 == 0) Styles.THEME.BACKGROUND_DARK_COLOR else Styles.THEME.BACKGROUND_LIGHT_COLOR
        style = "-fx-background-color: $color;"

        val wEarn = userData.earnedPointsForSide(ChessSide.WHITE, game.board.size)
        val bEarn = userData.earnedPointsForSide(ChessSide.BLACK, game.board.size)
        if (wEarn != 0 || bEarn != 0) {
            add(EarnedPointsPositionLayer(wEarn, bEarn))
        }

        GameDispatcher.on("PIECE_DIED", {
            if (pieceUI?.userData == it.oldValue) {
                removePiece()
            }
        })
    }

    override fun getUserData(): ChessPiece.Position {
        return super.getUserData() as ChessPiece.Position
    }

    fun removePiece(): ChessPieceUI? {
        val oldValue = pieceUI
        pieceUI = null
        return oldValue
    }

    fun movePieceTo(posUI: ChessPositionUI) {
        posUI.pieceUI = removePiece()
    }

    var pieceUI: ChessPieceUI? = null
        set(value) {
            if (field != null) {
                children.remove(field)
                field?.unbind()
            }
            if (value != null) {
                add(value)
            }
            field = value
        }

    fun markAsValidMove() {
        effect = ValidMoveEffect()
    }

    fun unmark() {
        effect = null
    }
}