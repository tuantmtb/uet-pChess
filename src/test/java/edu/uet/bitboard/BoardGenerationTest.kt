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

    @Test
    fun sample() {
        val i = 8
        var binary = "0000000000000000000000000000000000000000000000000000000000000000"
        binary = binary.substring(i + 1) + "1" + binary.substring(0, i)

        print(binary)
    }
}