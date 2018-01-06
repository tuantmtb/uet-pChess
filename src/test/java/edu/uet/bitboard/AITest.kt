package edu.uet.bitboard

import org.junit.Test
import kotlin.test.assertEquals

class AITest {
    @Test
    fun alphaBetaSearch() {
//        val array = arrayOf(
//                arrayOf(" ", "N", " ", " ", " ", " ", " ", " "),
//                arrayOf(" ", " ", " ", " ", "N", " ", " ", "n"),
//                arrayOf(" ", " ", "n", " ", " ", "n", " ", " "),
//                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
//                arrayOf(" ", "n", " ", " ", " ", " ", "N", " "),
//                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
//                arrayOf(" ", " ", "N", " ", " ", " ", " ", " "),
//                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))

        val array = arrayOf(
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", "n", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", "N", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", "n", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf("N", "N", " ", " ", " ", "N", "N", "N"))
//
        val chessBoard = BoardGeneration.arrayToBitBoards(array)

        //  val chessBoard = BoardGeneration.initiateStandardBoard()

        val ai = AI(Zobrist())
        val startTime = System.currentTimeMillis()
        val result = ai.findNextMove(7, chessBoard.WN, chessBoard.BN, false, 0, 0)

        val endTime = System.currentTimeMillis()


        val moveResult = ai.makeMove(result.move, true, chessBoard.WN, chessBoard.BN, 0, 0)

        Helper.drawBitboard(moveResult.WN)
        println()
        Helper.drawBitboard(moveResult.BN)

        println(Rating.evaluate(moveResult.WN, 0L, 0, 0))


        print("=> ")
        Helper.printMoves(result.move)
        println(result.score)
        println("That took " + (endTime - startTime) + " milliseconds")
    }

    @Test
    fun makeMove() {
        val array = arrayOf(
                arrayOf("n", "n", "n", " ", " ", "n", "n", "n"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf("N", "N", "N", " ", " ", "N", "N", "N"))

        val chessBoard = BoardGeneration.arrayToBitBoards(array)

        val ai = AI(Zobrist())

        val result = ai.makeMove("2203", true, chessBoard.WN, chessBoard.BN, 0, 0)

        Helper.drawBitboard(result.WN)
        println()
        Helper.drawBitboard(result.BN)

//        assertEquals(3, result.wScore)
    }
}