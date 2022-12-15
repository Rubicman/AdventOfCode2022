package day15

import lines
import kotlin.math.abs

class BeaconExclusionZone {
    fun lineNonePositions() {
        val regex = Regex("-?\\d+")

        val sensors = lines()
            .map { line ->
                regex
                    .findAll(line)
                    .map { it.value.toLong() }
                    .toList()
            }
            .map { Sensor(it[0] to it[1], it[2] to it[3]) }
            .toList()

        val ranges = sensors
            .map { sensor ->
                val radius = sensor.distance - abs(SEARCHING_LINE - sensor.y)
                sensor.x - radius..sensor.x + radius
            }
            .filterNot(LongRange::isEmpty)
            .sortedBy { it.first }

        if (ranges.isEmpty()) {
            println(0)
            return
        }

        val beaconsOnLine = sensors
            .map { it.nearestBeacon }
            .filter { it.y == SEARCHING_LINE }
            .map { it.x }

        sequence {
            var previous = ranges.first()

            for (range in ranges) {
                previous = if (previous.last < range.first) {
                    yield(previous)
                    range
                } else {
                    previous.first..maxOf(previous.last, range.last)
                }
            }

            yield(previous)
        }
            .sumOf { range -> range.count() - beaconsOnLine.count { it in range } }
            .let(::println)
    }

    companion object {
        private const val SEARCHING_LINE = 2000000L
    }
}

open class Point(
    val x: Long,
    val y: Long,
) {
    constructor(point: Pair<Long, Long>) : this(point.first, point.second)
}

class Sensor(
    coordinates: Point,
    val nearestBeacon: Point,
) : Point(coordinates.x, coordinates.y) {
    val distance = abs(coordinates.x - nearestBeacon.x) + abs(coordinates.y - nearestBeacon.y)

    constructor(coordinates: Pair<Long, Long>, nearestBeacon: Pair<Long, Long>) : this(
        Point(coordinates),
        Point(nearestBeacon)
    )
}