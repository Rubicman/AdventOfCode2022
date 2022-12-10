fun lines() = sequence {
    while (true) {
        val line = readlnOrNull() ?: break
        yield(line)
    }
}