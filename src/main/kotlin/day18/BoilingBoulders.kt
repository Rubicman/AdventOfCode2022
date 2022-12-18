package day18

import lines

class BoilingBoulders {
    fun surfaceArea() {
        var result = 0
        val xSurfaces = mutableSetOf<Triple<Int, Int, Int>>()
        val ySurfaces = mutableSetOf<Triple<Int, Int, Int>>()
        val zSurfaces = mutableSetOf<Triple<Int, Int, Int>>()

        lines().map { it.split(",").map(String::toInt) }.forEach { (x, y, z) ->
            if (xSurfaces.add(Triple(x, y, z))) result++ else result--
            if (xSurfaces.add(Triple(x + 1, y, z))) result++ else result--

            if (ySurfaces.add(Triple(x, y, z))) result++ else result--
            if (ySurfaces.add(Triple(x, y + 1, z))) result++ else result--

            if (zSurfaces.add(Triple(x, y, z))) result++ else result--
            if (zSurfaces.add(Triple(x, y, z + 1))) result++ else result--
        }

        println(result)
    }

    fun simulateSteam() {
        val lavaBlocks = mutableSetOf<Triple<Int, Int, Int>>()

        lines().map { it.split(",").map(String::toInt) }.forEach { (x, y, z) ->
            lavaBlocks.add(Triple(x, y, z))
        }

        val bounds = Triple(
            lavaBlocks.minOf { it.first } - 1..lavaBlocks.maxOf { it.first } + 1,
            lavaBlocks.minOf { it.second } - 1..lavaBlocks.maxOf { it.second } + 1,
            lavaBlocks.minOf { it.third } - 1..lavaBlocks.maxOf { it.third } + 1
        )

        var result = 0
        val steamBlocks = mutableSetOf<Triple<Int, Int, Int>>()
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        queue.addLast(Triple(bounds.first.first, bounds.second.first, bounds.third.first))

        while (queue.isNotEmpty()) {
            val block = queue.removeFirst()
            if (block in steamBlocks) continue
            steamBlocks.add(block)

            val (x, y, z) = block
            if (x !in bounds.first || y !in bounds.second || z !in bounds.third) continue

            var newBlock = Triple(x + 1, y, z)
            if (newBlock in lavaBlocks) result++ else queue.add(newBlock)
            newBlock = Triple(x - 1, y, z)
            if (newBlock in lavaBlocks) result++ else queue.add(newBlock)

            newBlock = Triple(x, y + 1, z)
            if (newBlock in lavaBlocks) result++ else queue.add(newBlock)
            newBlock = Triple(x, y - 1, z)
            if (newBlock in lavaBlocks) result++ else queue.add(newBlock)

            newBlock = Triple(x, y, z + 1)
            if (newBlock in lavaBlocks) result++ else queue.add(newBlock)
            newBlock = Triple(x, y, z - 1)
            if (newBlock in lavaBlocks) result++ else queue.add(newBlock)
        }

        println(result)
    }
}