package day19

import day19.Material.*
import lines

class NotEnoughMinerals {
    fun collectGeodes() {
        lines()
            .map { it.split(" ").mapNotNull(String::toLongOrNull) }
            .map {
                listOf(
                    ORE to mapOf(ORE to it[0]),
                    CLAY to mapOf(ORE to it[1]),
                    OBSIDIAN to mapOf(ORE to it[2], CLAY to it[3]),
                    GEODE to mapOf(ORE to it[4], OBSIDIAN to it[5])
                )
            }
            .mapIndexed { index, blueprint ->
                Dfs(blueprint)(
                minutes = TOTAL_MINUTES,
                performance = values().associateWith { if (it == ORE) 1L else 0L },
                collected = values().associateWith { 0L }
                ) * (index + 1) }
            .sum()
            .let(::println)
    }

    companion object {
        private const val TOTAL_MINUTES = 24L
    }
}

typealias Production = Map<Material, Long>

enum class Material {
    ORE, CLAY, OBSIDIAN, GEODE
}

class Dfs(
    private val blueprint: List<Pair<Material, Production>>,
) {
    private val maxPerformance: Production = blueprint
        .flatMap { it.second.entries }
        .groupBy { it.key }
        .mapValues { (_, value) -> value.maxOf { it.value } }
        .plus(GEODE to Long.MAX_VALUE)

    operator fun invoke(minutes: Long, performance: Production, collected: Production): Long {
        if (minutes <= 0) return 0
        var result = performance.getValue(GEODE) * minutes + collected.getValue(GEODE)

        mainLoop@ for ((robot, cost) in blueprint) {
            if (maxPerformance.getValue(robot) == performance.getValue(robot)) continue

            val needs = cost
                .map { it.key to maxOf(0L, it.value - collected.getValue(it.key)) }
                .toMap()
                .withDefault { 0 }

            for ((material, amount) in needs) if (amount > 0 && performance.getValue(material) == 0L) continue@mainLoop

            val neededMinutes = (needs
                .filterValues { it > 0L }
                .maxOfOrNull { (material, need) ->
                    performance
                        .getValue(material)
                        .let { (need + it - 1) / it }
                } ?: 0) + 1

            val newPerformance = performance.mapValues { if (it.key == robot) it.value + 1 else it.value }
            val newCollected = collected.mapValues { it.value + neededMinutes * performance.getValue(it.key) - cost.getOrDefault(it.key, 0) }

            result = maxOf(result, invoke(minutes - neededMinutes, newPerformance, newCollected))
        }

        return result
    }
}
