package day14

import lines

class RegolithReservoir {
    fun sandSimulation() {
        val paths = lines()
            .map { line ->
                line
                    .split(" -> ")
                    .map { string ->
                        string
                            .split(",")
                            .map(String::toInt)
                            .let { it[0] to it[1] }
                    }
            }
            .toList()

        val maxCoordinates = paths
            .flatten()
            .plus(START_POINT)
            .reduce { (a1, b1), (a2, b2) -> maxOf(a1, a2) to maxOf(b1, b2) }
        val minCoordinates = paths
            .flatten()
            .plus(START_POINT)
            .reduce { (a1, b1), (a2, b2) -> minOf(a1, a2) to minOf(b1, b2) }

        val cave =
            Array(LENGTH) { Array(maxCoordinates.second + 4) { 0 } }

        paths
            .plus(listOf(listOf(minCoordinates.first - LENGTH / 2 to maxCoordinates.second + 2, minCoordinates.first + LENGTH / 2 - 1 to maxCoordinates.second + 2)))
            .forEach { path: List<Pair<Int, Int>> ->
                path
                    .map { (x, y) -> x - minCoordinates.first + LENGTH / 2 to y - minCoordinates.second + 1 }
                    .zipWithNext()
                    .forEach { (start, end) ->
                        if (start.first == end.first) {
                            range(start.second, end.second).forEach { cave[start.first][it] = 2 }
                        } else {
                            range(start.first, end.first).forEach { cave[it][start.second] = 2 }
                        }
                    }
            }

        var k = -1
        mainLoop@ while (true) {
            var x = START_POINT.first - minCoordinates.first + LENGTH / 2
            var y = START_POINT.second - minCoordinates.second + 1
            k++
            if (cave[x][y] != 0)
                break

            while (true) {
                when {
                    cave[x][y + 1] == 0 -> {}
                    cave[x - 1][y + 1] == 0 -> x--
                    cave[x + 1][y + 1] == 0 -> x++
                    else -> {
                        cave[x][y] = 1
                        continue@mainLoop
                    }
                }
                y++
            }
        }

        cave.print()
        println(k)
    }

    private fun range(a: Int, b: Int) = (minOf(a, b)..maxOf(a, b))

    private fun Array<Array<Int>>.print() = transpose()
        .forEach { line ->
            line.joinToString("") {
                when (it) {
                    0 -> "."
                    1 -> "o"
                    2 -> "#"
                    else -> "?"
                }
            }.let(::println)
        }

    private inline fun <reified T> Array<Array<T>>.transpose(): Array<Array<T>> {
        val cols = get(0).size
        val rows = size
        return Array(cols) { j ->
            Array(rows) { i ->
                get(i)[j]
            }
        }
    }

    companion object {
        private val START_POINT = 500 to 0
        private const val LENGTH = 1_000
    }
}