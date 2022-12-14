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
            Array(maxCoordinates.first - minCoordinates.first + 3) { Array(maxCoordinates.second - minCoordinates.second + 3) { 0 } }

        paths
            .forEach { path ->
                path
                    .map { (x, y) -> x - minCoordinates.first + 1 to y - minCoordinates.second + 1 }
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
            var x = START_POINT.first - minCoordinates.first + 1
            var y = START_POINT.second - minCoordinates.second + 1
            k++

            while (y < cave[0].size - 1) {
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

            break
        }

        println(k)
    }

    private fun range(a: Int, b: Int) = (minOf(a, b)..maxOf(a, b))

    companion object {
        private val START_POINT = 500 to 0
    }
}