package day15

import lines
import kotlin.math.abs

class BeaconExclusionZone {
    fun lineNonePositions() {
        val sensors = sensors()

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

    fun findDistressBeacon() {
        val sensors = sensors()
        val lines = sensors.flatMap { it.lines }

        val points = mutableListOf<Point?>()
        sequence {
            for (i1 in 0 until lines.size - 3) {
                for (i2 in i1 until lines.size - 2) {
                    points.add(lines[i1].intersection(lines[i2]))
                    for (i3 in i2 until lines.size - 1) {
                        points.add(lines[i1].intersection(lines[i3]))
                        points.add(lines[i2].intersection(lines[i3]))
                        for (i4 in i3 until lines.size) {
                            points.add(lines[i1].intersection(lines[i4]))
                            points.add(lines[i2].intersection(lines[i4]))
                            points.add(lines[i3].intersection(lines[i4]))

                            yield(distressBeacon(points.filterNotNull()))

                            points.removeLast()
                            points.removeLast()
                            points.removeLast()
                        }
                        points.removeLast()
                        points.removeLast()
                    }
                    points.removeLast()
                }
            }
        }
            .filterNotNull()
            .toSet()
            .filterNot { point -> sensors.any { point in it } }
            .filter { it.x in BORDERS && it.y in BORDERS}
            .forEach { println(it.x * X_MULTIPLIER + it.y) }
    }

    private fun distressBeacon(points: List<Point>): Point? {
        if (points.size != 4) return null

        val beacon = Point(points.map { it.x }.average().toLong(), points.map { it.y }.average().toLong())

        if (!points.any { beacon.x == it.x && beacon.y == it.y - 1 }) return null
        if (!points.any { beacon.x == it.x && beacon.y == it.y + 1 }) return null
        if (!points.any { beacon.x == it.x - 1 && beacon.y == it.y }) return null
        if (!points.any { beacon.x == it.x + 1 && beacon.y == it.y }) return null

        return beacon
    }

    private fun sensors(): List<Sensor> {
        val regex = Regex("-?\\d+")

        return lines()
            .map { line ->
                regex
                    .findAll(line)
                    .map { it.value.toLong() }
                    .toList()
            }
            .map { Sensor(it[0] to it[1], it[2] to it[3]) }
            .toList()
    }

    companion object {
        private const val SEARCHING_LINE = 2000000L
        private const val X_MULTIPLIER = 4_000_000L
        private val BORDERS = 0..4_000_000L
    }
}

open class Point(
    val x: Long,
    val y: Long,
) {
    constructor(point: Pair<Long, Long>) : this(point.first, point.second)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
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

    val lines: List<Line>
        get() = listOf(x - distance to y, x to y - distance, x + distance to y, x to y + distance, x - distance to y)
            .map { Point(it) }
            .zipWithNext { a, b -> Line(a, b) }

    operator fun contains(point: Point): Boolean = abs(x - point.x) + abs(y - point.y) <= distance
}

class Line(
    private val a: Point,
    private val b: Point,
) {
    fun intersection(other: Line): Point? {
        val dk = (a.x - b.x) * (other.a.y - other.b.y) - (a.y - b.y) * (other.a.x - other.b.x)
        if (dk == 0L) return null
        val k1 = a.x * b.y - a.y * b.x
        val k2 = other.a.x * other.b.y - other.a.y * other.b.x
        val x =
            (k1 * (other.a.x - other.b.x) - (a.x - b.x) * k2) / dk
        val y = (k1 * (other.a.y - other.b.y) - (a.y - b.y) * k2) / dk

        return Point(x, y)
    }
}