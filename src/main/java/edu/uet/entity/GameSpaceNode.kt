package edu.uet.entity

/**
 *
 */
class GameSpaceNode {
    // contain chessBoard
    var chessBoard: ChessBoard? = null

    var score: Int = 0

    // children
    var children = arrayListOf<GameSpaceNode>()

    // parent
    var parent: GameSpaceNode? = null

    fun calculate(): Int {
        var score = 0
        return score
    }

}