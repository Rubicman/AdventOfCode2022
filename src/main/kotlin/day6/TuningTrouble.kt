package day6

class TuningTrouble {
    fun startOfTheMessage() {
        val line = readln()
        mainLoop@ for (i in 13 until line.length) {
            for (j in i - 13 until i) {
                for (k in j + 1..i) {
                    if (line[j] == line[k]) continue@mainLoop
                }
            }
            println(i + 1)
            break
        }
    }
}