package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import org.junit.Test
import kotlin.test.assertEquals

class ChessPieceTest {

    @Test
    fun getPossiblePositionsWithoutBlocking() {
        val piece = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)

        val generatedPositions = piece.getPossibleNextPositions(false, false, false, false, ChessBoard.Size(6, 6))

        assertEquals(8, generatedPositions.size)
    }

    @Test
    fun getPossiblePositionsWithTopBlocking() {
        val piece = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)

        val generatedPositions = piece.getPossibleNextPositions(true, false, false, false, ChessBoard.Size(6, 6))

        assertEquals(6, generatedPositions.size)
    }

    @Test
    fun getPossiblePositionsWhileOnRightEdgeAndTopBlocking() {
        val piece = ChessPiece(ChessPiece.Position(5, 3), ChessSide.BLACK)

        val generatedPositions = piece.getPossibleNextPositions(true, false, false, false, ChessBoard.Size(6, 6))

        assertEquals(3, generatedPositions.size)
    }
}