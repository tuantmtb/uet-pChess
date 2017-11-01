package edu.uet.entity

class ChessAI {
    var color = ChessSide.BLACK
    var gameSpaceDeep = 3
    val currentGameSpace: GameSpace? = null

    fun getNextMoveForChessBoard(chessBoard: ChessBoard) {

        // create game space
        val gameSpaceRoot = GameSpaceNode(chessBoard)
        val currentGameSpace = GameSpace(gameSpaceRoot)


    }
}