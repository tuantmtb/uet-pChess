package edu.uet.bitboard

import org.junit.Test

class MovesTest {
    @Test
    fun printPossibleMove() {
        val chessBoard = arrayOf(
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", "n"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", "N"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))

        val bitboardPair = BoardGeneration.arrayToBitBoards(chessBoard)
        val moves = Moves.possibleMovesW(bitboardPair)

        Helper.printMoves(moves)
    }
}