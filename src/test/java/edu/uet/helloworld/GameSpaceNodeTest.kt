package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.entity.GameSpaceNode
import org.junit.Test
import kotlin.test.assertEquals

class GameSpaceNodeTest {
    @Test
    fun evaluateCorrectly() {
        val pieces = arrayListOf<ChessPiece>(
                ChessPiece(ChessPiece.Position(1,1), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(2,2), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(3,3), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(4,4), ChessSide.BLACK)
        )

        val gameSpaceNode = GameSpaceNode(ChessBoard(pieces))

        val evaluatedValue = gameSpaceNode.evaluateForSide(ChessSide.BLACK, 10, 2)

        assertEquals(16, evaluatedValue)
    }
}