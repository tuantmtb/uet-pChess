package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import org.junit.Test
import kotlin.test.assertEquals

class PositionTest {

    /**
     * == Black side == (White earns)
     *       2 1
     *
     *
     *
     *       1 2
     * == White side == (Black earns)
     *
     */
    @Test
    fun checkPointPositionCorrectly() {
        val size = ChessBoard.Size(6, 6)

        assertEquals(0, ChessPiece.Position(2, 5).earnedPointsForSide(ChessSide.BLACK, size))
        assertEquals(0, ChessPiece.Position(3, 5).earnedPointsForSide(ChessSide.BLACK, size))
        assertEquals(1, ChessPiece.Position(2, 0).earnedPointsForSide(ChessSide.BLACK, size))
        assertEquals(2, ChessPiece.Position(3, 0).earnedPointsForSide(ChessSide.BLACK, size))
        assertEquals(1, ChessPiece.Position(3, 5).earnedPointsForSide(ChessSide.WHITE, size))
        assertEquals(2, ChessPiece.Position(2, 5).earnedPointsForSide(ChessSide.WHITE, size))
        assertEquals(0, ChessPiece.Position(2, 0).earnedPointsForSide(ChessSide.WHITE, size))
        assertEquals(0, ChessPiece.Position(3, 0).earnedPointsForSide(ChessSide.WHITE, size))
    }
}