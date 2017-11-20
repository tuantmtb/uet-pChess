package edu.uet.bitboard

object BoardGeneration {
    fun initiateStandardBoard(): BitBoardPair {
        val chessBoard = arrayOf(
                arrayOf("n", "n", "n", " ", " ", "n", "n", "n"),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
                arrayOf("N", "N", "N", " ", " ", "N", "N", "N"))

        return arrayToBitBoards(chessBoard)
    }

    fun arrayToBitBoards(chessBoard: Array<Array<String>>): BitBoardPair {

        var WN = 0L
        var WB = 0L

        var binary: String
        for (i in 0..63) {
            binary = "0000000000000000000000000000000000000000000000000000000000000000"
            binary = binary.substring(i + 1) + "1" + binary.substring(0, i)

            when (chessBoard[i / 8][i % 8]) {
                "N" -> {
                    WN += convertStringToBitboard(binary)
                }

                "n" -> {
                    WB += convertStringToBitboard(binary)
                }
            }
        }

        return BitBoardPair(WN, WB)
    }

    private fun convertStringToBitboard(Binary: String): Long {
        return if (Binary[0] == '0') {//not going to be a negative number
            Binary.toLong(2)
//            java.lang.Long.parseLong(Binary, 2)
        } else {
            ("1" + Binary.substring(2)).toLong(2) * 2
//            java.lang.Long.parseLong("1" + Binary.substring(2), 2) * 2
        }
    }


}