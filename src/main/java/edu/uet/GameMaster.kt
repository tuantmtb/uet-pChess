package edu.uet

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide

/**
 * Created by tuantmtb on 10/31/17.
 */
class GameMaster {
    val board: ChessBoard
    var turn : ChessSide = ChessSide.WHITE

    init {
        board = ChessBoard(arrayListOf(
                ChessPiece(ChessPiece.Position(0, 0), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(0, 1), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(0, 2), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(0, 3), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(0, 4), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(0, 5), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(5, 0), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(5, 1), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(5, 2), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(5, 3), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(5, 4), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(5, 5), ChessSide.WHITE)
        ), ChessBoard.Size(6, 6))
    }

    fun nextTurn() {
        turn = if (turn == ChessSide.WHITE) {
            ChessSide.BLACK
        } else {
            ChessSide.WHITE
        }
    }
}