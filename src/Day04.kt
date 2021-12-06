class Board(board: List<List<Int>>) {
    private val bitMap: List<MutableList<Boolean>>
    private val rowCounters: MutableList<Int>
    private val colCounters: MutableList<Int>
    private val locationMap =  mutableMapOf<Int, Pair<Int, Int>>()
    private val rows = board.size
    private val cols = board[0].size
    private var total = board.sumOf { it.sum() }

    init {
        bitMap = List(rows) { MutableList(cols) { false } }
        rowCounters = MutableList(rows) { 0 }
        colCounters = MutableList(cols) { 0 }
        board.forEachIndexed { rowIdx, numbers ->
            numbers.forEachIndexed { colIdx, num ->
                locationMap[num] = Pair(rowIdx, colIdx)
            }
        }
    }

    fun update(called: Int): Pair<Boolean, Int?> {
        if (locationMap.contains(called)) {
            val (rowIdx, colIdx) = locationMap[called]!!
            bitMap[rowIdx][colIdx] = true
            total -= called

            if (++rowCounters[rowIdx] == rows || ++colCounters[colIdx] == cols) {
                return Pair(true, total * called)
            }
        }
        return Pair(false, null)
    }
}

class NoWinnerException(s: String) : Exception()


fun main() {
    fun parseInput(input: List<String>): Pair<List<Int>, MutableSet<Board>> {
        var bingoNumbers: List<Int>? = null
        val boards = mutableSetOf<Board>()
        var currentBoardInfo = mutableListOf<List<Int>>()

        input.forEachIndexed { idx, line ->
            when {
                idx == 0 -> bingoNumbers = line.split(',').map { it.toInt() }
                line == "" -> {
                    if (idx > 1) {
                        boards.add(Board(currentBoardInfo))
                        currentBoardInfo = mutableListOf()
                    }

                }
                else -> currentBoardInfo.add(line.trim().split("\\s+".toRegex()).map { it.toInt() })
            }
        }

        boards.add(Board(currentBoardInfo))
        return Pair(bingoNumbers!!, boards)
    }


    fun part1(input: List<String>): Int? {
        val (bingoNumbers, boards) = parseInput(input)
        bingoNumbers.forEach { choice ->
            boards.forEach { board ->
                val (won, ans) = board.update(choice)
                if (won)
                    return ans
            }
        }

        throw NoWinnerException("Could not find a winner!!!")
    }

    fun part2(input: List<String>): Int {
        val (bingoNumbers, boards) = parseInput(input)
        val boardsSet = boards.toMutableSet()
        var lastWinningResult = 0

        bingoNumbers.forEach { choice ->
            val boardsToRemove = mutableSetOf<Board>()
            boardsSet.forEach { board ->
                val (won, answer) = board.update(choice)
                if (won) {
                    lastWinningResult = answer!!
                    boardsToRemove.add(board)
                }
            }
            boardsToRemove.forEach { boardsSet.remove(it) }
        }
        return lastWinningResult
    }

    val testInput = readInputAsLines("Day04_Test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInputAsLines("Day04")
    println(part1(input))
    println(part2(input))

}
