package edu.uet.view

import edu.uet.GameMaster
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessPiece.Position
import edu.uet.entity.ChessSide
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.control.SplitPane
import javafx.scene.effect.BlurType
import javafx.scene.effect.InnerShadow
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.*

class GameScreen: View() {
    override val root: SplitPane by fxml("/GameScreen.fxml")
    private val boardUI: GridPane by fxid("board")
    private var selectedPane: Pane? = null
    private var game: GameMaster = GameMaster()
    private val pBlackName: Label by fxid("pBlackName")
    private val pWhiteName: Label by fxid("pWhiteName")

    init {
        titleProperty.set("Cờ điểm UET")

        boardUI.getChildList()?.forEach {
            // set position cho các ô
            val pos = Position(game.board.size.width - 1 - GridPane.getColumnIndex(it), game.board.size.height - 1 - GridPane.getRowIndex(it))
            it.userData = pos

            // Đặt các quân cờ vào các ô
            val piece = game.board.pieces.firstOrNull { it.position.equals(pos) }
            val pane = it as Pane
            if (piece != null) {
                val imageView = ImageView(if (piece.chessSide == ChessSide.BLACK) "knight_black.png" else "knight_white.png")
                imageView.fitHeightProperty().set(100.0)
                imageView.fitWidthProperty().set(100.0)
                imageView.mouseTransparentProperty().set(true)
                pane.children.add(imageView)

                // Hình bàn tay khi di chuyển lên quân cờ
                if (piece.chessSide == game.turn) {
                    pane.cursorProperty().set(Cursor.HAND)
                }
            }
        }
        onTurnSwitched()
    }

    private fun findPaneOf(piece: ChessPiece) : Pane {
        return boardUI.getChildList()?.first { piece.position.equals(it.userData as Position) } as Pane
    }

    fun positionOnMouseClicked(event: MouseEvent) {
        val targetPane = event.target as Pane
        val targetPosition = targetPane.userData as Position
        val targetPiece = game.board.getPieceAtPosition(targetPosition)

        if (selectedPane == null) {
            // Chọn quân
            if (targetPiece != null && targetPiece.chessSide == game.turn) {
                select(targetPane)
            }
        } else {
            // Di chuyển hoặc ăn quân
            val selectedPosition = selectedPane?.userData as Position
            val currentPiece = game.board.getPieceAtPosition(selectedPosition)
            if (currentPiece != null) {
                val possibleNextPositions = game.board.getPossibleNextPositionForPiece(currentPiece)
                // Nếu là 1 nước đi hợp lệ
                if (possibleNextPositions.any { targetPosition.equals(it) }) {
                    var prevPane = selectedPane
                    game.board.move(
                            currentPiece, targetPosition,
                            {piece ->
                                val pane = findPaneOf(piece)
                                removePiece(pane)
                            },
                            null,
                            {piece ->
                                val newPane = findPaneOf(piece)
                                if (prevPane != null) {
                                    movePiece(prevPane!!, newPane)
                                    prevPane = newPane
                                }
                            }
                    )
                    deselect()
                    game.nextTurn()
                    onTurnSwitched()
                }
            }
        }
    }

    private fun movePiece(from: Pane, to: Pane) {
        removePiece(from).let { image -> to.children.add(image)}
        to.cursorProperty().set(Cursor.HAND)
    }

    private fun removePiece(pane: Pane) : ImageView {
        val image = pane.children.first()
        pane.children.clear()
        pane.cursorProperty().set(Cursor.DEFAULT)
        return image as ImageView
    }

    private fun onTurnSwitched() {
        if (game.turn == ChessSide.BLACK) {
            pBlackName.textFillProperty().set(Paint.valueOf("red"))
            pWhiteName.textFillProperty().set(Paint.valueOf("black"))
        } else {
            pBlackName.textFillProperty().set(Paint.valueOf("black"))
            pWhiteName.textFillProperty().set(Paint.valueOf("red"))
        }
        game.board.pieces.forEach {
            val pane = findPaneOf(it)
            if (it.chessSide == game.turn) {
                pane.cursorProperty().set(Cursor.HAND)
            } else {
                pane.cursorProperty().set(Cursor.DEFAULT)
            }
        }
    }

    fun boardOnKeyReleased(event: KeyEvent) {
        if (event.code == KeyCode.ESCAPE) {
            deselect()
        }
    }

    private fun deselect() {
        selectedPane?.effectProperty()?.set(null)
        selectedPane = null
    }

    private fun select(pane: Pane) {
        val effect = InnerShadow()
        effect.blurTypeProperty().set(BlurType.ONE_PASS_BOX)
        effect.chokeProperty().set(10.0)
        effect.widthProperty().set(30.0)
        effect.heightProperty().set(30.0)
        effect.colorProperty().set(Color.valueOf("#e65100"))
        selectedPane = pane
        selectedPane?.effectProperty()?.set(effect)
    }
}