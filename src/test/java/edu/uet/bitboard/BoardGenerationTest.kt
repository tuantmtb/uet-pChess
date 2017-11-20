package edu.uet.bitboard

import org.junit.Test

class BoardGenerationTest {
    @Test
    fun generateCorrectly() {
        val pair = BoardGeneration.initiateStandardBoard()
        Helper.drawArray(pair)
    }

    @Test
    fun arrayToBitBoards() {
        val chessBoard = arrayOf(
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", "N", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", "n", " ", "n", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))

        println(BoardGeneration.arrayToBitBoards(chessBoard).BN)
    }
}