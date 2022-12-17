package day17

import day17.Rock.HORIZONTAL_LINE
import lines

class PyroclasticFlow {
    fun dropRocks() {
        val moves = lines().first().map {
            when (it) {
                '<' -> -1
                '>' -> +1
                else -> throw IllegalArgumentException("Must be only '<' and '>'")
            }
        }
        var i = 0
        var rock = HORIZONTAL_LINE
        val occupiedPlaces = mutableSetOf<Pair<Long, Int>>()
        val maxLevels = Array(WIDTH) { -1L }
        var rockNumber = 0L

        while (true) {
            if (++rockNumber > TOTAL_ROCKS) break

            val edge = START_POSITION.run { first + maxLevels.max() to second }
            var positions = rock.points.map { it.first + edge.first to it.second + edge.second }

            while (true) {
                var newPositions = positions.map { it.first to it.second + moves[i] }
                if (newPositions.all { it.second in maxLevels.indices && it !in occupiedPlaces }) {
                    positions = newPositions
                }
                i = (i + 1) % moves.size

                newPositions = positions.map { it.first - 1 to it.second }
                if (newPositions.all { it.first >= 0 && it !in occupiedPlaces }) {
                    positions = newPositions
                } else {
                    break
                }
            }

            occupiedPlaces.addAll(positions)
            positions.forEach { maxLevels[it.second] = maxOf(maxLevels[it.second], it.first) }
            rock = rock.next
        }

        println(maxLevels.max() + 1)
    }

    private fun printFiled(occupiedPlaces: Set<Pair<Int, Int>>, positions: List<Pair<Int, Int>>) {
        val n = occupiedPlaces.plus(positions).maxOfOrNull { it.first } ?: -1

        for (i in n downTo 0) {
            print("|")
            for (j in 0 until WIDTH) {
                val string = when {
                    i to j in occupiedPlaces -> "#"
                    i to j in positions -> "@"
                    else -> "."
                }
                print(string)
            }
            println("|")
        }

        println("+${"-".repeat(WIDTH)}+")
    }

    companion object {
        private const val TOTAL_ROCKS = 2022L
        private const val WIDTH = 7
        private val START_POSITION = 4L to 2
    }
}

enum class Rock(val points: List<Pair<Long, Int>>) {
    HORIZONTAL_LINE(
        """
        ####
    """
    ),
    PLUS(
        """
        .#.
        ###
        .#.
    """
    ),
    L_SHAPE(
        """
        ..#
        ..#
        ###
    """
    ),
    VERTICAL_LINE(
        """
        #
        #
        #
        #
    """
    ),
    SQUARE(
        """
        ##
        ##
    """
    );

    constructor(shape: String) : this(stringToPoints(shape))

    val next: Rock
        get() = when (this) {
            HORIZONTAL_LINE -> PLUS
            PLUS -> L_SHAPE
            L_SHAPE -> VERTICAL_LINE
            VERTICAL_LINE -> SQUARE
            SQUARE -> HORIZONTAL_LINE
        }
}

fun stringToPoints(shape: String): List<Pair<Long, Int>> {
    val lines = shape.trimIndent().split("\n")
    val n = lines.size

    return buildList {
        for (i in lines.indices) {
            for (j in lines[0].indices) {
                if (lines[i][j] == '#') {
                    add(n - i - 1L to j)
                }
            }
        }
    }
}