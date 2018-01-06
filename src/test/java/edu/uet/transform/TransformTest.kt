package edu.uet.transform

import edu.uet.bitboard.BoardGeneration
import edu.uet.bitboard.Helper
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



}