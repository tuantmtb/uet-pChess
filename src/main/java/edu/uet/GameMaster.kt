package edu.uet

import edu.uet.entity.ChessBoard
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessSide
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeSupport

/**
 * Created by tuantmtb on 10/31/17.
 */
class GameMaster {
    val board = ChessBoard(arrayListOf(
            ChessPiece(ChessPiece.Position( 0, 0), ChessSide.WHITE),
            ChessPiece(ChessPiece.Position( 1, 0), ChessSide.WHITE),
            ChessPiece(ChessPiece.Position( 2, 0), ChessSide.WHITE),
            ChessPiece(ChessPiece.Position( 3, 0), ChessSide.WHITE),
            ChessPiece(ChessPiece.Position( 4, 0), ChessSide.WHITE),
            ChessPiece(ChessPiece.Position( 5, 0), ChessSide.WHITE),
            ChessPiece(ChessPiece.Position( 0, 5), ChessSide.BLACK),
            ChessPiece(ChessPiece.Position( 1, 5), ChessSide.BLACK),
            ChessPiece(ChessPiece.Position( 2, 5), ChessSide.BLACK),
            ChessPiece(ChessPiece.Position( 3, 5), ChessSide.BLACK),
            ChessPiece(ChessPiece.Position( 4, 5), ChessSide.BLACK),
            ChessPiece(ChessPiece.Position( 5, 5), ChessSide.BLACK)
    ), ChessBoard.Size(6, 6))
    var turn : ChessSide = ChessSide.WHITE
    private val points = hashMapOf(
            Pair(ChessSide.BLACK, 0),
            Pair(ChessSide.WHITE, 0)
    )
    private val pointThreshold = 10
    private val propChangeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(propName: String, listener: (evt: PropertyChangeEvent) -> Unit) {
        propChangeSupport.addPropertyChangeListener(propName, listener)
    }

    private fun nextTurn() {
        val oldValue = turn
        turn = if (isTurnOf(ChessSide.WHITE)) ChessSide.BLACK else ChessSide.WHITE
        propChangeSupport.firePropertyChange("TURN_SWITCHED", oldValue, turn)
    }

    fun winner() : ChessSide? {
        if (points[ChessSide.BLACK]!! >= pointThreshold) {
            return ChessSide.BLACK
        }

        if (points[ChessSide.WHITE]!! >= pointThreshold) {
            return ChessSide.WHITE
        }

        if (board.pieces.all { it.chessSide == ChessSide.WHITE }) {
            return ChessSide.WHITE
        }

        if (board.pieces.all { it.chessSide == ChessSide.BLACK }) {
            return ChessSide.BLACK
        }

        return null
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
                            propChangeSupport.firePropertyChange("PIECE_DIED", it, null)
                        },
                        { side, point ->
                            val oldValue = points[side]!!
                            points[side] = oldValue + point
                            propChangeSupport.firePropertyChange("${side.name}_POINT_CHANGED", oldValue, points[side])
                        },
                        {
                            propChangeSupport.firePropertyChange("PIECE_MOVED", oldPos, it.position)
                            oldPos = it.position
                        }
                )
                if (hasWinner()) {
                    propChangeSupport.firePropertyChange("WINNER", null, winner())
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