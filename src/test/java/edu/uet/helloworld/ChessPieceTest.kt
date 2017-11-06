package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.entity.ChessBoard.Size
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun getBlockPositions() {
        val piece = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)

        val generatedBlockedPositions = piece.getPossibleBlockedPositions(ChessBoard.Size(6, 6))

        assertEquals(3, generatedBlockedPositions[0].position.x)
        assertEquals(4, generatedBlockedPositions[0].position.y)
        assertEquals(2, generatedBlockedPositions[3].position.x)
        assertEquals(3, generatedBlockedPositions[3].position.y)
    }

    @Test
    fun getBlockPositionWhileOnTopRightCorner() {
        val piece = ChessPiece(ChessPiece.Position(5, 5), ChessSide.BLACK)

        val generatedBlockPositions = piece.getPossibleBlockedPositions(ChessBoard.Size(6, 6))

        assertEquals(2, generatedBlockPositions.size)
    }

    @Test
    fun equalsOther() {
        val piece = ChessPiece(ChessPiece.Position(5, 5), ChessSide.BLACK)
        val other = ChessPiece(ChessPiece.Position(5, 5), ChessSide.BLACK)

        assertTrue { piece.equals(other) }
    }
}