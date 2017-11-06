package edu.uet.entity

import edu.uet.ChessConfig

/**
 * Save status on board
 *
 */
class ChessBoard(val pieces: List<ChessPiece>, val size: Size = Size(ChessConfig.BOARD_WIDTH, ChessConfig.BOARD_HEIGHT)) {

    class Size(val width: Int = ChessConfig.BOARD_WIDTH, val height:Int = ChessConfig.BOARD_HEIGHT)

    fun getBlockedPositionForPiece(piece: ChessPiece) = piece.getPossibleBlockedPositions(size)
            .filter { bp ->
                val matchedPiece = pieces.find { piece ->
                    piece.position.x == bp.position.x
                            && piece.position.y == bp.position.y
                }

                matchedPiece != null
            }

    fun getPossibleNextPositionForPiece(piece: ChessPiece): List<ChessPiece.Position> {
        val blockedPositions = getBlockedPositionForPiece(piece)

        var topBlocked = false
        var rightBlocked = false
        var bottomBlocked = false
        var leftBlocked = false

        blockedPositions.forEach({ p ->
            if (p.blockedSide == BlockedSide.TOP) {
                topBlocked = true
            }

            if (p.blockedSide == BlockedSide.RIGHT) {
                rightBlocked = true
            }

            if (p.blockedSide == BlockedSide.BOTTOM) {
                bottomBlocked = true
            }

            if (p.blockedSide == BlockedSide.LEFT) {
                leftBlocked = true
            }
        })

        return piece.getPossibleNextPositions(topBlocked, rightBlocked, bottomBlocked, leftBlocked, size)
    }
}