package day8

import day8.Visibility.*

class TreetopTreeHouse {
    fun visibleTrees() {
        val forrest = forrest()

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

    fun bestPosition() {
        val forrest = forrest()

        val n = forrest.size
        val m = forrest[0].size

        var bestResult = 1
        for (i in 1 until n - 1) {
            for (j in 1 until m - 1) {
                var score = 1
                for (k in i - 1 downTo 0) {
                    if (k == 0 || forrest[k][j] >= forrest[i][j]) {
                        score *= i - k
                        break
                    }
                }
                for (k in i + 1 until n) {
                    if (k == n - 1 || forrest[k][j] >= forrest[i][j]) {
                        score *= k - i
                        break
                    }
                }
                for (k in j - 1 downTo 0) {
                    if (k == 0 || forrest[i][k] >= forrest[i][j]) {
                        score *= j - k
                        break
                    }
                }
                for (k in j + 1 until m) {
                    if (k == m - 1 || forrest[i][k] >= forrest[i][j]) {
                        score *= k - j
                        break
                    }
                }
                bestResult = maxOf(bestResult, score)
            }
        }
        println(bestResult)
    }

    private fun forrest(): MutableList<List<Int>> {
        val forrest = mutableListOf<List<Int>>()

        while (true) {
            val line = readlnOrNull() ?: break
            forrest.add(line.map { it.code - '0'.code })
        }
        return forrest
    }
}

enum class Visibility {
    UP,
    RIGHT,
    DOWN,
    LEFT
}