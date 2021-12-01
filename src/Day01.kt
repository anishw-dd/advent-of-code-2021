fun main() {
    fun part1(input: List<Int>): Int {
        return input.mapIndexed { idx, value ->
            if (idx == 0 || input[idx - 1] >= value) 0 else 1
        }.sum()
    }

    fun part2(input: List<Int>, windowSize: Int): Int {
        var (left, right) = Pair(0, 0)
        var (prevSum, currSum) = Pair(Integer.MAX_VALUE, 0)
        var cnt = 0

        while (right < input.size) {
            if (right - left < windowSize) currSum += input[right++]
            else {
                if (currSum > prevSum) cnt++
                prevSum = currSum
                currSum -= input[left++]
                currSum += input[right++]
            }
        }
        if (currSum > prevSum) cnt++
        return cnt
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").map{ it.toInt() }
    check(part1(testInput) == 7)
    check(part2(testInput, 3) == 5)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input, 3))
}
