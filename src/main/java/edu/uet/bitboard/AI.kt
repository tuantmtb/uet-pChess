package edu.uet.bitboard

class AI(var WN: Long, var BN: Long, var wScore: Int, var bScore: Int, private val searchDepth: Int, val zobrist: Zobrist) {

    private val MATE_SCORE = 5000

    private fun bitCount(number: Long) = java.lang.Long.bitCount(number)

    private fun zWSearch(beta: Int, WN: Long, BN: Long, whiteToMove: Boolean, depth: Int, wScore: Int, bScore: Int): Int {
        var score = Int.MIN_VALUE

        //alpha == beta - 1
        //this is either a cut- or all-node

        if (depth == searchDepth) {
            score = Rating.evaluate(WN, BN, wScore, bScore)
            return score
        }

        var moves = if (whiteToMove) {
            Moves.possibleMovesW(WN, BN)
        } else {
            Moves.possibleMovesB(WN, BN)
        }

        moves = sortMoves(moves, whiteToMove, WN, BN, wScore, bScore)

        var i = 0
        while (i < moves.length) {
            val move = moves.substring(i, i + 4)

            val moveResult = makeMove(move, whiteToMove, WN, BN, wScore, bScore)

            score = -zWSearch(1 - beta, moveResult.WN, moveResult.BN, !whiteToMove, depth + 1, moveResult.wScore, moveResult.bScore)

            if (score >= beta) {
                return score// fail-hard beta-cutoff
            }

            i += 4
        }

        return beta - 1// beta - 1 // fail-hard, return alpha
    }

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

    fun pvSearch(alpha: Int, beta: Int, WN: Long, BN: Long, whiteToMove: Boolean, depth: Int, wScore: Int, bScore: Int): SearchResult {
        var bestScore: Int
        var nextAlpha = alpha


        if (depth == searchDepth) {
            bestScore = Rating.evaluate(WN, BN, wScore, bScore)
            println("Choose: ")
            return SearchResult("", bestScore)
        }

        var moves = if (whiteToMove) {
            Moves.possibleMovesW(WN, BN)
        } else {
            Moves.possibleMovesB(WN, BN)
        }

        moves = sortMoves(moves, whiteToMove, WN, BN, wScore, bScore)

        printMoves(moves, depth)
        println("=================")

        if (moves.isEmpty()) {
            return if (whiteToMove) {
                println("Choose: ")
                SearchResult("", MATE_SCORE)
            } else {
                println("Choose: ")
                SearchResult("", -MATE_SCORE)
            }
        }

        val firstMove = moves.substring(0, 4)

        val firstMoveResult = makeMove(firstMove, whiteToMove, WN, BN, wScore, bScore)

        val searchResult = pvSearch(-beta, -nextAlpha, firstMoveResult.WN, firstMoveResult.BN, !whiteToMove, depth + 1, firstMoveResult.wScore, firstMoveResult.bScore)

        bestScore = -searchResult.score

        if (Math.abs(bestScore) == MATE_SCORE) {
            println("Choose: " + firstMove)
            return SearchResult(firstMove, bestScore)
        }

        if (bestScore > alpha) {
            if (bestScore >= beta) {
                //This is a refutation move
                //It is not a PV move
                //However, it will usually cause a cutoff so it can
                //be considered a best move if no other move is found
                println("Choose: " + firstMove)
                return SearchResult(firstMove, bestScore)
            }
            nextAlpha = bestScore
        }


        // Try with other moves

        var bestMove = firstMove
        var i = 4
        while (i < moves.length) {
            var score: Int
            val move = moves.substring(i, i + 4)

            val moveResult = makeMove(move, whiteToMove, WN, BN, wScore, bScore)

            score = -zWSearch(-nextAlpha, moveResult.WN, moveResult.BN, !whiteToMove, depth + 1, wScore, bScore)


            if ((score > nextAlpha) && (score < beta)) {
                //research with window [alpha;beta]
                val pvSearchResult = pvSearch(-beta, -nextAlpha, WN, BN, !whiteToMove, depth + 1, moveResult.wScore, moveResult.bScore)

                bestScore = -pvSearchResult.score

                if (score > nextAlpha) {
                    bestMove = move
                    nextAlpha = score
                }
            }

            if (score != Int.MIN_VALUE && score > bestScore) {
                if (score >= beta) {
                    println("Choose: " + move)
                    return SearchResult(move, score)
                }

                bestScore = score
                if (Math.abs(bestScore) == MATE_SCORE) {
                    println("Choose: " + move)
                    return SearchResult(move, bestScore)
                }
            }

            i += 4
        }

        println("Choose: " + bestMove)
        return SearchResult(bestMove, bestScore)
    }

    fun alphaBetaSearch(alpha: Int, beta: Int, WN: Long, BN: Long, whiteToMove: Boolean, depth: Int, wScore: Int, bScore: Int): SearchResult {
        var bestScore = Int.MIN_VALUE
        var tAlpha = alpha


        if (depth == searchDepth) {
            val evaluated = Rating.evaluate(WN, BN, wScore, bScore)

            bestScore = if (whiteToMove) {
                evaluated
            } else {
                -evaluated
            }


//            bestScore = evaluated

            return SearchResult("", bestScore)
        }

        val found = zobrist.checkPosition(WN, BN, whiteToMove)

        found?.let {
            return found
        }

        var moves = if (whiteToMove) {
            Moves.possibleMovesW(WN, BN)
        } else {
            Moves.possibleMovesB(WN, BN)
        }

        moves = sortMoves(moves, whiteToMove, WN, BN, wScore, bScore)

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
            val searchResult = alphaBetaSearch(-beta, -tAlpha, moveResult.WN, moveResult.BN, !whiteToMove, depth + 1, moveResult.wScore, moveResult.bScore)

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

        zobrist.addPosition(WN, BN, whiteToMove, result)

        return result
    }

    private fun sortMoves(moves: String, whiteToMove: Boolean, WN: Long, BN: Long, wScore: Int, bScore: Int): String {

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

            val evaluated = Rating.evaluate(moveResult.WN, moveResult.BN, moveResult.wScore, moveResult.bScore)

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

    private fun printMoves(moves: String, depth: Int) {
        for (i in 0..(moves.length / 4 - 1)) {
            println(getSpaces(depth) + "(" + moves[4 * i] + "," + moves[4 * i + 1] + ") -> (" + moves[4 * i + 2] + "," + moves[4 * i + 3] + ")")
        }
    }

    private fun getSpaces(depth: Int): String {
        var spaces = ""

        for (i in 0..(depth - 1)) {
            spaces += "\t"
        }

        return spaces
    }
}