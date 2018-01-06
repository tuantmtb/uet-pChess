package edu.uet.transform

import edu.uet.bitboard.*
import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import org.junit.Test
import java.util.*

/**
 * Created by tuantmtb on 1/6/18.
 */
class TransformTest {
    @Test
    fun arrayToViewModel() {
        val array = arrayOf(
                arrayOf("n", "n", "n", " ", " ", "n", "n", "n"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf("N", "N", "N", " ", " ", "N", "N", "N"))
        val transform = TranformBitboardAndView()

        val chessBoard = transform.arrayToViewModel(array)
        Helper.drawChessBoard(chessBoard)
    }

    @Test
    fun arrayToAIModel() {
        val array = arrayOf(
                arrayOf("n", "n", "n", " ", " ", "n", "n", "n"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf("N", "N", "N", " ", " ", "N", "N", "N"))
        val bitBoardPair = BoardGeneration.arrayToBitBoards(array)
        val transform = TranformBitboardAndView()
        val arrayConverted = transform.aiModelToArray(bitBoardPair)
        val chessBoard = transform.arrayToViewModel(arrayConverted)
        Helper.drawChessBoard(chessBoard)
    }

    @Test
    fun integrateAI(){

        val array = arrayOf(
                arrayOf("n", "n", "n", " ", " ", "n", "n", "n"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf("N", "N", "N", " ", " ", "N", "N", "N"))
//
        val chessBoard = BoardGeneration.arrayToBitBoards(array)

        //  val chessBoard = BoardGeneration.initiateStandardBoard()

        val ai = AI(Zobrist())
        val startTime = System.currentTimeMillis()
        val move = ai.findNextMove(3, chessBoard.WN, chessBoard.BN, false, 0, 0)
        val endTime = System.currentTimeMillis()

        val moveResult = ai.makeMove(move, true, chessBoard.WN, chessBoard.BN, 0, 0)

        Helper.drawBitboard(moveResult.WN)
        println()
        Helper.drawBitboard(moveResult.BN)

        println(Rating.evaluate(moveResult.WN, 0L, 0, 0))


        print("=> ")
        Helper.printMoves(move)
//        println(score)
        println("That took " + (endTime - startTime) + " milliseconds")
    }


}