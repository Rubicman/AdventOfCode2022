package day23

import lines

class UnstableDiffusion {
    fun elvesMoves() {
        val lines = lines().toList()
        val directions = Direction.values().toMutableList()
        var positions = sequence {
            for (y in lines.indices) {
                for (x in lines[0].indices) {
                    if (lines[y][x] == '#') yield(x to y)
                }
            }
        }.toSet()

        var k = 0
        while (true) {
            k++
            val newPositions = positions
                .map { position ->
                    (position to position).takeIf {
                        directions.flatMap { it.watched }.distinct().all { position.first + it.first to position.second + it.second !in positions }
                    }
                        ?: directions.firstOrNull { direction ->
                            direction
                                .watched
                                .all { position.first + it.first to position.second + it.second !in positions }
                        }?.let { position to (position.first + it.first to position.second + it.second) }
                        ?: (position to position)
                }
                .groupBy { it.second }
                .flatMap { (_, list) -> if (list.size == 1) list.map { it.second } else list.map { it.first } }
                .toSet()

            if (newPositions == positions) break
            positions = newPositions

            directions.add(directions.removeAt(0))
        }

        println(k)
    }

    companion object {
        private const val ROUNDS = 10
    }
}

enum class Direction(val first: Int, val second: Int) {
    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0);

    val watched = if (first == 0) (-1..1).map { it to second } else (-1..1).map { first to it }
}