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
                head = headMotion(direction, head)
                tail = tailMotion(head, tail)

                positions.add(tail)
            }
        }

        println(positions.size)
    }

    fun simulateRope() {
        val positions = mutableSetOf<Pair<Int, Int>>()
        val rope = Array(ROPE_SIZE) { START_POSITION }
        var oldRope = rope

        while (true) {
            val (direction, steps) = readlnOrNull()?.split(" ") ?: break

            oldRope = rope.clone()
            repeat(steps.toInt()) {
                rope[0] = headMotion(direction, rope[0])

                for (i in 1 until ROPE_SIZE) {
                    rope[i] = tailMotion(rope[i - 1], rope[i])
                }
                positions.add(rope.last())
            }
        }

        println(positions.size)
    }

    private fun tailMotion(
        head: Pair<Int, Int>,
        tail: Pair<Int, Int>
    ): Pair<Int, Int> {
        if (head.isNext(tail)) return tail
        val newFirst = when(head.first - tail.first) {
            -2 -> head.first + 1
            2 -> head.first - 1
            else -> head.first
        }
        val newSecond = when(head.second - tail.second) {
            -2 -> head.second + 1
            2 -> head.second - 1
            else -> head.second
        }
        return newFirst to newSecond
    }

    private fun headMotion(
        direction: String,
        head: Pair<Int, Int>,
    ) = when (direction) {
        "U" -> head.first + 1 to head.second
        "R" -> head.first to head.second + 1
        "D" -> head.first - 1 to head.second
        "L" -> head.first to head.second - 1
        else -> throw IllegalStateException()
    }

    private fun Pair<Int, Int>.isNext(other: Pair<Int, Int>) =
        (abs(first - other.first) <= 1 && abs(second - other.second) <= 1)

    companion object {
        private val START_POSITION = 0 to 0
        private const val ROPE_SIZE = 10
    }

    private fun printWay(positions: Set<Pair<Int, Int>>) {
        val yMax = positions.maxOf { it.first }
        val yMin = positions.minOf { it.first }
        val xMax = positions.maxOf { it.second }
        val xMin = positions.minOf { it.second }

        val field = Array(yMax - yMin + 1) { Array(xMax - xMin + 1) { "." } }

        positions.forEach { (y, x) -> field[yMax - y][x - xMin] = "#" }

        field.forEach { println(it.joinToString("")) }
    }
}