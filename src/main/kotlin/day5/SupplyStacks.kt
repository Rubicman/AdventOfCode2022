package day5

class SupplyStacks {
    fun rearrange() {
        val lines = ArrayList<String>()
        val n: Int

        while (true) {
            val line = readln().filterIndexed { index, _ -> index % 4 == 1 }

            if (line.all(Char::isDigit)) {
                n = line.length
                break
            } else
                lines.add(line)
        }

        val stacks = Array(n) { ArrayList<Char>() }
        for (i in lines.indices.reversed()) {
            for (j in 0 until n) {
                if (lines[i][j] != ' ')
                    stacks[j].add(lines[i][j])
            }
        }

        readln()
        while (true) {
            val command = readlnOrNull()?.split(" ") ?: break
            val count = command[1].toInt()
            val from = command[3].toInt() - 1
            val to = command[5].toInt() - 1
            stacks[to].addAll(stacks[from].takeLast(count))
            repeat(count) {
                stacks[from].removeLast()
            }
        }

        println(stacks.joinToString("") { it.lastOrNull()?.toString() ?: " " })
    }
}