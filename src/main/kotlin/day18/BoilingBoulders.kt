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
}