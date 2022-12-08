package day7

class NoSpaceLeftOnDevice {
    fun lightDirectories() {
        println(dfs(buildFileTree()))
    }

    fun deleteDirectory() {
        val root = buildFileTree()
        val searchingSize = root.size - 40_000_000
        if (searchingSize <= 0) {
            println(0)
            return
        }

        println(dfs(root, searchingSize))
    }

    private fun buildFileTree(): Directory {
        val root = Directory("/", null)
        var currentDirectory = root

        while (true) {
            val input = readlnOrNull()?.split(" ") ?: break
            if (input[0] == "$") {
                if (input[1] == "cd") {
                    when (input[2]) {
                        "/" -> {}
                        ".." -> currentDirectory = currentDirectory.parent!!
                        else -> currentDirectory = currentDirectory.children[input[2]] as Directory
                    }
                }
            } else if (input[0] == "dir") {
                currentDirectory.children.putIfAbsent(input[1], Directory(input[1], currentDirectory))
            } else {
                currentDirectory.children.putIfAbsent(input[1], File(input[1], input[0].toLong()))
            }
        }
        return root
    }

    private fun dfs(directory: Directory): Long {
        val sum = if (directory.size <= 100_000) directory.size else 0

        return sum + directory.subDirs.sumOf { dfs(it) }
    }

    private fun dfs(directory: Directory, size: Long): Long {
        var bestSize = directory.subDirs.minOfOrNull { dfs(it, size) } ?: Long.MAX_VALUE

        if (directory.size >= size) bestSize = minOf(bestSize, directory.size)

        return bestSize
    }
}

interface FSObject {
    val size: Long
}

data class File(
    val name: String,
    override val size: Long,
) : FSObject

data class Directory(
    val name: String,
    val parent: Directory?,
    val children: MutableMap<String, FSObject> = mutableMapOf()
) : FSObject {
    private var _size: Long? = null

    override val size: Long
        get() {
            if (_size == null) {
                _size = children.values.sumOf { it.size }
            }
            return _size!!
        }

    val subDirs: List<Directory>
        get() = children.values.filterIsInstance<Directory>()
}