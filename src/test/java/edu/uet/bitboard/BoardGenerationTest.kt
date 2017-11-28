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
                arrayOf(" ", " ", " ", "N", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))

        println(BoardGeneration.arrayToBitBoards(chessBoard).WN)
    }
}