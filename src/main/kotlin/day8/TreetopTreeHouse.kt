package day8

import day8.Visibility.*

class TreetopTreeHouse {
    fun visibleTrees() {
        val forrest = mutableListOf<List<Int>>()

        while (true) {
            val line = readlnOrNull() ?: break
            forrest.add(line.map { it.code - '0'.code })
        }

        val n = forrest.size
        val m = forrest[0].size

        val visibilities = Array(n) { Array(m) { Visibility.values().toMutableSet() } }

        for (i in 0 until n) {
            for (j in 0 until m) {
                for (k in 0 until i) {
                    if (forrest[k][j] >= forrest[i][j]) visibilities[i][j].remove(LEFT)
                }
                for (k in i + 1 until n) {
                    if (forrest[k][j] >= forrest[i][j]) visibilities[i][j].remove(RIGHT)
                }
                for (k in 0 until j) {
                    if (forrest[i][k] >= forrest[i][j]) visibilities[i][j].remove(UP)
                }
                for (k in j + 1 until m) {
                    if (forrest[i][k] >= forrest[i][j]) visibilities[i][j].remove(DOWN)
                }
            }
        }

        println(visibilities.flatten().count { it.isNotEmpty() })
    }
}

enum class Visibility {
    UP,
    RIGHT,
    DOWN,
    LEFT
}