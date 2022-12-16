package day16

import lines

class ProboscideaVolcanium {
    fun openValves() {
        val nameToNode = mutableMapOf<String, Node>()
        val nameToNext = mutableMapOf<String, List<String>>()
        val nodes = lines()
            .map { it.split(" ", limit = 10) }
            .mapIndexed { index, line ->
                val name = line[1]
                val value = line[4]
                val node = Node(name, index, value.substring(5, value.length - 1).toInt())
                nameToNode[name] = node
                nameToNext[name] = line[9].split(", ")
                node
            }.toList()
        nodes.forEach { node ->
            node.nextNodes.addAll(nameToNext.getValue(node.name).map { nameToNode.getValue(it) })
        }

        val shortestWays = Array(nodes.size) { Array(nodes.size) { nodes.size } }
        nodes.forEach { a ->
            a.nextNodes.forEach { b -> shortestWays[a.index][b.index] = 1 }
            shortestWays[a.index][a.index] = 0
        }
        for (k in shortestWays.indices) {
            for (i in shortestWays.indices) {
                for (j in shortestWays.indices) {
                    shortestWays[i][j] = minOf(shortestWays[i][j], shortestWays[i][k] + shortestWays[k][j])
                }
            }
        }

        println(Dfs(nodes.filter { it.value > 0 }, shortestWays, nameToNode.getValue("AA"))())
    }

    private class Dfs(
        private val nodes: List<Node>,
        private val shortestWays: Array<Array<Int>>,
        private val startNode: Node,
    ) {
        operator fun invoke(node: Node = startNode, time: Int = TOTAL_MINUTES, isElephant: Boolean = false): Long {
            var bestResult = 0L
            for (nextNode in nodes) {
                if (nextNode.isOpen) continue
                nextNode.isOpen = true

                val newTime = time - shortestWays[node.index][nextNode.index] - 1
                if (newTime >= 0) {
                    bestResult = maxOf(bestResult, invoke(nextNode, newTime, isElephant) + newTime * nextNode.value)
                }

                nextNode.isOpen = false
            }
            val elephantResult = if (isElephant) 0L else invoke(isElephant = true)
            return maxOf(bestResult, elephantResult)
        }

        companion object {
            private const val TOTAL_MINUTES = 26
        }
    }
}

data class Node(
    val name: String,
    val index: Int,
    val value: Int,
) {
    val nextNodes: MutableList<Node> = mutableListOf()
    var isOpen: Boolean = false
}