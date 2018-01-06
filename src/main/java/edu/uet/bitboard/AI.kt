package edu.uet.bitboard

class AI(val zobrist: Zobrist) {

    private val MATE_SCORE = 5000
    private var searchDepth = 0

    private fun bitCount(number: Long) = java.lang.Long.bitCount(number)

    fun makeMove(move: String, whiteToMove: Boolean, WN: Long, BN: Long, wScore: Int, bScore: Int): MoveResult {
        var newWScore = wScore
        var newBScore = bScore

        var WNt = Moves.makeMove(WN, move)
        var BNt = Moves.makeMove(BN, move)

        if (whiteToMove) {

            newWScore += bitCount(BN) - bitCount(BNt)

            val points = Moves.checkAtPointSquareW(WNt)

            newWScore += points


            val before = bitCount(BNt)

            WNt = Moves.teleW(points, WNt)
            BNt = Moves.teleW(points, BNt)

            val after = bitCount(BNt)

            newWScore += before - after

        } else {

            newBScore += bitCount(WN) - bitCount(WNt)

            val points = Moves.checkAtPointSquareB(BNt)

            newBScore += points

            val before = bitCount(WNt)

            WNt = Moves.teleB(points, WNt)
            BNt = Moves.teleB(points, BNt)

            val after = bitCount(WNt)

            newBScore += before - after
        }


        return MoveResult(WNt, BNt, newWScore, newBScore)
    }

    fun findNextMove(searchDepth: Int, WN: Long, BN: Long, whiteToMove: Boolean, wScore: Int, bScore: Int, winPoint: Int = -1): String {
        this.searchDepth = searchDepth
        val result = alphaBetaSearch(-1000, 1000, WN, BN, whiteToMove, 0, wScore, bScore, winPoint)

        println("Score: " + result.score)

        return result.move
    }

    fun alphaBetaSearch(alpha: Int, beta: Int, WN: Long, BN: Long, whiteToMove: Boolean, depth: Int, wScore: Int, bScore: Int, winPoint: Int = -1): SearchResult {
        var bestScore = Int.MIN_VALUE
        var tAlpha = alpha


        if (winPoint > -1) {
            if (whiteToMove && bScore == winPoint) {
                return SearchResult("", -MATE_SCORE)
            }

            if (!whiteToMove && wScore == winPoint) {
                return SearchResult("", -MATE_SCORE)
            }
        }

        if (depth == searchDepth) {
            val evaluated = Rating.evaluate(WN, BN, wScore, bScore, winPoint)

            bestScore = if (whiteToMove) {
                evaluated
            } else {
                -evaluated
            }


//            bestScore = evaluated

            return SearchResult("", bestScore)
        }

        val found = zobrist.checkPosition(WN, BN, whiteToMove)
        val foundIndex = zobrist.resultArray.indexOf(found)

        found?.let {
            return found
        }

        var moves = if (whiteToMove) {
            Moves.possibleMovesW(WN, BN)
        } else {
            Moves.possibleMovesB(WN, BN)
        }

        moves = sortMoves(moves, whiteToMove, WN, BN, wScore, bScore, winPoint)

        if (moves.isEmpty()) {
//            return if (whiteToMove) {
//                SearchResult("", MATE_SCORE)
//            } else {
//                SearchResult("", -MATE_SCORE)
//            }

            return SearchResult("", -MATE_SCORE)
        }

        var bestMove = moves.substring(0, 4)

        var i = 0
        while (i < moves.length) {
            val move = moves.substring(i, i + 4)
            val moveResult = makeMove(move, whiteToMove, WN, BN, wScore, bScore)
            val searchResult = alphaBetaSearch(-beta, -tAlpha, moveResult.WN, moveResult.BN, !whiteToMove, depth + 1, moveResult.wScore, moveResult.bScore, winPoint)

            val score = -searchResult.score

            if (score > bestScore) {
                bestScore = score
                if (bestScore > beta) {
                    // Beta cut-off
                    return SearchResult(move, bestScore)
                } else {
                    tAlpha = bestScore
                    bestMove = move
                }
            }

            i += 4
        }

        val result = SearchResult(bestMove, bestScore)

        zobrist.addPosition(WN, BN, whiteToMove, result, foundIndex)

        return result
    }

    private fun sortMoves(moves: String, whiteToMove: Boolean, WN: Long, BN: Long, wScore: Int, bScore: Int, winPoint:Int = -1): String {

        if (moves.isEmpty()) {
            return moves
        }

        val moveArray = ArrayList<String>()
        val scoreArray = ArrayList<Int>()
        var i = 0

        while (i < moves.length) {
            val move = moves.substring(i, i + 4)
            moveArray.add(move)

            val moveResult = makeMove(move, whiteToMove, WN, BN, wScore, bScore)

            val evaluated = Rating.evaluate(moveResult.WN, moveResult.BN, moveResult.wScore, moveResult.bScore, winPoint)

            val score = if (whiteToMove) {
                evaluated
            } else {
                -evaluated
            }

            scoreArray.add(score)
            i += 4
        }

        val sortedArray = moveArray.sortedWith(compareByDescending({
            scoreArray.get(moveArray.indexOf(it))
        }))

        var result = ""

        for (move in sortedArray) {
            result += move
        }

        return result
    }
}