package edu.uet.entity

import edu.uet.bitboard.AI
import edu.uet.bitboard.Rating
import edu.uet.bitboard.Zobrist
import edu.uet.transform.TranformBitboardAndView
import java.util.*

class ChessAI_FixOld(val color: ChessSide = ChessSide.BLACK) {
    var gameSpaceDeep = 3


    /**
     * don't call callback if canceled
     */
    fun getNextMoveForChessBoard(chessBoard: ChessBoard, callback: ((ChessPiece, ChessPiece.Position) -> Unit)) {

//        // create game space
//        val gameSpaceRoot = GameSpaceNode(chessBoard)
//        val currentGameSpace = GameSpace(gameSpaceRoot)
        println("getNextMoveForChessBoard")
        val pieces = chessBoard.pieces
//        for (piece in pieces) {
//            val side = if (piece.chessSide == ChessSide.BLACK) "n" else "N"
//            print("[" + piece.position.x + "][" + piece.position.y + "]=" + side)
//        }
//        println()
        val chessBoardRotated = rotateChessBoard90Left(chessBoard)
        val bitBoardPair = TranformBitboardAndView().viewModelToAIModel(chessBoardRotated)

        val array = TranformBitboardAndView().viewModelToArray(chessBoardRotated)
        for (i in 0..7) {
            println(Arrays.toString(array[i]))
        }

        val ai = AI(Zobrist())
        val startTime = System.currentTimeMillis()
        val move = ai.findNextMove(5, bitBoardPair.WN, bitBoardPair.BN, false, 0, 0)
        val endTime = System.currentTimeMillis()

        val moveResult = ai.makeMove(move, true, bitBoardPair.WN, bitBoardPair.BN, 0, 0)
        println(Rating.evaluate(moveResult.WN, 0L, 0, 0))

//        var piece = chessBoardRotated.pieces.filter { it.chessSide == ChessSide.BLACK }.first()
        printMoves(move)
        val piece = chessBoard.pieces.filter {
            //            it.position.x == move[0].toString().toInt() && it.position.y == move[1].toString().toInt()
            it.position.x == move[1].toString().toInt() && it.position.y == 7 - move[0].toString().toInt()
        }.first()

//        val position = ChessPiece.Position(move[2].toString().toInt(), move[3].toString().toInt())
        val position = ChessPiece.Position(move[3].toString().toInt(), 7 - move[2].toString().toInt())
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