package day11

import lines
import kotlin.collections.ArrayDeque
import kotlin.concurrent.thread

class MonkeyInTheMiddle {
    fun keepAway() {
        val monkeys = lines()
            .map { it.split(" ").filter(String::isNotBlank) }
            .windowed(BLOCK_SIZE, BLOCK_SIZE)
            .map { lines ->
                val items = lines[1].subList(2, lines[1].size).map { it.replace(",", "").toInt() }
                val leftElement = if (lines[2][3] == "old") Value() else Const(lines[2][3].toInt())
                val rightElement = if (lines[2][5] == "old") Value() else Const(lines[2][5].toInt())
                val operation = when (lines[2][4]) {
                    "+" -> Add(leftElement, rightElement)
                    "*" -> Multiply(leftElement, rightElement)
                    else -> throw IllegalStateException()
                }
                val test = lines[3][3].toInt()
                val trueMonkey = lines[4][5].toInt()
                val falseMonkey = lines[5][5].toInt()

                Monkey(
                    ArrayDeque<Int>().apply { addAll(items) },
                    { operation.calculate(it) },
                    { if (it % test == 0) trueMonkey else falseMonkey}
                )
            }.toList()

        repeat(ROUNDS) {
            monkeys.forEach { it.throwItems(monkeys) }
        }

        println(monkeys.map(Monkey::inspectations).sorted().reversed().let { it[0] * it[1] })
    }

    companion object {
        private const val BLOCK_SIZE = 7
        private const val ROUNDS = 20
    }
}

class Monkey(
    private val items: ArrayDeque<Int>,
    private val map: (Int) -> Int,
    private val nextMonkey: (Int) -> Int,
) {
    var inspectations = 0

    fun throwItems(monkeys: List<Monkey>) {
        while (items.isNotEmpty()) {
            inspectations++
            var item = items.removeFirst()
            item = map(item) / 3
            val monkeyIndex = nextMonkey(item)
            monkeys[monkeyIndex].items.addLast(item)
        }
    }
}

interface Element {
    fun calculate(x: Int): Int
}

class Add(
    private val left: Element,
    private val right: Element,
) : Element {
    override fun calculate(x: Int): Int = left.calculate(x) + right.calculate(x)
}

class Multiply(
    private val left: Element,
    private val right: Element,
) : Element {
    override fun calculate(x: Int): Int = left.calculate(x) * right.calculate(x)
}

class Const(private val value: Int) : Element {
    override fun calculate(x: Int): Int = value
}

class Value : Element {
    override fun calculate(x: Int): Int = x
}