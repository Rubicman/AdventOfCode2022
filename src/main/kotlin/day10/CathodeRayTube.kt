package day10

import lines

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
            when(val command = queue.removeFirst()) {
                is StartAdd -> queue.addFirst(EndAdd(command.value))
                is EndAdd -> score += command.value
                is Noop -> {}
            }
            i++
        }

        println(sum)
    }
}

sealed interface Command

@JvmInline
value class StartAdd(val value: Long): Command

@JvmInline
value class EndAdd(val value: Long): Command

object Noop: Command