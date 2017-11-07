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
    fun evaluate(valueOfEachRemainingPiece: Int = ChessConfig.VALUE_OF_EACH_REMAINING_PIECE): Int {
        val currentScore = if (currentSide == ChessSide.BLACK) {
            blackSideGameScore
        } else {
            whiteSideGameScore
        }

        val numberOfRemainPieceOfThisSide = chessBoard.pieces.filter { p -> p.chessSide == currentSide }.size

        return currentScore + numberOfRemainPieceOfThisSide * valueOfEachRemainingPiece
    }

    fun generateChildren() {

        children.clear()

        chessBoard.pieces
                .filter { p -> p.chessSide == currentSide }
                .forEach({ piece ->
                    val nextPossiblePositions = chessBoard.getPossibleNextPositionForPiece(piece)

                    nextPossiblePositions.forEach({ position ->
                        val newBoard = chessBoard.clone()

                        var newBlackSideGameScore = blackSideGameScore
                        var newWhiteSideGameScore = whiteSideGameScore

                        newBoard.pieces.find { p -> p.equals(piece) }?.let { pieceToMove ->
                            chessBoard.move(pieceToMove, position, onPieceMoves = null, onPieceDies = null, onPointsGivenForSide = { side, points ->
                                if (side == ChessSide.BLACK) {
                                    newBlackSideGameScore += points
                                } else {
                                    newWhiteSideGameScore += points
                                }
                            })

                            val newSide = if (currentSide == ChessSide.BLACK) ChessSide.WHITE else ChessSide.BLACK
                            val newNode = GameSpaceNode(newBoard, newBlackSideGameScore, newWhiteSideGameScore, newSide)

                            newNode.parent = this
                            children.add(newNode)
                        }
                    })
                })
    }
}