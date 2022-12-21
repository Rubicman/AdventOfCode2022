package day21

import lines

class MonkeyMath {
    fun monkeyNumber() {
        val nameToMonkey = lines()
            .map { it.split(" ", limit = 2) }
            .map { it[0].dropLast(1) to it[1].split(" ").toMonkey() }
            .toMap()

        nameToMonkey
            .values
            .filterIsInstance<CalculatedMonkey>()
            .forEach {
                it.leftMonkey = nameToMonkey.getValue(it.leftName)
                it.rightMonkey = nameToMonkey.getValue(it.rightName)
            }

        println(nameToMonkey.getValue(DESIRED_MONKEY).number)
    }

    private fun List<String>.toMonkey(): Monkey {
        return if (size == 1) {
            SpecifiedMonkey(first().toLong())
        } else {
            CalculatedMonkey(when(get(1)) {
                "+" -> Long::plus
                "-" -> Long::minus
                "*" -> Long::times
                "/" -> Long::div
                else -> throw IllegalArgumentException("Not allowed operation")
            }, get(0), get(2))
        }
    }

    companion object {
        private const val DESIRED_MONKEY = "root"
    }
}

interface Monkey {
    val number: Long
}

class SpecifiedMonkey(
    override val number: Long
): Monkey

class CalculatedMonkey(
    private val operation: (Long, Long) -> Long,
    val leftName: String,
    val rightName: String,
): Monkey {
    lateinit var leftMonkey: Monkey
    lateinit var rightMonkey: Monkey

    private var cachedNumber: Long? = null

    override val number: Long
        get() = cachedNumber ?: operation(leftMonkey.number, rightMonkey.number).also { cachedNumber = it }
}