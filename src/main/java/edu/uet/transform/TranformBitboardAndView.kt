package edu.uet.transform

import edu.uet.bitboard.BitBoardPair
import edu.uet.bitboard.BoardGeneration
import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide

/**
 * Created by tuantmtb on 1/6/18.
 */
class TranformBitboardAndView {

    fun aiModelToViewModel(bitBoardPair: BitBoardPair): ChessBoard {
        val array = aiModelToArray(bitBoardPair)
        return arrayToViewModel(array)
    }

    fun viewModelToAIModel(chessBoard: ChessBoard): BitBoardPair {
        val array = viewModelToArray(chessBoard)
        return arrayToAIModel(array)
    }

    fun arrayToViewModel(array: Array<Array<String>>): ChessBoard {
        val pieces = arrayListOf<ChessPiece>()
        for (i in 0..7) {
            for (j in 0..7) {
                if (array[i][j] == "n") {
                    val piece = ChessPiece(ChessPiece.Position(i, j), ChessSide.BLACK)
                    pieces.add(piece)
                } else if (array[i][j] == "N") {
                    val piece = ChessPiece(ChessPiece.Position(i, j), ChessSide.WHITE)
                    pieces.add(piece)
                }
            }
        }
        return ChessBoard(pieces) // viewModel
    }

    fun viewModelToArray(chessBoard: ChessBoard): Array<Array<String>> {
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
        return chessBoardArray
    }

    fun aiModelToArray(bitBoardPair: BitBoardPair): Array<Array<String>> {
        val array = arrayOf(
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
                array[i / 8][i % 8] = "N"
            }

            if (BN shr i and 1 == 1L) {
                array[i / 8][i % 8] = "n"
            }
        }
        return array
    }

    fun arrayToAIModel(array: Array<Array<String>>): BitBoardPair {
        val bitBoardPair = BoardGeneration.arrayToBitBoards(array)
        return bitBoardPair
    }


}