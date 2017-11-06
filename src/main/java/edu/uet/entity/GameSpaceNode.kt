package edu.uet.entity

import edu.uet.ChessConfig

/**
 *
 */
class GameSpaceNode(val chessBoard: ChessBoard, val blackSideGameScore: Int, val whiteSideGameScore: Int, val currentSide: ChessSide) {
    var evaluatedValue: Int = 0

    // children
    var children = arrayListOf<GameSpaceNode>()

    // parent
    var parent: GameSpaceNode? = null

    /**
     * Evaluating function based on current score and number of remaining pieces
     */
    fun evaluateForSide(valueOfEachRemainingPiece: Int = ChessConfig.VALUE_OF_EACH_REMAINING_PIECE): Int {
        val currentScore = if (currentSide == ChessSide.BLACK) {
            blackSideGameScore
        } else {
            whiteSideGameScore
        }

        val numberOfRemainPieceOfThisSide = chessBoard.pieces.filter { p -> p.chessSide == currentSide }.size

        return currentScore + numberOfRemainPieceOfThisSide * valueOfEachRemainingPiece
    }

    fun generateNextPossibilities() {
        chessBoard.pieces.forEach({piece ->
            val nextPosiblePositions = chessBoard.getPossibleNextPositionForPiece(piece)

        })
    }
}