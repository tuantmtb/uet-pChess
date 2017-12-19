package edu.uet.view

import edu.uet.ChessConfig
import edu.uet.GameDispatcher
import edu.uet.GameMaster
import edu.uet.entity.ChessPiece
import edu.uet.entity.ChessPiece.Position
import edu.uet.entity.ChessSide
import edu.uet.view.themes.Default
import javafx.application.Platform
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.effect.BlurType
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import javafx.scene.image.ImageView
import javafx.scene.input.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.hide
import tornadofx.information
import tornadofx.show

class GameView : BaseView() {
    override val root: Pane by fxml("/Game.fxml")

    private val boardGrid: GridPane by fxid("board")
    private val bName: Label by fxid("bName")
    private val wName: Label by fxid("wName")
    private val bPoint: Label by fxid("bPoint")
    private val wPoint: Label by fxid("wPoint")
    private val bCDSection: Pane by fxid("bCDSection")
    private val bCDLabel: Label by fxid("bCDLabel")
    private val bCDProgress: ProgressBar by fxid("bCDProgress")
    private val wCDSection: Pane by fxid("wCDSection")
    private val wCDLabel: Label by fxid("wCDLabel")
    private val wCDProgress: ProgressBar by fxid("wCDProgress")
    private val rightSideBar: VBox by fxid("rightSideBar")

    private var game = GameMaster()
    private var nextPane: Pane? = null
    private val pieceMap =  HashMap<ChessPiece, ImageView>()
    private val posMap = HashMap<String, Pane>()
    private val possibleNextPos = arrayListOf<Position>()
    private var draggingPiece: ChessPiece? = null

    private val theme = Default

    init {
        initBoardGrid()
        initRightSideBar()
        bind()
        game.newGame()
    }

    private fun initBoardGrid() {
        // Clear demo
        boardGrid.columnConstraints.clear()
        boardGrid.rowConstraints.clear()
        boardGrid.children.clear()

        boardGrid.prefWidth = Styles.GRID_SIZE * game.board.size.width
        boardGrid.prefHeight = Styles.GRID_SIZE * game.board.size.height

        (0 until game.board.size.width).forEach {
            val c = ColumnConstraints()
            c.prefWidth = Styles.GRID_SIZE
            boardGrid.columnConstraints.add(c)
        }

        (0 until game.board.size.height).forEach { row ->
            val r = RowConstraints()
            r.prefHeight = Styles.GRID_SIZE
            boardGrid.rowConstraints.add(r)

            (0 until game.board.size.width).forEach { col ->
                val p = AnchorPane()
                val color = if ((row + col) % 2 == 0) theme.BACKGROUND_DARK_COLOR else theme.BACKGROUND_LIGHT_COLOR
                p.style = "-fx-background-color: $color;"
                p.prefHeight = Styles.GRID_SIZE
                p.prefWidth = Styles.GRID_SIZE

                val pos = Position(game.board.size.width - 1 - col, game.board.size.height - 1 - row)
                p.userData = pos
                p.setOnDragOver { onPositionDragOver(it) }
                p.setOnDragDone { onChessPieceDragDone(it) }
                posMap.put(pos.toString(), p as Pane)

                boardGrid.add(p, col, row)

                val imgView = initEarnedPointsPosition(pos)
                if (imgView != null) {
                    p.children.add(imgView)
                }
            }
        }
    }

    private fun initRightSideBar() {
        rightSideBar.prefHeight = boardGrid.prefHeight
        rightSideBar.children.map { it as VBox }.forEach { it.prefHeight = rightSideBar.prefHeight/2 }
    }

    private fun initEarnedPointsPosition(pos: Position): ImageView? {
        val wEarn = pos.earnedPointsForSide(ChessSide.WHITE, game.board.size)
        val bEarn = pos.earnedPointsForSide(ChessSide.BLACK, game.board.size)
        if (wEarn != 0 || bEarn != 0) {
            val imgView = ImageView(
                    when (wEarn) {
                        ChessConfig.POINT_1 -> "W1.png"
                        ChessConfig.POINT_2 -> "W2.png"
                        else -> when (bEarn) {
                            ChessConfig.POINT_1 -> "B1.png"
                            ChessConfig.POINT_2 -> "B2.png"
                            else -> null // won't happen
                        }
                    }
            )
            imgView.fitWidth = Styles.GRID_SIZE
            imgView.fitHeight = Styles.GRID_SIZE
            imgView.mouseTransparentProperty().set(true)
            return imgView
        } else {
            return null
        }
    }

