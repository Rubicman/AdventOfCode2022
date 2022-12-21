package day21

import lines

class MonkeyMath {
    fun monkeyNumber() {
        val nameToMonkey = monkeys()

        println(nameToMonkey.getValue(DESIRED_MONKEY_NAME).number)
    }

    fun humanNumber() {
        val nameToMonkey = monkeys()

        nameToMonkey.getValue(DESIRED_MONKEY_NAME).findHuman(0)
        println((nameToMonkey.getValue(HUMAN_NAME) as Human).expected)
    }

    private fun monkeys(): Map<String, Monkey> {
        val nameToMonkey = lines()
            .map { it.split(" ", limit = 2) }
            .map {
                val name = it[0].dropLast(1)
                name to it[1].split(" ").toMonkey(name)
            }
            .toMap()

        nameToMonkey
            .values
            .filterIsInstance<CalculatedMonkey>()
            .forEach {
                it.leftMonkey = nameToMonkey.getValue(it.leftName)
                it.rightMonkey = nameToMonkey.getValue(it.rightName)
            }
        return nameToMonkey
    }

    private fun List<String>.toMonkey(name: String): Monkey {
        return when {
            name == DESIRED_MONKEY_NAME -> RootMonkey(Operation.operationOf(get(1)), get(0), get(2))
            name == HUMAN_NAME -> Human(first().toLong())
            size == 1 -> SpecifiedMonkey(first().toLong())
            else -> CalculatedMonkey(Operation.operationOf(get(1)), get(0), get(2))
        }
    }

    companion object {
        private const val DESIRED_MONKEY_NAME = "root"
        private const val HUMAN_NAME = "humn"
    }
}

interface Monkey {
    val number: Long
    fun findHuman(result: Long)
}

open class SpecifiedMonkey(
    override val number: Long,
) : Monkey {
    override fun findHuman(result: Long) {}
}

open class CalculatedMonkey(
    private val operation: Operation,
    val leftName: String,
    val rightName: String,
) : Monkey {
    lateinit var leftMonkey: Monkey
    lateinit var rightMonkey: Monkey

    private var cachedNumber: Long? = null

    override val number: Long
        get() = cachedNumber ?: operation.result(leftMonkey.number, rightMonkey.number).also { cachedNumber = it }

    override fun findHuman(result: Long) {
        leftMonkey.findHuman(operation.left(result, rightMonkey.number))
        rightMonkey.findHuman(operation.right(result, leftMonkey.number))
    }
}

class Human(number: Long) : SpecifiedMonkey(number) {
    var expected: Long? = null

    override fun findHuman(result: Long) {
        expected = result
    }
}

class RootMonkey(operation: Operation, leftName: String, rightName: String) :
    CalculatedMonkey(operation, leftName, rightName) {

    override fun findHuman(result: Long) {
        leftMonkey.findHuman(rightMonkey.number)
        rightMonkey.findHuman(leftMonkey.number)
    }
}

interface Operation {
    fun result(left: Long, right: Long): Long
    fun left(result: Long, right: Long): Long
    fun right(result: Long, left: Long): Long

    companion object {
        fun operationOf(string: String) = when (string) {
            "+" -> Plus
            "-" -> Minus
            "*" -> Times
            "/" -> Div
            else -> throw IllegalArgumentException("Not allowed operation")
        }
    }
}

object Plus : Operation {
    override fun result(left: Long, right: Long): Long = left + right

    override fun left(result: Long, right: Long): Long = result - right

    override fun right(result: Long, left: Long): Long = result - left
}

object Minus : Operation {
    override fun result(left: Long, right: Long): Long = left - right

    override fun left(result: Long, right: Long): Long = result + right

    override fun right(result: Long, left: Long): Long = left - result
}

object Times : Operation {
    override fun result(left: Long, right: Long): Long = left * right

    override fun left(result: Long, right: Long): Long = result / right

    override fun right(result: Long, left: Long): Long = result / left
}

object Div : Operation {
    override fun result(left: Long, right: Long): Long = left / right

    override fun left(result: Long, right: Long): Long = result * right

    override fun right(result: Long, left: Long): Long = left / result
}