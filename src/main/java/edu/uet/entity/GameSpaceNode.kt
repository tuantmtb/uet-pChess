package edu.uet.entity

import edu.uet.ChessConfig

/**
 *
 */
class GameSpaceNode(val chessBoard: ChessBoard) {
    var score: Int = 0

    // children
    var children = arrayListOf<GameSpaceNode>()

    // parent
    var parent: GameSpaceNode? = null

    /**
     * Evaluating function based on current score and number of remaining pieces
     */
    fun evaluateForSide(currentSide: ChessSide, currentScore: Int, valueOfEachRemainingPiece: Int = ChessConfig.VALUE_OF_EACH_REMAINING_PIECE): Int {
        val numberOfRemainPieceOfThisSide = chessBoard.pieces.filter { p -> p.chessSide == currentSide }.size

        return currentScore + numberOfRemainPieceOfThisSide * valueOfEachRemainingPiece
    }

}