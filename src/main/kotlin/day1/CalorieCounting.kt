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
}