package day13

import lines

class DistressSignal {
    fun rightOrder() {
        lines()
            .map { parse(it.iterator()) }
            .mapNotNull { it.values.firstOrNull() }
            .windowed(2, 2)
            .mapIndexed { index, (left, right) -> if (compare(left, right) == true) index + 1 else 0 }
            .sum()
            .let { println(it) }
    }

    private fun compare(left: Element, right: Element): Boolean? = when(left) {
        is SingleElement -> when(right) {
            is SingleElement -> if (left.value == right.value) null else (left.value < right.value)
            is ListElement -> compare(ListElement(left), right)
        }
        is ListElement -> when(right) {
            is SingleElement -> compare(left, ListElement(right))
            is ListElement -> left.values
                .zip(right.values)
                .fold(null) { acc: Boolean?, (left, right) -> acc ?: compare(left, right) }
                ?: if (left.size == right.size) null else (left.size < right.size)
        }
    }

    private fun parse(iterator: CharIterator): ListElement {
        var number = 0
        var wasNumber = false
        val result = mutableListOf<Element>()

        while (iterator.hasNext()) {
            val char = iterator.nextChar()
            if (char.isDigit()) {
                number = number * 10 + char.code - '0'.code
                wasNumber = true
            } else {
                if (wasNumber) {
                    wasNumber = false
                    result.add(SingleElement(number))
                    number = 0
                }
                when (char) {
                    '[' -> result.add(parse(iterator))
                    ']' -> return ListElement(result)
                    else -> {}
                }
            }
        }

        return ListElement(result)
    }
}

sealed interface Element

@JvmInline
value class SingleElement(val value: Int) : Element

@JvmInline
value class ListElement(val values: List<Element>) : Element {
    constructor(element: Element) : this(listOf(element))

    val size: Int
        get() = values.size
}