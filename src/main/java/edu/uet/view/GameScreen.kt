package edu.uet.view

import edu.uet.GameMaster
import edu.uet.entity.ChessPiece.Position
import edu.uet.entity.ChessSide
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
import tornadofx.*

class GameScreen: View() {
    override val root: SplitPane by fxml("/GameScreen.fxml")
    private val boardUI: GridPane by fxid("board")
    private var selectedPane: Pane? = null
    private var game: GameMaster = GameMaster()

    init {
        titleProperty.set("Cờ điểm UET")

        boardUI.getChildList()?.forEach {
            // set position cho các ô
            val pos = Position(GridPane.getRowIndex(it), GridPane.getColumnIndex(it))
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
            }
        }
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
                    game.board.move(
                            currentPiece, targetPosition,
                            {
                                targetPane.children.clear()
                            },
                            null,
                            {
                                movePiece(selectedPane, targetPane)
                                deselect()
                            }
                    )
                }
            }
        }
    }

    private fun movePiece(from: Pane?, to: Pane) {
        val imageView = from?.children?.first()
        from?.children?.clear()
        to.children.add(imageView)
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