package day11

import lines

class MonkeyInTheMiddle {
    fun keepAway() {
        val monkeys = lines()
            .map { it.split(" ").filter(String::isNotBlank) }
            .windowed(BLOCK_SIZE, BLOCK_SIZE)
            .map { lines ->
                val items = lines[1].subList(2, lines[1].size).map { it.replace(",", "").toInt() }
                val leftElement = if (lines[2][3] == "old") Value() else Const(Item(lines[2][3].toInt()))
                val rightElement = if (lines[2][5] == "old") Value() else Const(Item(lines[2][5].toInt()))
                val operation = when (lines[2][4]) {
                    "+" -> Add(leftElement, rightElement)
                    "*" -> Multiply(leftElement, rightElement)
                    else -> throw IllegalStateException()
                }
                val test = lines[3][3].toInt()
                Item.dividers.add(test)
                val trueMonkey = lines[4][5].toInt()
                val falseMonkey = lines[5][5].toInt()

                Monkey(
                    ArrayDeque<Item>().apply { addAll(items.map { Item(it) }) },
                    { operation.calculate(it) },
                    { if (it.reminder(test) == 0) trueMonkey else falseMonkey }
                )
            }.toList()

        repeat(ROUNDS) {
            monkeys.forEach { it.throwItems(monkeys) }
        }

        println(monkeys.map(Monkey::inspectations).sorted().reversed().let { it[0] * it[1] })
    }

    companion object {
        private const val BLOCK_SIZE = 7
        private const val ROUNDS = 10_000
    }
}

class Monkey(
    private val items: ArrayDeque<Item>,
    private val map: (Item) -> Item,
    private val nextMonkey: (Item) -> Int,
) {
    var inspectations = 0L

    fun throwItems(monkeys: List<Monkey>) {
        while (items.isNotEmpty()) {
            inspectations++
            var item = items.removeFirst()
            item = map(item)
            val monkeyIndex = nextMonkey(item)
            monkeys[monkeyIndex].items.addLast(item)
        }
    }
}

interface Element {
    fun calculate(x: Item): Item
}

class Add(
    private val left: Element,
    private val right: Element,
) : Element {
    override fun calculate(x: Item): Item = left.calculate(x) + right.calculate(x)
}

class Multiply(
    private val left: Element,
    private val right: Element,
) : Element {
    override fun calculate(x: Item): Item = left.calculate(x) * right.calculate(x)
}

class Const(private val value: Item) : Element {
    override fun calculate(x: Item): Item = value
}

class Value : Element {
    override fun calculate(x: Item): Item = x
}

class Item private constructor(
    private val reminders: MutableMap<Int, Int> = mutableMapOf(),
) {
    
    constructor(value: Int): this(mutableMapOf<Int, Int>().withDefault { value % it })
    
    operator fun plus(other: Item): Item {
        val newReminders = mutableMapOf<Int, Int>()
        for (divider in dividers) {
            newReminders[divider] = (reminder(divider) + other.reminder(divider)) % divider
        }
        return Item(newReminders)
    }

    operator fun times(other: Item): Item {
        val newReminders = mutableMapOf<Int, Int>()
        for (divider in dividers) {
            newReminders[divider] = (reminder(divider) * other.reminder(divider)) % divider
        }
        return Item(newReminders)
    }

    fun reminder(divider: Int): Int = reminders.getValue(divider)
    
    companion object {
        val dividers = mutableListOf<Int>()
    }
}