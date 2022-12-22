package day22

import day22.BoardElement.*
import day22.Side.*
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
        var direction = Direction.RIGHT
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

    fun findTheWayOnCube() {
        val lines = lines().toList()

        var x = 0
        var y = 0
        var direction: Direction
        var side: Side

        while (lines[y][x] != '.') {
            x++
            if (x == lines.size) {
                x = 0
                y++
            }
        }

        processCube(StartState(lines, Point(y, x, Direction.RIGHT, FRONT))).point.also {
            x = it.x
            y = it.y
            direction = it.direction
            side = it.side
        }

        val points = mutableListOf(Point(y, x, direction, side))
        parse(lines.last()).forEach { command ->
            when (command) {
                is Steps -> repeat(command.value) {
                    var newX = x + direction.dx
                    var newY = y + direction.dy
                    var newSide = side
                    var newDirection = direction

                    when {
                        newX == -1 -> when (side) {
                            FRONT -> {
                                newX = CUBE_SIZE - 1
                                newSide = LEFT
                            }

                            BACK -> {
                                newX = 0
                                newY = CUBE_SIZE - newY - 1
                                newDirection = Direction.RIGHT
                                newSide = LEFT
                            }

                            LEFT -> {
                                newX = 0
                                newY = CUBE_SIZE - newY - 1
                                newDirection = Direction.RIGHT
                                newSide = BACK
                            }

                            RIGHT -> {
                                newX = CUBE_SIZE - 1
                                newSide = FRONT
                            }

                            UP -> {
                                newX = newY
                                newY = 0
                                newDirection = Direction.DOWN
                                newSide = LEFT
                            }

                            DOWN -> {
                                newX = CUBE_SIZE - newY - 1
                                newY = CUBE_SIZE - 1
                                newDirection = Direction.UP
                                newSide = LEFT
                            }
                        }

                        newY == -1 -> when (side) {
                            FRONT -> {
                                newY = CUBE_SIZE - 1
                                newSide = UP
                            }

                            BACK -> {
                                newY = CUBE_SIZE - 1
                                newSide = DOWN
                            }

                            LEFT -> {
                                newY = newX
                                newX = 0
                                newDirection = Direction.RIGHT
                                newSide = UP
                            }

                            RIGHT -> {
                                newY = CUBE_SIZE - newX - 1
                                newX = CUBE_SIZE - 1
                                newDirection = Direction.LEFT
                                newSide = UP
                            }

                            UP -> {
                                newY = CUBE_SIZE - 1
                                newSide = BACK
                            }

                            DOWN -> {
                                newY = CUBE_SIZE - 1
                                newSide = FRONT
                            }
                        }

                        newX == CUBE_SIZE -> when (side) {
                            FRONT -> {
                                newX = 0
                                newSide = RIGHT
                            }

                            BACK -> {
                                newX = CUBE_SIZE - 1
                                newY = CUBE_SIZE - newY - 1
                                newDirection = Direction.LEFT
                                newSide = RIGHT
                            }

                            LEFT -> {
                                newX = 0
                                newSide = FRONT
                            }

                            RIGHT -> {
                                newX = CUBE_SIZE - 1
                                newY = CUBE_SIZE - newY - 1
                                newDirection = Direction.LEFT
                                newSide = BACK
                            }

                            UP -> {
                                newX = CUBE_SIZE - newY - 1
                                newY = 0
                                newDirection = Direction.DOWN
                                newSide = RIGHT
                            }

                            DOWN -> {
                                newX = newY
                                newY = CUBE_SIZE - 1
                                newDirection = Direction.UP
                                newSide = RIGHT
                            }
                        }

                        newY == CUBE_SIZE -> when (side) {
                            FRONT -> {
                                newY = 0
                                newSide = DOWN
                            }

                            BACK -> {
                                newY = 0
                                newSide = UP
                            }

                            LEFT -> {
                                newY = CUBE_SIZE - newX - 1
                                newX = 0
                                newDirection = Direction.RIGHT
                                newSide = DOWN
                            }

                            RIGHT -> {
                                newY = newX
                                newX = CUBE_SIZE - 1
                                newDirection = Direction.LEFT
                                newSide = DOWN
                            }

                            UP -> {
                                newY = 0
                                newSide = FRONT
                            }

                            DOWN -> {
                                newY = 0
                                newSide = BACK
                            }
                        }
                    }
                    if (newSide[newY][newX] == EMPTY) {
                        x = newX
                        y = newY
                        side = newSide
                        direction = newDirection
                    }
                    points.add(Point(y, x, direction, side))
                }

                is Turn -> {
                    direction = direction.next(command)
                    points.add(Point(y, x, direction, side))
                }
            }
        }

        processCube(ResultState(lines, Point(y, x, direction, side))).point.also {
            println((it.y + 1) * 1000 + (it.x + 1) * 4 + it.direction.value)
        }
    }

    private fun <T : State<T>> processCube(state: T): T =
        state.processSide(FRONT, 1, 1, 0) {
            processSide(UP, 0, 1, 0) {
                processSide(LEFT, 0, 0, 3)
                processSide(RIGHT, 0, 2, 1) {
                    processSide(DOWN, 0, 3, 2)
                }
            }
            processSide(LEFT, 1, 0, 0) {
                processSide(UP, 0, 0, 1)
                processSide(DOWN, 2, 0, 3) {
                    processSide(RIGHT, 3, 0, 2)
                }
            }
            processSide(DOWN, 2, 1, 0) {
                processSide(LEFT, 2, 0, 1) {
                    processSide(BACK, 3, 0, 3)
                }
                processSide(RIGHT, 2, 2, 3) {
                    processSide(BACK, 3, 2, 1)
                    processSide(UP, 2, 3, 2)
                }
                processSide(BACK, 3, 1, 0) {
                    processSide(LEFT, 3, 0, 2)
                    processSide(RIGHT, 3, 2, 2)
                }
            }
            processSide(RIGHT, 1, 2, 0) {
                processSide(UP, 0, 2, 3) {
                    processSide(BACK, 0, 3, 1)
                }
                processSide(DOWN, 2, 2, 1) {
                    processSide(LEFT, 3, 2, 2)
                    processSide(BACK, 2, 3, 1)
                }
                processSide(BACK, 1, 3, 2) {
                    processSide(UP, 0, 3, 2)
                    processSide(DOWN, 2, 3, 2)
                }
            }
        }

    private fun printCube() {
        for (i in 0 until CUBE_SIZE) {
            println(" ".repeat(CUBE_SIZE) + UP[i].joinToString("") + " ".repeat(CUBE_SIZE))
        }
        for (i in 0 until CUBE_SIZE) {
            println(LEFT[i].joinToString("") + FRONT[i].joinToString("") + RIGHT[i].joinToString(""))
        }
        for (i in 0 until CUBE_SIZE) {
            println(" ".repeat(CUBE_SIZE) + DOWN[i].joinToString("") + " ".repeat(CUBE_SIZE))
        }
        for (i in 0 until CUBE_SIZE) {
            println(" ".repeat(CUBE_SIZE) + BACK[i].joinToString("") + " ".repeat(CUBE_SIZE))
        }
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
    TELEPORT, WALL, EMPTY;

    override fun toString(): String = when (this) {
        WALL -> "#"
        EMPTY -> "."
        TELEPORT -> " "
    }
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

const val CUBE_SIZE = 50

enum class Side {
    FRONT, BACK, LEFT, RIGHT, UP, DOWN;

    val board = Array(CUBE_SIZE) { Array(CUBE_SIZE) { EMPTY } }

    operator fun get(i: Int) = board[i]

    val indices = board.indices
}

data class Point(
    val y: Int,
    val x: Int,
    val direction: Direction,
    val side: Side,
)

abstract class State<T : State<T>> {
    abstract val lines: List<String>

    abstract fun processSide(side: Side, i: Int, j: Int, times: Int, block: T.() -> Unit = {}): T

    protected fun placeCheck(i: Int, j: Int) =
        (CUBE_SIZE * i in lines.indices && CUBE_SIZE * j in lines[CUBE_SIZE * i].indices
                && lines[CUBE_SIZE * i][CUBE_SIZE * j] != ' ')

    protected fun Side.fill(lines: List<String>, offsetI: Int, offsetJ: Int) {
        for (i in indices) {
            for (j in indices) {
                if (lines[i + offsetI][j + offsetJ] == '#') {
                    board[i][j] = WALL
                }
            }
        }
    }

    protected fun Side.rotate(times: Int) = repeat(times) {
        for (i in 0 until CUBE_SIZE - CUBE_SIZE / 2) {
            for (j in 0 until CUBE_SIZE / 2) {
                val c = board[i][j]
                board[i][j] = board[CUBE_SIZE - j - 1][i]
                board[CUBE_SIZE - j - 1][i] = board[CUBE_SIZE - i - 1][CUBE_SIZE - j - 1]
                board[CUBE_SIZE - i - 1][CUBE_SIZE - j - 1] = board[j][CUBE_SIZE - i - 1]
                board[j][CUBE_SIZE - i - 1] = c
            }
        }
    }

    protected fun Point.toSidePoint(side: Side, i: Int, j: Int, times: Int): Point {
        var y = this.y - CUBE_SIZE * i
        var x = this.x - CUBE_SIZE * j
        var direction = this.direction
        repeat(times) {
            y = x.also { x = CUBE_SIZE - y - 1 }
            direction = direction.next(TurnRight)
        }
        return Point(y, x, direction, side)
    }

    protected fun Point.fromSidePoint(side: Side, i: Int, j: Int, times: Int): Point {
        var x = this.x
        var y = this.y
        var direction = this.direction
        repeat(times) {
            x = y.also { y = CUBE_SIZE - x - 1 }
            direction = direction.next(TurnLeft)
        }
        y += CUBE_SIZE * i
        x += CUBE_SIZE * j
        return Point(y, x, direction, side)
    }
}

class StartState(
    override val lines: List<String>,
    var point: Point,
) : State<StartState>() {
    private var filled = false

    override fun processSide(side: Side, i: Int, j: Int, times: Int, block: StartState.() -> Unit): StartState {
        if (placeCheck(i, j)) {
            side.fill(lines, CUBE_SIZE * i, CUBE_SIZE * j)
            side.rotate(times)
            if (!filled && point.y in CUBE_SIZE * i..CUBE_SIZE * (i + 1) && point.x in CUBE_SIZE * j..CUBE_SIZE * (j + 1)) {
                point = point.toSidePoint(side, i, j, times)
                filled = true
            }
            block()
        }
        return this
    }
}

class ResultState(
    override val lines: List<String>,
    var point: Point,
) : State<ResultState>() {
    private var filled = false

    override fun processSide(side: Side, i: Int, j: Int, times: Int, block: ResultState.() -> Unit): ResultState {
        if (placeCheck(i, j)) {
            if (!filled && point.side == side) {
                point = point.fromSidePoint(side, i, j, times)
                filled = true
            }
            block()
        }
        return this
    }
}

class PrintState(override val lines: List<String>, private val points: List<Point>) : State<PrintState>() {
    private val map = lines.dropLast(2).map { it.toCharArray() }

    override fun processSide(side: Side, i: Int, j: Int, times: Int, block: PrintState.() -> Unit): PrintState {
        if (placeCheck(i, j)) {
            points
                .filter { it.side == side }
                .map { it.fromSidePoint(side, i, j, times) }
                .forEach { point ->
                    map[point.y][point.x] = when (point.direction) {
                        Direction.UP -> '^'
                        Direction.RIGHT -> '>'
                        Direction.DOWN -> 'v'
                        Direction.LEFT -> '<'
                    }
                }
            block()
        }
        return this
    }

    fun print() {
        map.forEach { println(it.joinToString("")) }
    }
}