    private fun placePiecesOnBoard() {
        game.board.pieces.forEach {
            // Đặt các quân cờ vào các ô
            val imageView = ImageView(if (it.chessSide == ChessSide.BLACK) "BN.png" else "WN.png")
            imageView.fitHeight = Styles.GRID_SIZE
            imageView.fitWidth = Styles.GRID_SIZE
            imageView.userData = it
            imageView.setOnDragDetected { onChessPieceDragDetected(it) }
            if (game.isTurnOf(it.chessSide)) {
                Platform.runLater { imageView.cursor = Cursor.OPEN_HAND }
            } else {
                Platform.runLater { imageView.cursor = Cursor.DEFAULT }
            }
            val pane = posMap[it.position.toString()]
            Platform.runLater { pane!!.children.add(imageView) }
            pieceMap.put(it, imageView)
        }
    }

    private fun bind() {
        GameDispatcher.listen("WHITE_POINT_CHANGED", {
            Platform.runLater { wPoint.text = it.newValue.toString() }
        })
        GameDispatcher.listen("BLACK_POINT_CHANGED", {
            Platform.runLater { bPoint.text = it.newValue.toString() }
        })
        GameDispatcher.listen("TURN_SWITCHED", { updateTurn() })
        GameDispatcher.listen("PIECE_DIED", {
            val pane = pieceMap[it.oldValue as ChessPiece]!!.parent as Pane
            removePiece(pane)
            pieceMap.remove(it.oldValue as ChessPiece)
        })
        GameDispatcher.listen("PIECE_MOVED", {
            val oldPos = it.oldValue as Position
            val oldPane = posMap[oldPos.toString()]!!
            val newPos = it.newValue as Position
            val newPane = posMap[newPos.toString()]!!
            val imgView = movePiece(oldPane, newPane)
            val piece = imgView.userData as ChessPiece
            pieceMap[piece] = imgView
        })
        GameDispatcher.listen("WINNER", {
            pieceMap.values.forEach {
                Platform.runLater { it.cursor = Cursor.DEFAULT }
            }
            information("Người chiến thắng: " + game.winner()!!.name)
        })
        GameDispatcher.listen("PIECES_PLACED", {
            placePiecesOnBoard()
        })
        GameDispatcher.listen("COUNT_DOWN_TICK", {
            val value = it.newValue as Int
            val progress = value.toDouble() / ChessConfig.COUNT_DOWN
            if (game.isTurnOf(ChessSide.WHITE)) {
                Platform.runLater {
                    wCDLabel.text = value.toString()
                    wCDProgress.progress = progress
                }
            } else {
                Platform.runLater {
                    bCDLabel.text = value.toString()
                    bCDProgress.progress = progress
                }
            }
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
        Platform.runLater { to.children.add(imgView) }
        return imgView
    }

    private fun removePiece(pane: Pane) : ImageView {
        val imgView = pane.children.last { it is ImageView } as ImageView
        Platform.runLater { pane.children.remove(imgView) }
        return imgView
    }

    private fun updateTurn() {
        if (game.isTurnOf(ChessSide.BLACK)) {
            Platform.runLater {
                bName.textFill = Paint.valueOf("red")
                wName.textFill = Paint.valueOf("black")
                bCDSection.show()
                wCDSection.hide()
            }
        } else {
            Platform.runLater {
                bName.textFill = Paint.valueOf("black")
                wName.textFill = Paint.valueOf("red")
                bCDSection.hide()
                wCDSection.show()
            }
        }
        pieceMap.forEach { piece, imgView ->
            if (game.isTurnOf(piece.chessSide)) {
                Platform.runLater { imgView.cursor = Cursor.OPEN_HAND }
            } else {
                Platform.runLater { imgView.cursor = Cursor.DEFAULT }
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