import java.lang.Long.max

const val BOARD_SIZE = 10
const val ROLLS = 3
const val P1_WIN_CLAUSE = 1000
const val P2_WIN_CLAUSE = 21

data class Die(val faces: Int = 100, var currIdx: Int = 0) {
    private val store = (1..faces).toList()
    var rolls = 0

    fun getNextRoll(): Int {
        val nextNumber = store[currIdx]
        currIdx = ++currIdx % faces
        rolls++
        return nextNumber
    }
}

data class Player(val id: Int, val location: Int, var points: Int = 0) {
    private var internalLoc = location - 1

    fun play(rolls: Int) {
        internalLoc = (internalLoc + rolls) % BOARD_SIZE
        points += internalLoc + 1
    }

    fun hasWon(): Boolean = points >= P1_WIN_CLAUSE
}


data class MemoKey(val p1Place: Int, val p2Place: Int, val p1Score: Int, val p2Score: Int, val p1Turn: Boolean)

fun main() {
    fun String.toPlayer(): Player {
        val (_, playerId, _, _, startLoc) = this.split(' ')
        return Player(playerId.toInt(), startLoc.toInt())
    }

    fun List<String>.toPlayers(): List<Player> = this.map { it.toPlayer() }

    fun part1(players: List<Player>, die: Die = Die()): Int {
        var (currPlayer, otherPlayer) = Pair(players[0], players[1])

        while (true) {
            currPlayer.play((1..ROLLS).sumOf { die.getNextRoll() })
            if (currPlayer.hasWon()) break
            currPlayer = otherPlayer.also { otherPlayer = currPlayer }
        }
        return otherPlayer.points * die.rolls
    }

    fun part2(players: List<Player>): Long {
        fun helper(
            p1Place: Int, p2Place: Int, p1Score: Int, p2Score: Int, p1Turn: Boolean, memo: MutableMap<MemoKey, Pair<Long, Long>>): Pair<Long, Long> {

            val memoKey = MemoKey(p1Place, p2Place, p1Score, p2Score, p1Turn)
            if (memo.containsKey(memoKey)) return memo[memoKey]!!
            if (p1Score >= P2_WIN_CLAUSE) return Pair(1L, 0L)
            if (p2Score >= P2_WIN_CLAUSE) return Pair(0L, 1L)

            var (totalP1Wins, totalP2Wins) = Pair(0L, 0L)

            for (die1 in 1..3) {
                for (die2 in 1..3) {
                    for (die3 in 1..3) {
                        val roll = die1 + die2 + die3

                        val (p1Wins, p2Wins) = if (p1Turn) {
                            val newP1Place = (p1Place + roll) % BOARD_SIZE
                            val newP1Score = p1Score + newP1Place + 1
                            helper(newP1Place, p2Place, newP1Score, p2Score, !p1Turn, memo)
                        } else {
                            val newP2Place = (p2Place + roll) % BOARD_SIZE
                            val newP2Score = p2Score + newP2Place + 1
                            helper(p1Place, newP2Place, p1Score, newP2Score, !p1Turn, memo)
                        }

                        totalP1Wins += p1Wins
                        totalP2Wins += p2Wins
                    }
                }
            }
            memo[memoKey] = Pair(totalP1Wins, totalP2Wins)
            return Pair(totalP1Wins, totalP2Wins)
        }

        val memo = mutableMapOf<MemoKey, Pair<Long, Long>>()
        val (player1, player2) = players
        val (p1Wins, p2Wins) = helper(player1.location - 1, player2.location - 1, player1.points, player2.points, true, memo)
        return max(p1Wins, p2Wins)
    }

    val testInput = readInputAsLines("Day21_Test")
    check(part1(testInput.toPlayers()) == 739785)
    check(part2(testInput.toPlayers()) == 444356092776315)

    val input = readInputAsLines("Day21")
    println(part1(input.toPlayers()))
    println(part2(input.toPlayers()))
}
