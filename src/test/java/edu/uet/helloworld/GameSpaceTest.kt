package edu.uet.helloworld

import edu.uet.entity.*
import org.junit.Test
import kotlin.test.assertEquals

class GameSpaceTest {
    @Test
    fun generateATreeGivenRootHasOnly1Piece() {

        val board = ChessBoard(arrayListOf(
                ChessPiece(ChessPiece.Position( 1, 2), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position( 1, 0), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position( 2, 0), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position( 3, 0), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position( 4, 0), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position( 5, 0), ChessSide.WHITE),
                ChessPiece(ChessPiece.Position( 0, 5), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position( 1, 5), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position( 2, 5), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position( 3, 5), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position( 4, 5), ChessSide.BLACK),
                ChessPiece(ChessPiece.Position( 4, 3), ChessSide.BLACK)
        ), ChessBoard.Size(6, 6))

        val root = GameSpaceNode(board, 0, 0, ChessSide.WHITE)
        val gameSpace = GameSpace(root)

        gameSpace.generateWithDepth(6)


        var numberOfNodes = 0

        travel(gameSpace, onVisit = {node ->
            numberOfNodes++
        })

        assertEquals(4, numberOfNodes)
    }

    private fun travel(gameSpace: GameSpace, onVisit: (GameSpaceNode) -> Any) {
        visit(gameSpace.root, onVisit)
    }

    private fun visit(node: GameSpaceNode, onVisit: (GameSpaceNode) -> Any) {
        onVisit(node)

        node.children.forEach { child -> visit(child, onVisit) }
    }
}