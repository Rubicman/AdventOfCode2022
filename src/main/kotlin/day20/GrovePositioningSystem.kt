package day20

import lines

class GrovePositioningSystem {
    fun decode() {
        val list = lines().toList()
            .mapIndexed { index, s -> index to s.toInt() }
            .toMutableList()

        for (i in list.indices) {
            var newIndex = list[i].first + list[i].second
            for (j in list.indices) {
                if (list[j].first > list[i].first) {
                    list[j] = list[j].first - 1 to list[j].second
                }
            }
            while (newIndex <= 0) {
                newIndex += list.size - 1
            }
            while (newIndex >= list.size) {
                newIndex -= list.size - 1
            }
            for (j in list.indices) {
                if (list[j].first >= newIndex) {
                    list[j] = list[j].first + 1 to list[j].second
                }
            }
            list[i] = newIndex to list[i].second
        }

        list.sortBy { it.first }

        val zeroIndex = list.indexOfFirst { it.second == 0 }

        println(CONTROL_POINTS.map { (it + zeroIndex) % list.size }.map { list[it].second }.sum())
    }

    companion object {
        private val CONTROL_POINTS = setOf(1000, 2000, 3000)
    }
}
