package edu.uet.bitboard

object Moves {
    private val FILE_AB = 217020518514230019L
    private val FILE_GH = -4557430888798830400L
    private val KNIGHT_SPAN = 43234889994L
    private var NOT_WHITE_PIECES = 0L
    private var BLACK_PIECES = 0L
    private val BLOCK_TOP_ENEMY = 1024L
    private val BLOCK_BOTTOM_ENEMY = 67108864L
    private val BLOCK_RIGHT_ENEMY = 524288L
    private val BLOCK_LEFT_ENEMY = 131072L
    private val BLOCKED_TOP = 10L
    private val BLOCKED_RIGHT = 268439552L
    private val BLOCKED_LEFT = 16777472L
    private val BLOCKED_BOTTOM = 42949672960L


    fun possibleMovesW(bitBoardPair: BitBoardPair): String {
        NOT_WHITE_PIECES = bitBoardPair.WN.inv()
        BLACK_PIECES = bitBoardPair.BN
        return possibleWN(bitBoardPair.WN)
    }

    private fun possibleWN(inputWN: Long): String {
        var WN = inputWN
        var list = ""
        var i = WN and (WN - 1).inv()
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

                if ((BLACK_PIECES and (BLOCK_TOP_ENEMY shl shiftAmount)) != 0L) {
                    blockedTop = BLOCKED_TOP shl shiftAmount
                }

                if ((BLACK_PIECES and (BLOCK_RIGHT_ENEMY shl shiftAmount)) != 0L) {
                    blockedRight = BLOCKED_RIGHT shl shiftAmount
                }

                if ((BLACK_PIECES and (BLOCK_BOTTOM_ENEMY shl shiftAmount)) != 0L) {
                    blockedBottom = BLOCKED_BOTTOM shl shiftAmount
                }

                if ((BLACK_PIECES and (BLOCK_LEFT_ENEMY shl shiftAmount)) != 0L) {
                    blockedLeft = BLOCKED_LEFT shl shiftAmount
                }

            } else {

                val shiftAmount = 18 - iLocation

                possibility = KNIGHT_SPAN shr shiftAmount

                if ((BLACK_PIECES and (BLOCK_TOP_ENEMY shr shiftAmount)) != 0L) {
                    blockedTop = BLOCKED_TOP shr shiftAmount
                }

                if ((BLACK_PIECES and (BLOCK_RIGHT_ENEMY shr shiftAmount)) != 0L) {
                    blockedRight = BLOCKED_RIGHT shr shiftAmount
                }

                if ((BLACK_PIECES and (BLOCK_BOTTOM_ENEMY shr shiftAmount)) != 0L) {
                    blockedBottom = BLOCKED_BOTTOM shr shiftAmount
                }

                if ((BLACK_PIECES and (BLOCK_LEFT_ENEMY shr shiftAmount)) != 0L) {
                    blockedLeft = BLOCKED_LEFT shr shiftAmount
                }

            }

            possibility = possibility and blockedTop.inv() and blockedRight.inv() and blockedBottom.inv() and blockedLeft.inv()

            possibility = if (iLocation % 8 < 4) {
                possibility and (FILE_GH.inv() and NOT_WHITE_PIECES)
            } else {
                possibility and (FILE_AB.inv() and NOT_WHITE_PIECES)
            }

            var j = possibility and (possibility - 1).inv()

            while (j != 0L) {
                val index = java.lang.Long.numberOfTrailingZeros(j)
                list += "" + iLocation / 8 + iLocation % 8 + index / 8 + index % 8
                possibility = possibility and j.inv()
                j = possibility and (possibility - 1).inv()
            }

            WN = WN and i.inv()
            i = WN and (WN - 1).inv()
        }

        return list
    }
}