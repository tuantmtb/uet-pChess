package edu.uet.entity

class ChessAI(val color: ChessSide = ChessSide.BLACK) {
    var gameSpaceDeep = 3
    val currentGameSpace: GameSpace? = null

    /**
     * don't call callback if canceled
     */
    fun getNextMoveForChessBoard(chessBoard: ChessBoard, callback: ((ChessPiece, ChessPiece.Position) -> Unit)) {

//        // create game space
//        val gameSpaceRoot = GameSpaceNode(chessBoard)
//        val currentGameSpace = GameSpace(gameSpaceRoot)
        println("getNextMoveForChessBoard")

        var piece = chessBoard.pieces.filter { it.chessSide == ChessSide.BLACK }.first()
        var position = chessBoard.getPossibleNextPositionForPiece(piece)[0]
        callback(piece, position)
        println("piece moved")
    }


    /**
     * When timeout
     */
    fun cancelGetNextMove() {

    }
}