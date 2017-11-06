package edu.uet.entity

/**
 * Created by tuantmtb on 10/31/17.
 */
class ChessPiece(val position: Position, val chessSide: ChessSide) {
    class Position(val x: Int = -1, val y: Int = -1)

    /**
     * Given if this piece is being blocked or not on 4 sides and based on current position and the chess board size, find all possible positions for the next move
     */
    fun getPossibleNextPositions(topBlocked: Boolean = false,
                                 rightBlocked: Boolean = false,
                                 bottomBlocked: Boolean = false,
                                 leftBlocked: Boolean = false,
                                 boardSize: ChessBoard.Size
    ): List<Position> {
        val generatedPositions = arrayListOf<Position>()


        val x = position.x
        val y = position.y

        if (!topBlocked) {
            generatedPositions.add(Position(x - 1, y + 2))
            generatedPositions.add(Position(x + 1, y + 2))
        }

        if (!rightBlocked) {
            generatedPositions.add(Position(x + 2, y + 1))
            generatedPositions.add(Position(x + 2, y - 1))
        }

        if (!bottomBlocked) {
            generatedPositions.add(Position(x + 1, y - 2))
            generatedPositions.add(Position(x - 1, y - 2))
        }

        if (!leftBlocked) {
            generatedPositions.add(Position(x - 2, y - 1))
            generatedPositions.add(Position(x - 2, y + 1))
        }

        return generatedPositions.filter { p -> validatePosition(p, boardSize) }
    }

    /**
     * Check if this position is still inside the board
     */
    private fun validatePosition(position: Position, boardSize: ChessBoard.Size): Boolean {
        return position.x >= 0 && position.x < boardSize.width
                && position.y >= 0 && position.y < boardSize.height
    }

    /**
     * Given new x and y, clone a new piece
     */
    fun newChessWithPosition(x: Int, y: Int): ChessPiece {
        return ChessPiece(Position(x, y), chessSide)
    }

    fun getPossibleBlockedPositions(boardSize: ChessBoard.Size): List<BlockedPosition> {
        return arrayListOf(
                BlockedPosition(Position(position.x, position.y + 1), BlockedSide.TOP),
                BlockedPosition(Position(position.x + 1, position.y), BlockedSide.RIGHT),
                BlockedPosition(Position(position.x, position.y - 1), BlockedSide.BOTTOM),
                BlockedPosition(Position(position.x - 1, position.y), BlockedSide.LEFT)
        )
                .filter({ p -> validatePosition(p.position, boardSize) })
    }
}