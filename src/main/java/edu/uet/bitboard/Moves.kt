package edu.uet.bitboard

import javax.xml.stream.events.Characters

object Moves {
    private val FILE_AB = 217020518514230019L
    private val FILE_GH = -4557430888798830400L
    private val KNIGHT_SPAN = 43234889994L
    private var NOT_WHITE_PIECES = 0L
    private var BLACK_PIECES = 0L
    private var NOT_BLACK_PIECES = 0L
    private var WHITE_PIECES = 0L
    private val BLOCK_TOP_ENEMY = 1024L
    private val BLOCK_BOTTOM_ENEMY = 67108864L
    private val BLOCK_RIGHT_ENEMY = 524288L
    private val BLOCK_LEFT_ENEMY = 131072L
    private val BLOCKED_TOP = 10L
    private val BLOCKED_RIGHT = 268439552L
    private val BLOCKED_LEFT = 16777472L
    private val BLOCKED_BOTTOM = 42949672960L
    private val SQUARE_1_POINT_FOR_BLACK = 576460752303423488L
    private val SQUARE_2_POINT_FOR_BLACK = 1152921504606846976L
    private val SQUARE_1_POINT_FOR_WHITE = 16L
    private val SQUARE_2_POINT_FOR_WHITE = 8L
    private val W_TELE_1 = "0477"
    private val W_TELE_2 = "0370"
    private val B_TELE_1 = "7300"
    private val B_TELE_2 = "7407"


    fun possibleMovesW(WN: Long, BN: Long): String {
        NOT_WHITE_PIECES = WN.inv()
        BLACK_PIECES = BN
        NOT_BLACK_PIECES = BN.inv()
        WHITE_PIECES = WN

        return possibleWN(WN)
    }

    fun possibleMovesB(WN: Long, BN: Long): String {
        NOT_WHITE_PIECES = WN.inv()
        BLACK_PIECES = BN
        NOT_BLACK_PIECES = BN.inv()
        WHITE_PIECES = WN

        return possibleBN(BN)
    }

    fun makeMove(board: Long, move: String): Long {
        var result = board

        val start = move[0].toString().toInt() * 8 + move[1].toString().toInt()
        val end = move[2].toString().toInt() * 8 + move[3].toString().toInt()

        if (result.ushr(start) and 1L == 1L) {
            result = result and (1L shl start).inv()
            result = result or (1L shl end)
        } else {
            result = result and (1L shl end).inv()
        }

        return result
    }

    fun teleW(points: Int, WN: Long): Long {
        if (points == 1) {
            return makeMove(WN, W_TELE_1)
        }

        if (points == 2) {
            return makeMove(WN, W_TELE_2)
        }

        return WN
    }

    fun teleB(points: Int, BN: Long): Long {
        if (points == 1) {
            return makeMove(BN, B_TELE_1)
        }

        if (points == 2) {
            return makeMove(BN, B_TELE_2)
        }

        return BN
    }

    fun checkAtPointSquareW(WN: Long): Int {
        if ((WN and SQUARE_1_POINT_FOR_WHITE) != 0L) {
            return 1
        }

        if ((WN and SQUARE_2_POINT_FOR_WHITE) != 0L) {
            return 2
        }

        return 0
    }

    fun checkAtPointSquareB(BN: Long): Int {
        if ((BN and SQUARE_1_POINT_FOR_BLACK) != 0L) {
            return 1
        }

        if ((BN and SQUARE_2_POINT_FOR_BLACK) != 0L) {
            return 2
        }

        return 0
    }

    private fun possibleN(inputBitboard: Long, enemies: Long, notOurPieces: Long): String {
        var bitboard = inputBitboard
        var list = ""
        var i = bitboard and (bitboard - 1).inv()
        var possibility: Long
        var blockedTop = 0L
        var blockedRight = 0L
        var blockedBottom = 0L
        var blockedLeft = 0L

        while (i != 0L) {
            val iLocation = java.lang.Long.numberOfTrailingZeros(i)

            if (iLocation > 18) {

                val shiftAmount = iLocation - 18

                possibility = KNIGHT_SPAN shl shiftAmount

                if ((enemies and (BLOCK_TOP_ENEMY shl shiftAmount)) != 0L) {
                    blockedTop = BLOCKED_TOP shl shiftAmount
                }

                if ((enemies and (BLOCK_RIGHT_ENEMY shl shiftAmount)) != 0L) {
                    blockedRight = BLOCKED_RIGHT shl shiftAmount
                }

                if ((enemies and (BLOCK_BOTTOM_ENEMY shl shiftAmount)) != 0L) {
                    blockedBottom = BLOCKED_BOTTOM shl shiftAmount
                }

                if ((enemies and (BLOCK_LEFT_ENEMY shl shiftAmount)) != 0L) {
                    blockedLeft = BLOCKED_LEFT shl shiftAmount
                }

            } else {

                val shiftAmount = 18 - iLocation

                possibility = KNIGHT_SPAN shr shiftAmount

                if ((enemies and (BLOCK_TOP_ENEMY shr shiftAmount)) != 0L) {
                    blockedTop = BLOCKED_TOP shr shiftAmount
                }

                if ((enemies and (BLOCK_RIGHT_ENEMY shr shiftAmount)) != 0L) {
                    blockedRight = BLOCKED_RIGHT shr shiftAmount
                }

                if ((enemies and (BLOCK_BOTTOM_ENEMY shr shiftAmount)) != 0L) {
                    blockedBottom = BLOCKED_BOTTOM shr shiftAmount
                }

                if ((enemies and (BLOCK_LEFT_ENEMY shr shiftAmount)) != 0L) {
                    blockedLeft = BLOCKED_LEFT shr shiftAmount
                }

            }

            possibility = possibility and blockedTop.inv() and blockedRight.inv() and blockedBottom.inv() and blockedLeft.inv()

            possibility = if (iLocation % 8 < 4) {
                possibility and (FILE_GH.inv() and notOurPieces)
            } else {
                possibility and (FILE_AB.inv() and notOurPieces)
            }

            var j = possibility and (possibility - 1).inv()

            while (j != 0L) {
                val index = java.lang.Long.numberOfTrailingZeros(j)
                list += "" + iLocation / 8 + iLocation % 8 + index / 8 + index % 8
                possibility = possibility and j.inv()
                j = possibility and (possibility - 1).inv()
            }

            bitboard = bitboard and i.inv()
            i = bitboard and (bitboard - 1).inv()
        }

        return list
    }

    private fun possibleWN(inputWN: Long): String {
        return possibleN(inputWN, BLACK_PIECES, NOT_WHITE_PIECES)
    }

    private fun possibleBN(inputBN: Long): String {
        return possibleN(inputBN, WHITE_PIECES, NOT_BLACK_PIECES)
    }

}