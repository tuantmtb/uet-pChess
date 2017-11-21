package edu.uet.view

import edu.uet.GameMaster
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessPiece.Position
import edu.uet.entity.ChessSide
import javafx.application.Platform
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.effect.BlurType
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import javafx.scene.image.ImageView
import javafx.scene.input.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.information

class GameView : BaseView() {
    override val root: Pane by fxml("/Game.fxml")
    private val boardUI: GridPane by fxid("board")
    private var game = GameMaster()
    private val blackName: Label by fxid("blackName")
    private val whiteName: Label by fxid("whiteName")
    private val blackPoint: Label by fxid("blackPoint")
    private val whitePoint: Label by fxid("whitePoint")
    private var nextPane: Pane? = null
    private val pieceMap =  HashMap<ChessPiece, ImageView>()
    private val posMap = HashMap<String, Pane>()
    private val possibleNextPos = arrayListOf<Position>()
    private var draggingPiece: ChessPiece? = null

    init {
        boardUI.children.forEach {
            // set position cho các ô
            val pos = Position(game.board.size.width - 1 - GridPane.getColumnIndex(it), game.board.size.height - 1 - GridPane.getRowIndex(it))
            it.userData = pos
            it.setOnDragOver { onPositionDragOver(it) }
            it.setOnDragDone { onChessPieceDragDone(it) }
            posMap.put(pos.toString(), it as Pane)
        }

        bind()
        game.newGame()
    }

    private fun placePiecesOnBoard() {
        game.board.pieces.forEach {
            // Đặt các quân cờ vào các ô
            val imageView = ImageView(if (it.chessSide == ChessSide.BLACK) "knight_black.png" else "knight_white.png")
            imageView.fitHeight = 100.0
            imageView.fitWidth = 100.0
            imageView.userData = it
            imageView.setOnDragDetected { onChessPieceDragDetected(it) }
            if (game.isTurnOf(it.chessSide)) {
                imageView.cursor = Cursor.OPEN_HAND
            } else {
                imageView.cursor = Cursor.DEFAULT
            }
            val pane = posMap[it.position.toString()]
            pane!!.children.add(imageView)
            pieceMap.put(it, imageView)
        }
    }

    private fun bind() {
        game.addPropertyChangeListener("WHITE_POINT_CHANGED", {
            whitePoint.text = it.newValue.toString()
        })
        game.addPropertyChangeListener("BLACK_POINT_CHANGED", {
            blackPoint.text = it.newValue.toString()
        })
        game.addPropertyChangeListener("TURN_SWITCHED", { updateTurn() })
        game.addPropertyChangeListener("PIECE_DIED", {
            val pane = pieceMap[it.oldValue as ChessPiece]!!.parent as Pane
            removePiece(pane)
            pieceMap.remove(it.oldValue as ChessPiece)
        })
        game.addPropertyChangeListener("PIECE_MOVED", {
            val oldPos = it.oldValue as Position
            val oldPane = posMap[oldPos.toString()]!!
            val newPos = it.newValue as Position
            val newPane = posMap[newPos.toString()]!!
            val imgView = movePiece(oldPane, newPane)
            val piece = imgView.userData as ChessPiece
            pieceMap[piece] = imgView
        })
        game.addPropertyChangeListener("WINNER", {
            pieceMap.values.forEach {
                it.cursor = Cursor.DEFAULT
            }
            information("Người chiến thắng", game.winner()!!.name)
        })
        game.addPropertyChangeListener("PIECES_PLACED", {
            placePiecesOnBoard()
        })
    }

    private fun onChessPieceDragDetected(event: MouseEvent) {
        val imgView = event.target as ImageView
        val piece = imgView.userData as ChessPiece
        if (!game.hasWinner() && game.isTurnOf(piece.chessSide)) {
            // put image to drag board
            val db = imgView.startDragAndDrop(TransferMode.MOVE)
            val content = ClipboardContent()
            content.putImage(imgView.image)
            db.setContent(content)
            draggingPiece = piece

            // highlight possible positions
            possibleNextPos.addAll(game.board.getPossibleNextPositionForPiece(piece))
            highlightPossibleNextPositions()

            event.consume()
        }
    }

    private fun onPositionDragOver(event: DragEvent) {
        nextPane = event.source as Pane
        if (game.isValidMove(draggingPiece!!, nextPane!!.userData as Position)) {
            event.acceptTransferModes(TransferMode.MOVE)
        } else {
            event.acceptTransferModes(null)
        }
    }

    private fun onChessPieceDragDone(event: DragEvent) {
        val nextPos = nextPane!!.userData as Position
        game.move(draggingPiece!!, nextPos)
        nextPane = null
        draggingPiece = null
        deHighlightPossibleNextPositions()
        possibleNextPos.clear()
    }

    private fun movePiece(from: Pane, to: Pane): ImageView {
        val imgView = removePiece(from)
        to.children.add(imgView)
        return imgView
    }

    private fun removePiece(pane: Pane) : ImageView {
        val imgView = pane.children.last { it is ImageView } as ImageView
        pane.children.remove(imgView)
        return imgView
    }

    private fun updateTurn() {
        if (game.isTurnOf(ChessSide.BLACK)) {
            blackName.textFill = Paint.valueOf("red")
            whiteName.textFill = Paint.valueOf("black")
        } else {
            blackName.textFill = Paint.valueOf("black")
            whiteName.textFill = Paint.valueOf("red")
        }
        pieceMap.forEach { piece, imgView ->
            if (game.isTurnOf(piece.chessSide)) {
                imgView.cursor = Cursor.OPEN_HAND
            } else {
                imgView.cursor = Cursor.DEFAULT
            }
        }
    }

    private fun highlightPossibleNextPositions() {
        possibleNextPos.map { posMap[it.toString()] }.forEach {
            val effect = possibleEffect()
            it!!.effect = effect
        }
    }

    private fun deHighlightPossibleNextPositions() {
        possibleNextPos.map { posMap[it.toString()] }.forEach {
            it!!.effect = null
        }
    }

    private fun possibleEffect() : Effect {
        val effect = InnerShadow()
        effect.blurType = BlurType.ONE_PASS_BOX
        effect.choke = 10.0
        effect.width = 30.0
        effect.height = 30.0
        effect.color = Color.valueOf("GREEN")
        return effect
    }

    fun menuNewGame() {
        game.newGame()
    }

    fun menuClose() {
        Platform.exit()
    }
}