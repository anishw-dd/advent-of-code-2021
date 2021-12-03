import kotlin.math.max


enum class Gas {
    O2, C02
}


fun main() {

    fun getIndexedBitCount(data: List<String>): Pair<Map<Int, Int>, Map<Int, Int>> {
        val zeroBitCount = mutableMapOf<Int, Int>()
        val oneBitCount = mutableMapOf<Int, Int>()

        data.forEach { bits ->
            bits.forEachIndexed { idx, bit ->
                when (bit) {
                    '0' -> zeroBitCount[idx] = zeroBitCount.getOrDefault(idx, 0) + 1
                    '1' -> oneBitCount[idx] = oneBitCount.getOrDefault(idx, 0) + 1
                }
            }
        }

        return Pair(zeroBitCount, oneBitCount)
    }

    fun part1(data: List<String>): Int {
        val (zeroBitCount, oneBitCount) = getIndexedBitCount(data)

        val gamma = mutableListOf<Char>()
        val epsilon = mutableListOf<Char>()

        for (bitIdx in 0 until data[0].length) {
            if (zeroBitCount[bitIdx]!! > oneBitCount[bitIdx]!!) {
                gamma.add('0')
                epsilon.add('1')
            }
            else {
                gamma.add('1')
                epsilon.add('0')
            }
        }

        return String(gamma.toCharArray()).toInt(2) * String(epsilon.toCharArray()).toInt(2)
    }

    fun getBitCountAtIndex(data: List<String>, bitIdx: Int): Pair<Int, Int> {
        var (zeroCount, oneCount) = Pair(0, 0)
        data.forEach { bits ->
            if (bits[bitIdx] == '1') oneCount++
            else zeroCount++
        }

        return Pair(zeroCount, oneCount)
    }

    fun getGasRating(data: List<String>, gas: Gas): Int {
        var rating = ""
        var filteredData = data

        for (bitIdx in 0 until data[0].length) {
            val (zeroBitCount, oneBitCount) = getBitCountAtIndex(filteredData, bitIdx)

            var chosenBit = when(gas) {
                Gas.O2 -> '1'
                Gas.C02 -> '0'
            }

            if (zeroBitCount > oneBitCount)
                chosenBit = when(gas) {
                    Gas.O2 -> '0'
                    Gas.C02 -> '1'
                }

            filteredData = filteredData.filter { it[bitIdx] == chosenBit }
            if (filteredData.size == 1) {
                rating = filteredData.first()
                break
            }
        }

        return rating.toInt(2)
    }

    fun part2(data: List<String>): Int {
        return getGasRating(data, Gas.O2) * getGasRating(data, Gas.C02)
    }

    val testInput = readInput("Day03_Test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
