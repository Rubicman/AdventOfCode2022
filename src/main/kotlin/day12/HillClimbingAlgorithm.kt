package day12

import lines

class HillClimbingAlgorithm {
    fun findWay() {
        val grid = lines().toList()
        val distances = Array(grid.size) { Array(grid[0].length) { Int.MAX_VALUE } }
        val queue = ArrayDeque<Pair<Int, Int>>()

        for (i in distances.indices) {
            for (j in distances[0].indices) {
                if (grid[i][j] == 'S') {
                    queue.addLast(i to j)
                    distances[i][j] = 0
                }
            }
        }

        while (queue.isNotEmpty()) {
            val (i, j) = queue.removeFirst()
            MOVES.map { it.first + i to it.second + j }.forEach { (di, dj) ->
                if (distances.get(i, j) + 1 < distances.get(di, dj)
                    && distance(grid[di][dj]) - distance(grid[i][j]) < 2
                ) {
                    distances[di][dj] = distances[i][j] + 1
                    queue.addLast(di to dj)
                }
            }
        }

        for (i in distances.indices) {
            for (j in distances[0].indices) {
                if (grid[i][j] == 'E') println(distances[i][j])
            }
        }
    }

    private fun Array<Array<Int>>.get(i: Int, j: Int): Int {
        return if (i in indices && j in get(0).indices) {
            get(i)[j]
        } else {
            Int.MIN_VALUE
        }
    }

    private fun distance(height: Char) = when (height) {
        'S' -> 0
        'E' -> 25
        else -> height.code - 'a'.code
    }

    companion object {
        private val MOVES = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
    }
}