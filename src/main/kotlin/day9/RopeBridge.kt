package day9

import kotlin.math.abs

class RopeBridge {

    fun simulateTail() {
        val positions = mutableSetOf<Pair<Int, Int>>()
        var head = START_POSITION
        var tail = START_POSITION

        while (true) {
            val (direction, steps) = readlnOrNull()?.split(" ") ?: break

            repeat(steps.toInt()) {
                head = when (direction) {
                    "U" -> head.first - 1 to head.second
                    "R" -> head.first to head.second + 1
                    "D" -> head.first + 1 to head.second
                    "L" -> head.first to head.second - 1
                    else -> throw IllegalStateException()
                }

                if (!head.isNext(tail)) {
                    tail = when (direction) {
                        "U" -> head.first + 1 to head.second
                        "R" -> head.first to head.second - 1
                        "D" -> head.first - 1 to head.second
                        "L" -> head.first to head.second + 1
                        else -> throw IllegalStateException()
                    }
                }

                positions.add(tail)
            }
        }

        println(positions.size)
    }

    private fun Pair<Int, Int>.isNext(other: Pair<Int, Int>) =
        (abs(first - other.first) <= 1 && abs(second - other.second) <= 1)

    companion object {
        private val START_POSITION = 0 to 0
    }

}