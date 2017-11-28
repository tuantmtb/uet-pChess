package edu.uet.bitboard

object Rating {
    private fun bitCount(number: Long) = java.lang.Long.bitCount(number)

    fun evaluate(WN: Long, BN: Long, wScore: Int, bScore: Int): Int {
        return (bitCount(WN) - bitCount(BN)) * 2 + wScore - bScore
    }
}