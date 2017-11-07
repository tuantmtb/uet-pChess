package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.entity.GameSpaceNode
import org.junit.Test
import tornadofx.children
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GameSpaceNodeTest {
    @Test
    fun evaluateCorrectly() {
        val pieces = arrayListOf(
                ChessPiece(ChessPiece.Position(1,1), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(2,2), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position(3,3), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(4,4), ChessSide.BLACK)
        )

        val gameSpaceNode = GameSpaceNode(ChessBoard(pieces), 10, 12, ChessSide.BLACK)

        val evaluatedValue = gameSpaceNode.evaluate(2)

        assertEquals(16, evaluatedValue)
    }

    @Test
    fun generateChildrenCorrectly() {
        val pieces = arrayListOf(
                ChessPiece(ChessPiece.Position(2,2), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position(4,3), ChessSide.BLACK)
        )

        val chessBoard = ChessBoard(pieces, ChessBoard.Size(6, 6))

        val node = GameSpaceNode(chessBoard, 0, 0, ChessSide.WHITE)

        node.generateChildren()

        assertEquals(8, node.children.size)

        val found = node.children.find { c ->
            c.whiteSideGameScore == 1
        }

        assertNotNull(found)
    }
}