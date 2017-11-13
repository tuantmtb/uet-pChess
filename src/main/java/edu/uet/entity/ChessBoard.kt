package edu.uet.entity

import edu.uet.ChessConfig

/**
 * Save status on board
 *
 */
class ChessBoard(var pieces: ArrayList<ChessPiece>, val size: Size = Size(ChessConfig.BOARD_WIDTH, ChessConfig.BOARD_HEIGHT)) {

    class Size(val width: Int = ChessConfig.BOARD_WIDTH, val height: Int = ChessConfig.BOARD_HEIGHT)

    fun getBlockedPositionForPiece(piece: ChessPiece) = piece.getPossibleBlockedPositions(size)
            .filter { bp ->
                val matchedPiece = getPieceAtPosition(bp.position)
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
                .filter { position ->
                    // Exclude positions that allies are standing on

                    var result = true

                    getPieceAtPosition(position)?.let { found ->
                        if(found.chessSide == piece.chessSide) {
                            result = false
                        }
                    }

                    result
                }
    }

    fun move(piece: ChessPiece, position: ChessPiece.Position,
             onPieceDies: ((ChessPiece) -> Unit)?,
             onPointsGivenForSide: ((ChessSide, Int) -> Unit)?,
             onPieceMoves: ((ChessPiece) -> Unit)?) {


        // Check if position is valid
        val possibleNextPositions = getPossibleNextPositionForPiece(piece)
        possibleNextPositions.find({ p -> p.equals(position) }) ?: return

        getPieceAtPosition(position)?.let { eatenPiece ->
            onPieceDies?.invoke(eatenPiece)
            onPointsGivenForSide?.invoke(piece.chessSide, ChessConfig.POINTS_FOR_EATING_AN_PIECE)
            pieces.remove(eatenPiece)
        }

        piece.moveTo(position)
        onPieceMoves?.invoke(piece)

        val point = position.earnedPointsForSide(piece.chessSide, size)

        // Reach a point position and teleport to corner
        if (point != 0) {
            onPointsGivenForSide?.invoke(piece.chessSide, point)

            val newPosition = getNewPositionWhenPieceIsTeleported(piece, point)
            val pieceAtNewPosition = getPieceAtPosition(newPosition)

            if (pieceAtNewPosition == null) {
                // Teleport
                piece.moveTo(newPosition)
                onPieceMoves?.invoke(piece)
            } else if (pieceAtNewPosition.chessSide != piece.chessSide) {
                // Eat an enemy
                onPieceDies?.invoke(pieceAtNewPosition)
                onPointsGivenForSide?.invoke(piece.chessSide, ChessConfig.POINTS_FOR_EATING_AN_PIECE)
                pieces.remove(pieceAtNewPosition)
                piece.moveTo(newPosition)
                onPieceMoves?.invoke(piece)
            }
        }
    }


    fun clone(): ChessBoard {
        val newPieces = ArrayList(pieces.map { p -> p.clone() })
        return ChessBoard(newPieces, Size(size.width, size.height))
    }

    fun getPieceAtPosition(position: ChessPiece.Position): ChessPiece? {
        return pieces.find({ piece -> piece.position.equals(position) })
    }

    private fun getNewPositionWhenPieceIsTeleported(piece: ChessPiece, point: Int): ChessPiece.Position {
        return if (piece.chessSide == ChessSide.WHITE) {
            if (point == ChessConfig.POINT_1) ChessPiece.Position(size.width - 1, 0)
            else ChessPiece.Position(0, 0)
        } else {
            if (point == ChessConfig.POINT_1) ChessPiece.Position(0, size.height - 1)
            else ChessPiece.Position(size.width - 1, size.height - 1)
        }
    }

}