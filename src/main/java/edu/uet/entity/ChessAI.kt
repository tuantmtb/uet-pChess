package edu.uet.entity

import edu.uet.ChessConfig
import edu.uet.bitboard.AI
import edu.uet.bitboard.Rating
import edu.uet.bitboard.Zobrist
import edu.uet.transform.TranformBitboardAndView
import javafx.concurrent.Task
import tornadofx.*
import java.util.*
import kotlin.collections.HashMap

class ChessAI(val color: ChessSide = ChessSide.BLACK) {
    var gameSpaceDeep = 3
    var task: Task<Unit>? = null
    val ai = AI(Zobrist())


    /**
     * don't call callback if canceled
     */
    fun getNextMoveForChessBoard(chessBoard: ChessBoard, points: HashMap<ChessSide, Int?>, callback: ((ChessPiece, ChessPiece.Position) -> Unit)) {
        task = runAsync(daemon = true) {
            //        // create game space
//        val gameSpaceRoot = GameSpaceNode(chessBoard)
//        val currentGameSpace = GameSpace(gameSpaceRoot)
//            println("getNextMoveForChessBoard")
            val pieces = chessBoard.pieces
//        for (piece in pieces) {
//            val side = if (piece.chessSide == ChessSide.BLACK) "n" else "N"
//            print("[" + piece.position.x + "][" + piece.position.y + "]=" + side)
//        }
//        println()
            val chessBoardRotated = rotateChessBoard90Left(chessBoard)
            val bitBoardPair = TranformBitboardAndView().viewModelToAIModel(chessBoardRotated)

            val array = TranformBitboardAndView().viewModelToArray(chessBoardRotated)
//            for (i in 0..7) {
//                println(Arrays.toString(array[i]))
//            }

            val startTime = System.currentTimeMillis()
            val deepSearch = getDeepSearch(chessBoard.pieces.size)
            val move = ai.findNextMove(deepSearch, bitBoardPair.WN, bitBoardPair.BN, false, points.get(ChessSide.WHITE)!!, points.get(ChessSide.BLACK)!!, ChessConfig.WIN_POINT)

            val endTime = System.currentTimeMillis()

            val moveResult = ai.makeMove(move, true, bitBoardPair.WN, bitBoardPair.BN, points.get(ChessSide.WHITE)!!, points.get(ChessSide.BLACK)!!)
//            println(Rating.evaluate(moveResult.WN, 0L, 0, 0))

//        var piece = chessBoardRotated.pieces.filter { it.chessSide == ChessSide.BLACK }.first()
//            printMoves(move)

            if (move != "") {
                val piece = chessBoard.pieces.filter {
                    //            it.position.x == move[0].toString().toInt() && it.position.y == move[1].toString().toInt()
                    it.position.x == move[1].toString().toInt() && it.position.y == 7 - move[0].toString().toInt()
                }.first()

//        val position = ChessPiece.Position(move[2].toString().toInt(), move[3].toString().toInt())
                val position = ChessPiece.Position(move[3].toString().toInt(), 7 - move[2].toString().toInt())
                callback(piece, position)
                Unit
            }
        }
    }

    fun getDeepSearch(pieceSize: Int): Int {
        if (pieceSize > 10) return 5
        if (pieceSize > 8) return 6
        if (pieceSize > 6) return 7
        if (pieceSize > 5) return 8
        if (pieceSize > 4) return 9
        if (pieceSize > 3) return 9
        if (pieceSize > 2) return 10
        return 11
    }


    /**
     * When timeout
     */
    fun cancelSearching() {
         ai.cancel = true
//        task?.cancel()
    }

    fun printMoves(moves: String) {

        for (i in 0..(moves.length / 4 - 1)) {
            println("(" + moves[4 * i] + "," + moves[4 * i + 1] + ") -> (" + moves[4 * i + 2] + "," + moves[4 * i + 3] + ")")
        }
    }

    fun rotateChessBoard90Left(chessBoard: ChessBoard): ChessBoard {
        val pieces = chessBoard.pieces
        val piecesRotated = arrayListOf<ChessPiece>()
        for (piece in pieces) {
            val pieceRotated = ChessPiece(ChessPiece.Position(7 - piece.position.y, piece.position.x), piece.chessSide)
            piecesRotated.add(pieceRotated)
        }
        return ChessBoard(piecesRotated, ChessBoard.Size(8, 8))

    }
}