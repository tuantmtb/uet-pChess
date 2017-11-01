package edu.uet.entity

import edu.uet.ChessConfig

/**
 * Save status on board
 *
 */
class ChessBoard(val pieces: List<ChessPiece>) {

    class Size(val width: Int = ChessConfig.BOARD_WIDTH, val height:Int = ChessConfig.BOARD_HEIGHT)
}