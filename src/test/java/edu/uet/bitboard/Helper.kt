package edu.uet.bitboard

import edu.uet.ChessConfig
import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessSide
import org.junit.Test
import java.util.*
import java.util.Arrays


object Helper {


    fun drawChessBoard(chessBoard: ChessBoard) {
        val chessBoardArray = arrayOf(
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))
        val pieces = chessBoard.pieces
        for (piece in pieces) {
            val side = if (piece.chessSide == ChessSide.BLACK) "n" else "N"
            chessBoardArray[piece.position.x][piece.position.y] = side
        }
        for (i in 0..7) {
            println(Arrays.toString(chessBoardArray[i]))
        }
    }

    fun drawArray(bitBoardPair: BitBoardPair) {
        val chessBoard = arrayOf(
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "))

        val WN = bitBoardPair.WN
        val BN = bitBoardPair.BN

        for (i in 0..63) {

            if (WN shr i and 1 == 1L) {
                chessBoard[i / 8][i % 8] = "N"
            }

            if (BN shr i and 1 == 1L) {
                chessBoard[i / 8][i % 8] = "n"
            }
        }
        for (i in 0..7) {
            println(Arrays.toString(chessBoard[i]))
        }
    }

    fun printMoves(moves: String) {
        for (i in 0..(moves.length / 4 - 1)) {
            println("(" + moves[4 * i] + "," + moves[4 * i + 1] + ") -> (" + moves[4 * i + 2] + "," + moves[4 * i + 3] + ")")
        }
    }

    fun drawBitboard(bitBoard: Long) {
        val chessBoard = Array<Array<String>>(8) { arrayOf(" ", " ", " ", " ", " ", " ", " ", " ") }
        for (i in 0..63) {
            chessBoard[i / 8][i % 8] = ""
        }
        for (i in 0..63) {
            if (bitBoard.ushr(i) and 1 == 1L) {
                chessBoard[i / 8][i % 8] = "P"
            }
            if ("" == chessBoard[i / 8][i % 8]) {
                chessBoard[i / 8][i % 8] = " "
            }
        }
        for (i in 0..7) {
            println(Arrays.toString(chessBoard[i]))
        }
    }


}