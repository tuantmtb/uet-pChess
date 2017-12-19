package edu.uet

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.utils.CountDownTimer

/**
 * Created by tuantmtb on 10/31/17.
 */
class GameMaster {
    val board = ChessBoard(arrayListOf(), ChessBoard.Size(ChessConfig.BOARD_WIDTH, ChessConfig.BOARD_HEIGHT))
    private var turn : ChessSide? = null
    private val points = hashMapOf(
            Pair<ChessSide, Int?>(ChessSide.BLACK, null),
            Pair<ChessSide, Int?>(ChessSide.WHITE, null)
    )
    private var timer = CountDownTimer(
            onTick = { oldValue, newValue -> GameDispatcher.dispatch("COUNT_DOWN_TICK", oldValue, newValue) },
            onTimeOut = { nextTurn() }
    )

    fun newGame() {
        points.forEach { side, point ->
            points[side] = 0
            GameDispatcher.dispatch("${side.name}_POINT_CHANGED", point, points[side])
        }

        board.pieces.forEach { GameDispatcher.dispatch("PIECE_DIED", it, null)}
        board.pieces.clear()
        (0 until board.size.width).filter { it != board.size.width/2 && it != board.size.width/2 - 1 }.forEach {
            board.pieces.add(ChessPiece(ChessPiece.Position(it, 0), ChessSide.WHITE))
            board.pieces.add(ChessPiece(ChessPiece.Position(it, board.size.height - 1), ChessSide.BLACK))
        }
        GameDispatcher.dispatch("PIECES_PLACED", null, board.pieces)

        val oldTurn = turn
        turn = ChessSide.WHITE
        GameDispatcher.dispatch("TURN_SWITCHED", oldTurn, turn)

        timer.restart()
    }

    private fun nextTurn() {
        val oldValue = turn
        turn = if (isTurnOf(ChessSide.WHITE)) ChessSide.BLACK else ChessSide.WHITE
        GameDispatcher.dispatch("TURN_SWITCHED", oldValue, turn)

        timer.restart()
    }

    fun winner() : ChessSide? {
        return when {
            points[ChessSide.BLACK]!! >= ChessConfig.WIN_POINT -> ChessSide.BLACK
            points[ChessSide.WHITE]!! >= ChessConfig.WIN_POINT -> ChessSide.WHITE
            board.pieces.all { it.chessSide == ChessSide.WHITE } -> ChessSide.WHITE
            board.pieces.all { it.chessSide == ChessSide.BLACK } -> ChessSide.BLACK
            else -> null
        }
    }

    fun hasWinner() : Boolean {
        return winner() != null
    }

    fun move(chessPiece: ChessPiece, position: ChessPiece.Position) : Boolean {
        if (!hasWinner() && isTurnOf(chessPiece.chessSide)) {
            if (isValidMove(chessPiece, position)) {
                var oldPos = chessPiece.position
                board.move(
                        chessPiece, position,
                        {
                            GameDispatcher.dispatch("PIECE_DIED", it, null)
                        },
                        { side, point ->
                            val oldValue = points[side]!!
                            points[side] = oldValue + point
                            GameDispatcher.dispatch("${side.name}_POINT_CHANGED", oldValue, points[side])
                        },
                        {
                            GameDispatcher.dispatch("PIECE_MOVED", oldPos, it.position)
                            oldPos = it.position
                        }
                )
                if (hasWinner()) {
                    GameDispatcher.dispatch("WINNER", null, winner())
                    timer.stop()
                } else {
                    nextTurn()
                }
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    fun isTurnOf(side: ChessSide): Boolean {
       return side == turn
    }

    fun isValidMove(piece: ChessPiece, position: ChessPiece.Position) : Boolean {
        val possibleNextPositions = board.getPossibleNextPositionForPiece(piece)
        return possibleNextPositions.any { position.equals(it) }
    }
}