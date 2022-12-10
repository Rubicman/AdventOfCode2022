package day10

import lines
import kotlin.math.abs

class CathodeRayTube {
    fun simulateCPU() {
        val queue = ArrayDeque<Command>()

        lines().map { it.split(" ") }.forEach {
            queue.addLast(it.takeIf { it[0] == "addx" }?.let { StartAdd(it[1].toLong()) } ?: Noop)
        }

        var i = 1
        var score = 1L
        var sum = 0L
        while (queue.isNotEmpty()) {
            if (i % 40 == 20)
                sum += i * score
            when (val command = queue.removeFirst()) {
                is StartAdd -> queue.addFirst(EndAdd(command.value))
                is EndAdd -> score += command.value
                is Noop -> {}
            }
            i++
        }

        println(sum)
    }

    fun draw() {
        val queue = ArrayDeque<Command>()

        lines().map { it.split(" ") }.forEach {
            queue.addLast(it.takeIf { it[0] == "addx" }?.let { StartAdd(it[1].toLong()) } ?: Noop)
        }

        var score = 1L
        val screen = Array(6) { Array(40) { " " } }
        for (i in 0..239) {
            screen[i / 40][i % 40] = if (abs(score - i % 40) < 2) "#" else "."
            when (val command = queue.removeFirstOrNull()) {
                is StartAdd -> queue.addFirst(EndAdd(command.value))
                is EndAdd -> score += command.value
                else -> {}
            }
        }
        screen.forEach { println(it.joinToString("")) }
    }
}

sealed interface Command

@JvmInline
value class StartAdd(val value: Long) : Command

@JvmInline
value class EndAdd(val value: Long) : Command

object Noop : Command