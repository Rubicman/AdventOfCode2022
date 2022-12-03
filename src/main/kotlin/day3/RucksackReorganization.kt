package day3

class RucksackReorganization {
    fun findErrors() {
        val doubleTypes = ArrayList<Char>()

        while (true) {
            val line = readlnOrNull() ?: break
            val localDoubleTypes = HashSet<Char>()
            val middle = line.length / 2

            for (i in 0 until middle) {
                localDoubleTypes.add(line[i])
            }

            for (i in middle until line.length) {
                if (localDoubleTypes.remove(line[i])) doubleTypes.add(line[i])
            }
        }

        var sum = 0
        for (type in doubleTypes) {
            sum += if (type.isLowerCase()) {
                type - 'a' + 1
            } else {
                type - 'A' + 27
            }
        }
        println(sum)
    }
}