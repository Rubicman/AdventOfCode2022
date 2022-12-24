package day24

import lines

class BlizzardBasin {
    fun avoidBlizzards() {
        val lines = lines().toList()

        val start = Point(lines.first().indexOf('.'), 0)
        val finish = Point(lines.last().indexOf('.'), lines.size - 1)
        val xBorders = 1 until lines[0].length - 1
        val yBorders = 1 until lines.size - 1
        val blizzards = sequence {
            for (y in lines.indices) {
                for (x in lines[0].indices) {
                    Direction.of(lines[y][x])?.let {
                        yield(Blizzard(x, y, it))
                    }
                }
            }
        }.toList()
        val map = Map(blizzards, xBorders, yBorders)

        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.addLast(start to 0)
        val used = mutableSetOf<Pair<Point, Int>>()

        while (queue.isNotEmpty()) {
            val (point, time) = queue.removeFirst()
            if (point to time in used) continue
            used.add(point to time)
            if (point == finish) {
                println(time)
                return
            }
            if (point in map[time]) continue
            if (point != start && !(point.x in xBorders && point.y in yBorders)) continue

            queue.addLast(point to time + 1)
            queue.addLast(point.copy(x = point.x + 1) to time + 1)
            queue.addLast(point.copy(x = point.x - 1) to time + 1)
            queue.addLast(point.copy(y = point.y + 1) to time + 1)
            queue.addLast(point.copy(y = point.y - 1) to time + 1)
        }
    }

    class Map(
        private val blizzards: List<Blizzard>,
        private val xBorders: IntRange,
        private val yBorders: IntRange,
    ) {
        private val states = mutableListOf(blizzards.map { Point(it.x, it.y) }.toSet())

        operator fun get(index: Int): Set<Point> {
            while (index >= states.size) round()
            return states[index]
        }

        private fun round() {
            blizzards.forEach {
                it.x += it.direction.dx
                it.y += it.direction.dy
                if (it.x < xBorders.first) it.x = xBorders.last
                if (it.y < yBorders.first) it.y = yBorders.last
                if (it.x > xBorders.last) it.x = xBorders.first
                if (it.y > yBorders.last) it.y = yBorders.first
            }
            states.add(blizzards.map { Point(it.x, it.y) }.toSet())
        }
    }
}

data class Point(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int) {
    UP(0 ,-1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    companion object {
        fun of(char: Char): Direction? = when(char) {
            '^' -> UP
            'v' -> DOWN
            '<' -> LEFT
            '>' -> RIGHT
            else -> null
        }
    }
}

class Blizzard(
    var x: Int,
    var y: Int,
    val direction: Direction
)