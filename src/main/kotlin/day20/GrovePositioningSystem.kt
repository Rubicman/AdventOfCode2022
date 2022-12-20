package day20

import lines

class GrovePositioningSystem {
    fun decode() {
        val list = lines().toList()
            .mapIndexed { index, s -> index.toLong() to s.toLong() * SECRET_KEY}
            .toMutableList()

        repeat(DECODE_TIMES) {
            decode(list)
        }

        list.sortBy { it.first }
        val zeroIndex = list.indexOfFirst { it.second == 0L }
        println(CONTROL_POINTS.map { (it + zeroIndex) % list.size }.sumOf { list[it].second })
    }

    private fun decode(list: MutableList<Pair<Long, Long>>) {
        for (i in list.indices) {
            var newIndex = list[i].first + list[i].second
            for (j in list.indices) {
                if (list[j].first > list[i].first) {
                    list[j] = list[j].first - 1 to list[j].second
                }
            }
            newIndex %= list.size - 1
            while (newIndex <= 0) {
                newIndex += list.size - 1
            }
            for (j in list.indices) {
                if (list[j].first >= newIndex) {
                    list[j] = list[j].first + 1 to list[j].second
                }
            }
            list[i] = newIndex to list[i].second
        }
    }

    companion object {
        private val CONTROL_POINTS = setOf(1000, 2000, 3000)
        private const val DECODE_TIMES = 10
        private const val SECRET_KEY = 811589153L
    }
}
