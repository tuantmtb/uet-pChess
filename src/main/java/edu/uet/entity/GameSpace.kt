package edu.uet.entity


/**
 * AI
 */
class GameSpace(val root: GameSpaceNode) {
    fun generateWithDepth(height: Int) {

        var nodesInCurrentLevel = arrayListOf(root)

        for (i in 1..height) {
            val nodesInNextLevel = ArrayList<GameSpaceNode>()

            nodesInCurrentLevel.forEach { node ->
                node.generateChildren()
                nodesInNextLevel.addAll(node.children)
            }

            nodesInCurrentLevel = nodesInNextLevel
        }
    }
}