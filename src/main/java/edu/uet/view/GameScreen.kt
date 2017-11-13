package edu.uet.view

import edu.uet.GameMaster
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessPiece.Position
import edu.uet.entity.ChessSide
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.control.SplitPane
import javafx.scene.effect.BlurType
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import javafx.scene.image.ImageView
import javafx.scene.input.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.*

class GameScreen: View() {
    override val root: SplitPane by fxml("/GameScreen.fxml")
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

    init {
        titleProperty.set("Cờ điểm UET")

        boardUI.children.forEach {
            // set position cho các ô
            val pos = Position(game.board.size.width - 1 - GridPane.getColumnIndex(it), game.board.size.height - 1 - GridPane.getRowIndex(it))
            it.userData = pos
            it.setOnDragOver { onPositionDragOver(it) }
            it.setOnDragDone { onChessPieceDragDone(it) }
            posMap.put(pos.toString(), it as Pane)
        }

        game.board.pieces.forEach {
            // Đặt các quân cờ vào các ô
            val imageView = ImageView(if (it.chessSide == ChessSide.BLACK) "knight_black.png" else "knight_white.png")
            imageView.fitHeightProperty().set(100.0)
            imageView.fitWidthProperty().set(100.0)
            imageView.userData = it
            imageView.setOnDragDetected { onChessPieceDragDetected(it) }
            val pane = posMap[it.position.toString()]
            pane!!.children.add(imageView)
            pieceMap.put(it, imageView)
        }
        bind()
        updateTurn()
    }

    private fun bind() {
        game.addPropertyChangeListener("WHITE_POINT_CHANGED", {
            whitePoint.textProperty().set(it.newValue.toString())
        })
        game.addPropertyChangeListener("BLACK_POINT_CHANGED", {
            blackPoint.textProperty().set(it.newValue.toString())
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
            val player = it.newValue as ChessSide
            pieceMap.values.forEach {
                it.cursorProperty().set(Cursor.DEFAULT)
            }
        })
    }

    private fun onChessPieceDragDetected(event: MouseEvent) {
        val imgView = event.target as ImageView
        val piece = imgView.userData as ChessPiece
        if (!game.hasWinner() && game.isTurnOf(piece.chessSide)) {
            val db = imgView.startDragAndDrop(TransferMode.MOVE)
            val content = ClipboardContent()
            content.putImage(imgView.image)
            db.setContent(content)
            event.consume()
            possibleNextPos.addAll(game.board.getPossibleNextPositionForPiece(piece))
            highlightPossibleNextPositions()
        }
    }

    private fun onPositionDragOver(event: DragEvent) {
        nextPane = event.source as Pane
    }

    private fun onChessPieceDragDone(event: DragEvent) {
        val imgView = event.target as ImageView
        val curPiece = imgView.userData as ChessPiece
        val nextPos = nextPane!!.userData as Position
        game.move(curPiece, nextPos)
        nextPane = null
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
            blackName.textFillProperty().set(Paint.valueOf("red"))
            whiteName.textFillProperty().set(Paint.valueOf("black"))
        } else {
            blackName.textFillProperty().set(Paint.valueOf("black"))
            whiteName.textFillProperty().set(Paint.valueOf("red"))
        }
        pieceMap.forEach { piece, imgView ->
            if (game.isTurnOf(piece.chessSide)) {
                imgView.cursorProperty().set(Cursor.OPEN_HAND)
            } else {
                imgView.cursorProperty().set(Cursor.DEFAULT)
            }
        }
    }

    private fun highlightPossibleNextPositions() {
        possibleNextPos.map { posMap[it.toString()] }.forEach {
            val effect = possibleEffect()
            it!!.effectProperty().set(effect)
        }
    }

    private fun deHighlightPossibleNextPositions() {
        possibleNextPos.map { posMap[it.toString()] }.forEach {
            it!!.effectProperty().set(null)
        }
    }

    private fun selectedEffect() : Effect {
        val effect = InnerShadow()
        effect.blurTypeProperty().set(BlurType.ONE_PASS_BOX)
        effect.chokeProperty().set(10.0)
        effect.widthProperty().set(30.0)
        effect.heightProperty().set(30.0)
        effect.colorProperty().set(Color.valueOf("#e65100"))
        return effect
    }

    private fun possibleEffect() : Effect {
        val effect = InnerShadow()
        effect.blurTypeProperty().set(BlurType.ONE_PASS_BOX)
        effect.chokeProperty().set(10.0)
        effect.widthProperty().set(30.0)
        effect.heightProperty().set(30.0)
        effect.colorProperty().set(Color.valueOf("GREEN"))
        return effect
    }
}