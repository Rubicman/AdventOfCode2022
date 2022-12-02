package day1

class CalorieCounting {
    fun count() {
        var maxCalories = 0L
        var lastCalories = 0L

        while (true) {
            val line = readLine() ?: break
            if (line.isEmpty()) {
                maxCalories = maxOf(maxCalories, lastCalories)
                lastCalories = 0
            } else {
                lastCalories += line.toLong()
            }
        }

        println(maxCalories)
    }

    fun topThreeCount() {
        val maxCalories = mutableListOf(0L, 0L, 0L, 0L)
        var lastCalories = 0L

        while (true) {
            val line = readLine() ?: break
            if (line.isEmpty()) {
                maxCalories[0] = lastCalories
                maxCalories.sort()
                lastCalories = 0
            } else {
                lastCalories += line.toLong()
            }
        }

        println(maxCalories.drop(1).sum())
    }
}