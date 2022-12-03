package day2

class RockPaperScissors {
    fun totalPoints() {
        var points = 0
        while (true) {
            val (shape, result) = readLine()?.split(" ") ?: break
            val opponent = Shape.parse(shape)
            val my = Shape.parse((opponent.points - 1 + result(result)) % 3 + 1)
            points += my.points + my.result(opponent)
        }
        println(points)
    }

    private enum class Shape(val points: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        fun result(opponent: Shape): Int = when((points - opponent.points + 3) % 3) {
            0 -> DRAW_POINTS
            1 -> WON_POINTS
            2 -> LOST_POINTS
            else -> throw IllegalStateException()
        }

        companion object {
            private const val LOST_POINTS = 0
            private const val DRAW_POINTS = 3
            private const val WON_POINTS = 6

            fun parse(string: String): Shape = when(string) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                else -> throw IllegalStateException()
            }

            fun parse(points: Int): Shape = values().first { it.points == points}
        }
    }

    private fun result(string: String) = when(string) {
        "X" -> 2
        "Y" -> 0
        "Z" -> 1
        else -> throw IllegalStateException()
    }
}