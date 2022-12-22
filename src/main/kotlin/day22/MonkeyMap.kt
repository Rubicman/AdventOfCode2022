package day22

import day22.BoardElement.*
import day22.Direction.RIGHT
import lines

class MonkeyMap {
    fun findTheWay() {
        val lines = lines().toList()
        val n = lines.size - 2;
        val m = lines.drop(2).maxOf { it.length }
        val board = Array(n) { Array(m) { TELEPORT } }

        for (i in 0 until n) {
            for (j in lines[i].indices) {
                when (lines[i][j]) {
                    '.' -> board[i][j] = EMPTY
                    '#' -> board[i][j] = WALL
                }
            }
        }

        var x = 0
        var y = 0
        var direction = RIGHT
        while (board[y][x] != EMPTY) {
            x++
            if (x == m) {
                x = 0
                y++
            }
        }

        parse(lines.last()).forEach { command ->
            when (command) {
                is Steps -> repeat(command.value) {
                    var newX = x + direction.dx
                    var newY = y + direction.dy

                    if (newY !in board.indices || newX !in board[0].indices || board[newY][newX] == TELEPORT) {
                        do {
                            newX -= direction.dx
                            newY -= direction.dy
                        } while (newY in board.indices && newX in board[0].indices && board[newY][newX] != TELEPORT)
                        newX += direction.dx
                        newY += direction.dy
                    }

                    if (board[newY][newX] != WALL) {
                        x = newX
                        y = newY
                    }
                }

                is Turn -> direction = direction.next(command)
            }
        }

        println((y + 1) * 1000 + (x + 1) * 4 + direction.value)
    }

    private fun parse(input: String) = sequence {
        var value = 0
        for (char in input) {
            if (char.isDigit()) {
                value = value * 10 + char.code - '0'.code
            } else {
                if (value > 0) {
                    yield(Steps(value))
                    value = 0
                }
                when (char) {
                    'L' -> yield(TurnLeft)
                    'R' -> yield(TurnRight)
                }
            }
        }
        if (value > 0) yield(Steps(value))
    }
}

enum class BoardElement {
    TELEPORT, WALL, EMPTY
}

sealed interface Command
class Steps(val value: Int) : Command
sealed interface Turn : Command
object TurnLeft : Turn
object TurnRight : Turn

enum class Direction(val dx: Int, val dy: Int, val value: Int) {
    UP(0, -1, 3),
    RIGHT(1, 0, 0),
    DOWN(0, 1, 1),
    LEFT(-1, 0, 2);

    fun next(turn: Turn): Direction = when (this) {
        UP -> when (turn) {
            is TurnLeft -> LEFT
            is TurnRight -> RIGHT
        }

        RIGHT -> when (turn) {
            is TurnLeft -> UP
            is TurnRight -> DOWN
        }

        DOWN -> when (turn) {
            is TurnLeft -> RIGHT
            is TurnRight -> LEFT
        }

        LEFT -> when (turn) {
            is TurnLeft -> DOWN
            is TurnRight -> UP
        }
    }
}