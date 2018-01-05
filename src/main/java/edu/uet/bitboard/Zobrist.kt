package edu.uet.bitboard

import java.security.SecureRandom

class Zobrist() {
    var zArray = ArrayList<ArrayList<Long>>()
    var zBlackMove: Long
    private val hashArray = ArrayList<Long>()
    val resultArray = ArrayList<SearchResult>()
    private val WNArray = ArrayList<Long>()
    private val BNArray = ArrayList<Long>()
    private val whiteToMoveArray = ArrayList<Boolean>()

    private fun random64(): Long {
        val random = SecureRandom()
        return random.nextLong()
    }

    init {
        for (color in 0..1) {
            val colorArray = ArrayList<Long>()
            for (square in 0..63) {
                colorArray.add(random64())
            }

            zArray.add(colorArray)
        }

        zBlackMove = random64()
    }


    private fun getZobristHash(WN: Long, BN: Long, whiteToMove: Boolean): Long {
        var returnZKey = 0L
        for (square in 0..63) {
            if (((WN shr square) and 1L) == 1L) {
                returnZKey = returnZKey xor zArray[0][square]
            } else if (((BN shr square) and 1L) == 1L) {
                returnZKey = returnZKey xor zArray[1][square]
            }
        }

        if (!whiteToMove) {
            returnZKey = returnZKey xor zBlackMove
        }

        return returnZKey
    }

    fun checkPosition(WN: Long, BN: Long, whiteToMove: Boolean): SearchResult? {
        val hash = getZobristHash(WN, BN, whiteToMove)

        val index = (0..(hashArray.size - 1))
                .find { hash == hashArray[it] }

        return if (index !== null
                && whiteToMoveArray[index] == whiteToMove
                && WNArray[index] == WN
                && BNArray[index] == BN) {
            resultArray[index]
        } else {
            null
        }
    }

    fun addPosition(WN: Long, BN: Long, whiteToMove: Boolean, result: SearchResult, index: Int) {
        val hash = getZobristHash(WN, BN, whiteToMove)

        if (index != -1) {
            hashArray[index] = hash
            resultArray[index] = result
            whiteToMoveArray[index] = whiteToMove
            WNArray[index] = WN
            BNArray[index] = BN
        } else {
            hashArray.add(hash)
            resultArray.add(result)
            whiteToMoveArray.add(whiteToMove)
            WNArray.add(WN)
            BNArray.add(BN)
        }
    }
}