package edu.uet.helloworld

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChessBoardTest {

    @Test
    fun getBlockedPositionForPieceCorrectly() {
        val piece1 = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)
        val piece2 = ChessPiece(ChessPiece.Position(3, 4), ChessSide.WHITE)
        val piece3 = ChessPiece(ChessPiece.Position(3, 5), ChessSide.WHITE)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2, piece3))

        val blockedPositions = chessBoard.getBlockedPositionForPiece(piece1)

        assertEquals(1, blockedPositions.size)
        assertEquals(piece2.position.x, blockedPositions[0].position.x)
        assertEquals(piece2.position.y, blockedPositions[0].position.y)
    }

    @Test
    fun getPossibleNextPositionForPieceCorrectly() {
        val piece1 = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)
        val piece2 = ChessPiece(ChessPiece.Position(3, 4), ChessSide.WHITE) // block
        val piece3 = ChessPiece(ChessPiece.Position(3, 5), ChessSide.WHITE) // non-block
        val piece4 = ChessPiece(ChessPiece.Position(5, 4), ChessSide.BLACK) // ally

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2, piece3, piece4))

        val possibleNextPositions = chessBoard.getPossibleNextPositionForPiece(piece1)

        assertEquals(5, possibleNextPositions.size)
    }

    @Test
    fun moveAPieceAndEatAnEnemy() {
        val piece1 = ChessPiece(ChessPiece.Position(3, 3), ChessSide.BLACK)
        val piece2 = ChessPiece(ChessPiece.Position(4, 5), ChessSide.WHITE)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2), ChessBoard.Size(6, 6))

        var blackScore = 0
        var whiteScore = 0

        chessBoard.move(piece1, ChessPiece.Position(4, 5), onPieceDies = { piece ->
            assertEquals(piece, piece2)
        }, onPointsGivenForSide = { side, points ->

            if (side == ChessSide.BLACK) {
                blackScore += points
            } else {
                whiteScore += points
            }

        }, onPieceMoves = null)

        assertEquals(0, whiteScore)
        assertEquals(1, blackScore)
    }

    @Test
    fun moveAPieceToPointPosition() {

        val piece = ChessPiece(ChessPiece.Position(2, 3), ChessSide.WHITE)

        val chessBoard = ChessBoard(arrayListOf(piece), ChessBoard.Size(6, 6))

        var whiteScore = 0

        chessBoard.move(piece, ChessPiece.Position(3, 5), onPieceDies = { piece ->
            throw Exception("This should not happen")
        }, onPointsGivenForSide = { side, points ->
            if (side == ChessSide.WHITE) {
                whiteScore += points
            }
        }, onPieceMoves = { _ ->

        })

        assertTrue { ChessPiece.Position(5, 0).equals(piece.position) }
        assertEquals(1, whiteScore)
    }

    @Test
    fun moveAPieceAndEatAnEnemyAtPointPosition() {
        val piece1 = ChessPiece(ChessPiece.Position(2, 3), ChessSide.WHITE)
        val piece2 = ChessPiece(ChessPiece.Position(3, 5), ChessSide.BLACK)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2), ChessBoard.Size(6, 6))

        var blackScore = 0
        var whiteScore = 0

        chessBoard.move(piece1, ChessPiece.Position(3, 5), onPieceDies = { piece ->
            assertEquals(piece, piece2)
        }, onPointsGivenForSide = { side, points ->

            if (side == ChessSide.BLACK) {
                blackScore += points
            } else {
                whiteScore += points
            }

        }, onPieceMoves = { _ ->

        })

        assertEquals(0, blackScore)
        assertEquals(2, whiteScore)
        assertTrue { ChessPiece.Position(5, 0).equals(piece1.position) }
    }

    @Test
    fun moveAPieceAndEatAnEnemyAtPointPositionAnThenEatAnAlly() {
        val piece1 = ChessPiece(ChessPiece.Position(2, 3), ChessSide.WHITE)
        val piece2 = ChessPiece(ChessPiece.Position(3, 5), ChessSide.BLACK)
        val piece3 = ChessPiece(ChessPiece.Position(5, 0), ChessSide.WHITE)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2, piece3), ChessBoard.Size(6, 6))

        var blackScore = 0
        var whiteScore = 0

        chessBoard.move(piece1, ChessPiece.Position(3, 5), onPieceDies = { _ ->

        }, onPointsGivenForSide = { side, points ->

            if (side == ChessSide.BLACK) {
                blackScore += points
            } else {
                whiteScore += points
            }

        }, onPieceMoves = { _ ->

        })

        assertEquals(0, blackScore)
        assertEquals(2, whiteScore)
        assertEquals( 1, chessBoard.pieces.size)
        assertTrue { ChessPiece.Position(5, 0).equals(piece1.position) }
    }

    @Test
    fun moveAPieceAndEatAnEnemyAtPointPositionAnThenEatAnEnemy() {
        val piece1 = ChessPiece(ChessPiece.Position(2, 3), ChessSide.WHITE)
        val piece2 = ChessPiece(ChessPiece.Position(3, 5), ChessSide.BLACK)
        val piece3 = ChessPiece(ChessPiece.Position(5, 0), ChessSide.BLACK)

        val chessBoard = ChessBoard(arrayListOf(piece1, piece2, piece3), ChessBoard.Size(6, 6))

        var blackScore = 0
        var whiteScore = 0

        chessBoard.move(piece1, ChessPiece.Position(3, 5), onPieceDies = { _ ->

        }, onPointsGivenForSide = { side, points ->

            if (side == ChessSide.BLACK) {
                blackScore += points
            } else {
                whiteScore += points
            }

        }, onPieceMoves = { _ ->

        })

        assertEquals(0, blackScore)
        assertEquals(3, whiteScore)
        assertEquals( 1, chessBoard.pieces.size)
        assertTrue { ChessPiece.Position(5, 0).equals(piece1.position) }
    }
}