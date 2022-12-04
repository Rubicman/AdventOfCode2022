package day4

class CampCleanup {
    fun fullyContainsPairs() {
        var result = 0

        while (true) {
            val numbers = ranges() ?: break

            if (
                (numbers[0].first >= numbers[1].first && numbers[0].last <= numbers[1].last) ||
                (numbers[0].first <= numbers[1].first && numbers[0].last >= numbers[1].last)
            ) result++
        }

        println(result)
    }

    fun overlapPairs() {
        var result = 0

        while (true) {
            val numbers = ranges() ?: break

            if (
               // !(numbers[0].last < numbers[1].first || numbers[0].first > numbers[1].last)
                numbers[0].last >= numbers[1].first && numbers[0].first <= numbers[1].last
            ) result++
        }

        println(result)
    }

    private fun ranges(): List<IntRange>? {
        val line = readlnOrNull() ?: return null
        return line
            .split(",")
            .map { it.split("-").map(String::toInt) }
            .map { it[0]..it[1] }
    }
}