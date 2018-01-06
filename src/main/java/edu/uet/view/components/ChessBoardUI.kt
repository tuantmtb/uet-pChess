package edu.uet.view.components

import edu.uet.GameDispatcher
import edu.uet.GameMaster
import edu.uet.entity.ChessPiece
import edu.uet.view.Styles
import javafx.application.Platform
import javafx.scene.input.ClipboardContent
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints

class ChessBoardUI(private val game: GameMaster) : GridPane() {
    class ChessColumnConstraints : ColumnConstraints(Styles.GRID_SIZE)

    class ChessRowConstraints : RowConstraints(Styles.GRID_SIZE)

    init {
        prefWidth = Styles.GRID_SIZE * game.board.size.width
        prefHeight = Styles.GRID_SIZE * game.board.size.height

        (0 until game.board.size.width).forEach {
            columnConstraints.add(ChessColumnConstraints())
        }

        (0 until game.board.size.height).forEach { row ->
            rowConstraints.add(ChessRowConstraints())
            (0 until game.board.size.width).forEach { col ->
                val posUI = ChessPositionUI(row, col, game)
                posUI.setOnDragOver { onPositionUIDragOver(it) }
                posUI.setOnDragDone { onChessPieceUIDragDone(it) }
                add(posUI, col, row)
            }
        }

        bind()
    }

    private fun bind() {
        GameDispatcher.on("PIECE_MOVED", {
            Platform.runLater {
                val oldPosUI = findChessPositionUI(it.oldValue as ChessPiece.Position)
                val newPosUI = findChessPositionUI(it.newValue as ChessPiece.Position)
                oldPosUI.movePieceTo(newPosUI)
            }
        })

        GameDispatcher.on("PIECE_ADDED", {
            val piece = it.newValue as ChessPiece

            val pieceUI = ChessPieceUI(piece, game)
            pieceUI.setOnDragDetected { onChessPieceUIDragDetected(it) }

            val posUI = findChessPositionUI(piece.position)
            posUI.pieceUI = pieceUI
        })
    }

    private fun findChessPositionUI(pos: ChessPiece.Position): ChessPositionUI {
        return children.first { it is ChessPositionUI && it.userData.equals(pos) } as ChessPositionUI
    }

    private fun findChessPieceUI(piece: ChessPiece): ChessPieceUI? {
        return findChessPositionUI(piece.position).pieceUI
    }

    private var targetPositionUI: ChessPositionUI? = null
    private var draggingPiece: ChessPiece? = null

    private var validMoves: List<ChessPiece.Position>? = null
        set(value) {
            field?.map { findChessPositionUI(it) }?.forEach { it.unmark() }
            value?.map { findChessPositionUI(it) }?.forEach { it.markAsValidMove() }
            field = value
        }

    private fun onPositionUIDragOver(event: DragEvent) {
        targetPositionUI = event.source as ChessPositionUI
        if (game.isValidMove(draggingPiece!!, targetPositionUI!!.userData)) {
            event.acceptTransferModes(TransferMode.MOVE)
        } else {
            event.acceptTransferModes(null)
        }
    }

    private fun onChessPieceUIDragDone(event: DragEvent) {
        game.move(draggingPiece!!, targetPositionUI!!.userData)
        targetPositionUI = null
        draggingPiece = null
        validMoves = null
    }

    private fun onChessPieceUIDragDetected(event: MouseEvent) {
        val pieceUI = event.target as ChessPieceUI
        val piece = pieceUI.userData
        if (!game.hasWinner() && game.turn == piece.chessSide && !(game.pvc && game.turn == game.ai!!.color)) {
            // put image to drag board
            val db = pieceUI.startDragAndDrop(TransferMode.MOVE)
            val content = ClipboardContent()
            content.putImage(pieceUI.image)
            db.setContent(content)
            draggingPiece = piece

            // highlight possible positions
            validMoves = game.board.getPossibleNextPositionForPiece(piece)

            event.consume()
        }
    }
}