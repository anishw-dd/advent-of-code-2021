import kotlin.math.*

fun main() {
    fun calculate(numbers: List<Int>, costFunction: (start: Int, end: Int) -> Int ): Int {
        val (numMin, numMax) = Pair(numbers.minOrNull()!!, numbers.maxOrNull()!!)
        return (numMin..numMax).minOf { targetPos ->
            numbers.sumOf { costFunction(targetPos, it) }
        }
    }

    val testInput = readInputAsLines("Day07_Test")[0].split(',').map { it.toInt() }
    check(calculate(testInput) { start, end -> abs(start - end) } == 37)
    check(calculate(testInput) { start, end ->
        val n = abs(start - end)
        n * (n + 1) / 2
    } == 168)

    val input = readInputAsLines("Day07")[0].split(',').map { it.toInt() }
    println(calculate(input) { start, end -> abs(start - end)})
    println(calculate(input) { start, end ->
        val n = abs(start - end)
        n * (n + 1) / 2
    })
}