package edu.uet.entity

import edu.uet.bitboard.AI
import edu.uet.bitboard.BitBoardPair
import edu.uet.bitboard.Rating
import edu.uet.bitboard.Zobrist
import edu.uet.transform.TranformBitboardAndView
import java.util.*

class ChessAI(val color: ChessSide = ChessSide.BLACK) {
    var gameSpaceDeep = 3
    val currentGameSpace: GameSpace? = null

    /**
     * don't call callback if canceled
     */
    fun getNextMoveForChessBoard(chessBoard: ChessBoard, callback: ((ChessPiece, ChessPiece.Position) -> Unit)) {

//        // create game space
//        val gameSpaceRoot = GameSpaceNode(chessBoard)
//        val currentGameSpace = GameSpace(gameSpaceRoot)
        println("getNextMoveForChessBoard")

        val bitBoardPair = TranformBitboardAndView().viewModelToAIModel(chessBoard)
        drawArray(bitBoardPair)
        val ai = AI(Zobrist())
        val startTime = System.currentTimeMillis()
        val move = ai.findNextMove(5, bitBoardPair.WN, bitBoardPair.BN, false, 0, 0)
        val endTime = System.currentTimeMillis()

        val moveResult = ai.makeMove(move, true, bitBoardPair.WN, bitBoardPair.BN, 0, 0)
        println(Rating.evaluate(moveResult.WN, 0L, 0, 0))

//        var piece = chessBoard.pieces.filter { it.chessSide == ChessSide.BLACK }.first()
        printMoves(move)
        val piece = chessBoard.pieces.filter {
            it.position.x == move[0].toString().toInt() && it.position.y == move[1].toString().toInt()
        }.first()

        val position = ChessPiece.Position(move[2].toString().toInt(), move[3].toString().toInt())
        callback(piece, position)
        println("piece moved")
    }


    /**
     * When timeout
     */
    fun cancelGetNextMove() {

    }

    fun printMoves(moves: String) {

        for (i in 0..(moves.length / 4 - 1)) {
            println("(" + moves[4 * i] + "," + moves[4 * i + 1] + ") -> (" + moves[4 * i + 2] + "," + moves[4 * i + 3] + ")")
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
}