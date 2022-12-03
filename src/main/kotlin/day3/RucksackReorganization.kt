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

        println(doubleTypes.map(this::priority).sum())
    }

    fun badges() {
        var sum = 0

        while (true) {
            val first = readlnOrNull() ?: break
            val second = readln().toSet()
            val third = readln().toSet()

            sum += priority(first.first { second.contains(it) && third.contains(it) })
        }

        println(sum)
    }

    private fun priority(type: Char) = if (type.isLowerCase()) {
        type - 'a' + 1
    } else {
        type - 'A' + 27
    }
}