package day2

class RockPaperScissors {
    fun totalPoints() {
        var points = 0
        while (true) {
            val (opponent, my) = readLine()?.split(" ") ?: break
            points += Shape.parse(my).run { this.points + result(Shape.parse(opponent)) }
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
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalStateException()
            }
        }
    }
}