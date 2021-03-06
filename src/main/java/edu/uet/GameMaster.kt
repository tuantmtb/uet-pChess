package edu.uet

import edu.uet.entity.ChessAI
import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import edu.uet.utils.CountDownTimer

object GameMaster {
    val board = ChessBoard(arrayListOf(), ChessBoard.Size(ChessConfig.BOARD_WIDTH, ChessConfig.BOARD_HEIGHT))

    var turn : ChessSide? = null
        private set(newValue) {
            val oldValue = field
            field = newValue
            GameDispatcher.fire("TURN_SWITCHED", oldValue, newValue)
        }

    var pvc : Boolean = false
    var ai: ChessAI? = null

    private val points = hashMapOf(
            Pair<ChessSide, Int?>(ChessSide.BLACK, null),
            Pair<ChessSide, Int?>(ChessSide.WHITE, null)
    )

    private var timer = CountDownTimer(
            onTick = { oldValue, newValue -> GameDispatcher.fire("COUNT_DOWN_TICK", oldValue, newValue) },
            onTimeOut = {
                if (isAiTurn()) {
                    ai?.cancelSearching()
                } else {
                    nextTurn()
                }
            }
    )

    fun stopTimer() {
        timer.stop()
    }

    fun newGame(pvc: Boolean = false, initialTurn: ChessSide = ChessSide.WHITE) {
        this.pvc = pvc
        ai = if (pvc) ChessAI(ChessSide.BLACK) else null

        points.forEach { side, point ->
            points[side] = 0
            GameDispatcher.fire("${side.name}_POINT_CHANGED", point, points[side])
        }

        board.pieces.forEach { GameDispatcher.fire("PIECE_DIED", it, null)}
        board.pieces.clear()
        (0 until board.size.width).filter { it != board.size.width/2 && it != board.size.width/2 - 1 }.forEach {
            val wPiece = ChessPiece(ChessPiece.Position(it, 0), ChessSide.WHITE)
            board.pieces.add(wPiece)
            GameDispatcher.fire("PIECE_ADDED", null, wPiece)

            val bPiece = ChessPiece(ChessPiece.Position(it, board.size.height - 1), ChessSide.BLACK)
            board.pieces.add(bPiece)
            GameDispatcher.fire("PIECE_ADDED", null, bPiece)
        }

        turn = initialTurn
        timer.restart()
        if (isAiTurn()) {
            aiMoves()
        }
    }

    private fun aiMoves() {
        ai?.getNextMoveForChessBoard(board, points, {chessPiece, position ->
            move(chessPiece, position)
        })
    }

    fun isAiTurn(): Boolean {
        return pvc && turn == ai?.color
    }

    private fun nextTurn() {
        turn = if (turn == ChessSide.WHITE) ChessSide.BLACK else ChessSide.WHITE
        println("Next turn: " + if (turn == ChessSide.WHITE) "White" else "Black")
        timer.restart()
        if (isAiTurn()) {
            aiMoves()
        }
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
        if (!hasWinner() && turn == chessPiece.chessSide) {


            if (isValidMove(chessPiece, position)) {
                var oldPos = chessPiece.position
                board.move(
                        chessPiece, position,
                        {
                            GameDispatcher.fire("PIECE_DIED", it, null)
                        },
                        { side, point ->
                            val oldValue = points[side]!!
                            points[side] = oldValue + point
                            GameDispatcher.fire("${side.name}_POINT_CHANGED", oldValue, points[side])
                        },
                        {
                            println("=======================================================")
                            GameDispatcher.fire("PIECE_MOVED", oldPos, it.position)
                            oldPos = it.position
                        }
                )
                if (hasWinner()) {
                    GameDispatcher.fire("WINNER", null, winner())
                    turn = null
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

    fun isValidMove(piece: ChessPiece, position: ChessPiece.Position) : Boolean {
        val possibleNextPositions = board.getPossibleNextPositionForPiece(piece)
        return possibleNextPositions.any { position.equals(it) }
    }
}