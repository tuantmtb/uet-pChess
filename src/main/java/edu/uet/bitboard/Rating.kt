package edu.uet.bitboard

object Rating {

    /**
     * [ 0, 0, 0, *, *, 0, 0, 0]
     * [ 0,50,50, 5, 5,50,50, 0]
     * [ 0, 5,50,50,50,50, 5, 0]
     * [ 0, 5,20,30,30,20, 5, 0]
     * [ 0, 5,20,30,30,20, 5, 0]
     * [ 0, 0,10,20,20,10, 0, 0]
     * [ 0, 0, 0, 5, 5, 0, 0, 0]
     * [ 0, 0, 0, 0, 0, 0, 0, 0]
     */

    private val WNBoard = arrayOf(
            arrayOf(5, 5, 5, -1, -1, 5, 5, 5),
            arrayOf(5, 50, 50, 5, 5, 50, 50, 5),
            arrayOf(5, 5, 50, 50, 50, 50, 5, 5),
            arrayOf(5, 5, 15, 20, 20, 15, 5, 5),
            arrayOf(5, 5, 15, 20, 20, 15, 5, 5),
            arrayOf(5, 5, 10, 15, 15, 10, 5, 5),
            arrayOf(5, 5, 10, 10, 10, 10, 5, 5),
            arrayOf(5, 5, 5, 2, 2, 5, 5, 5)
    )

    private val BNBoard = arrayOf(
            arrayOf(5, 5, 5, 2, 2, 5, 5, 5),
            arrayOf(5, 5, 10, 10, 10, 10, 5, 5),
            arrayOf(5, 5, 10, 15, 15, 10, 5, 5),
            arrayOf(5, 5, 15, 20, 20, 15, 5, 5),
            arrayOf(5, 5, 15, 20, 20, 15, 5, 5),
            arrayOf(5, 5, 50, 50, 50, 50, 5, 5),
            arrayOf(5, 50, 50, 5, 5, 50, 50, 5),
            arrayOf(5, 5, 5, -1, -1, 5, 5, 5)
    )

    fun evaluate(WN: Long, BN: Long, wScore: Int, bScore: Int): Int {
        return evaluateByPositions(WN, BN) + (wScore - bScore) * 60
    }

    private fun evaluateByPositions(WN: Long, BN: Long): Int {
        var WNPoints = 0
        for (i in 0..63) {
            val point = WNBoard[i / 8][i % 8]
            val bit = if (WN.ushr(i) and 1L == 1L) 1 else 0
            WNPoints += bit * point
        }

        var BNPoints = 0
        for (i in 0..63) {
            val point = BNBoard[i / 8][i % 8]
            val bit = if (BN.ushr(i) and 1L == 1L) 1 else 0
            BNPoints += bit * point
        }

        return WNPoints - BNPoints
    }
}