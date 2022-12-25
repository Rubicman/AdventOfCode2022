package day25

import lines

class FullOfHotAir {
    fun fuelAmount() {
        println(toSNAFU(lines().sumOf(this::fromSNAFU)))
    }

    private fun fromSNAFU(string: String): Long =
        string.fold(0) { acc, c ->
            acc * 5 + when(c) {
                '0', '1', '2' -> c.code - '0'.code
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException("'$c' is not allowed in SNAFU")
            }
        }

    private fun toSNAFU(number: Long): String {
        var n = number
        val builder = StringBuilder()
        while (n > 0) {
            when(n % 5) {
                0L -> builder.append('0')
                1L -> builder.append('1')
                2L -> builder.append('2')
                3L -> builder.append('=')
                4L -> builder.append('-')
            }
            if (n % 5 > 2) n += 5
            n /= 5
        }
        builder.reverse()
        return builder.toString()
    }
}