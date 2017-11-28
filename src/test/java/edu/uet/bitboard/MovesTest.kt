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
        val moves = Moves.possibleMovesW(bitboardPair.WN, bitboardPair.BN)

        Helper.printMoves(moves)
    }

    @Test
    fun makeMove() {
        val chessBoard = arrayOf(
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", "N"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))

        val bitboardPair = BoardGeneration.arrayToBitBoards(chessBoard)
        val newWN = Moves.makeMove(bitboardPair.WN, "6765")

        Helper.drawArray(BitBoardPair(newWN, bitboardPair.BN))
    }
}