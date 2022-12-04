package day4

class CampCleanup {
    fun fullyContainsPairs() {
        var result = 0

        while (true) {
            val line = readlnOrNull() ?: break
            val numbers = line
                .split(",")
                .map { it.split("-").map(String::toInt) }
                .map { it[0]..it[1] }

            if (
                (numbers[0].first >= numbers[1].first && numbers[0].last <= numbers[1].last) ||
                (numbers[0].first <= numbers[1].first && numbers[0].last >= numbers[1].last)
            ) result++
        }

        println(result)
    }
}