package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import org.junit.Test
import kotlin.test.assertEquals

class ChessBoardTest {

    @Test
    fun getBlockedPositionForPieceCorrectly() {
        val piece1 = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)
        val piece2 = ChessPiece(ChessPiece.Position(3, 4), ChessSide.WHITE)
        val piece3 = ChessPiece(ChessPiece.Position(3,5), ChessSide.WHITE)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2, piece3))

        val blockedPositions = chessBoard.getBlockedPositionForPiece(piece1)

        assertEquals(1, blockedPositions.size)
        assertEquals(piece2.position.x, blockedPositions[0].position.x)
        assertEquals(piece2.position.y, blockedPositions[0].position.y)
    }

    @Test
    fun getPossibleNextPositionForPieceCorrectly() {
        val piece1 = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)
        val piece2 = ChessPiece(ChessPiece.Position(3, 4), ChessSide.WHITE)
        val piece3 = ChessPiece(ChessPiece.Position(3,5), ChessSide.WHITE)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2, piece3))

        val possibleNextPositions = chessBoard.getPossibleNextPositionForPiece(piece1)

        assertEquals(6, possibleNextPositions.size)
    }
}