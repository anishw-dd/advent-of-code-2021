import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun isHorizontallyAlignedWith(other: Point) = x == other.x
    fun isVerticallyAlignedWith(other: Point) = y == other.y
    fun isDiagonallyAlignedWith(other: Point) = abs(x - other.x) == abs(y - other.y)
}

data class VentLine(val startPoint: Point, val endPoint: Point) {
    fun explode(func: (input: Point) -> Unit) {
        when {
            startPoint.isHorizontallyAlignedWith(endPoint) -> {
                val (start, end) = if (startPoint.y < endPoint.y) {
                    Pair(startPoint.y, endPoint.y)
                } else {
                    Pair(endPoint.y, startPoint.y)
                }
                (start..end).forEach { func(Point(startPoint.x, it)) }
            }
            startPoint.isVerticallyAlignedWith(endPoint) -> {
                val (start, end) = if (startPoint.x < endPoint.x) {
                    Pair(startPoint.x, endPoint.x)
                } else {
                    Pair(endPoint.x, startPoint.x)
                }
                (start..end).forEach { func(Point(it, endPoint.y)) }
            }
            startPoint.isDiagonallyAlignedWith(endPoint) -> {
                var yModifier = 1
                val start = if (startPoint.x < endPoint.x) {
                    if (startPoint.y > endPoint.y) yModifier = -1
                    startPoint
                } else {
                    if (endPoint.y > startPoint.y) yModifier = -1
                    endPoint
                }
                (0..abs(startPoint.x - endPoint.x)).forEach { func(Point(start.x + it, start.y + it * yModifier)) }
            }
        }
    }

}

fun main() {
    fun String.toVentLine(): VentLine {
        val (start, end) = this.split("->")
        val startPoint = start.trim().split(',').map { it.toInt() }
        val endPoint = end.trim().split(',').map { it.toInt() }
        return VentLine(Point(startPoint[0], startPoint[1]), Point(endPoint[0], endPoint[1]))
    }

    fun List<String>.toVentLines(): List<VentLine> = this.map { it.toVentLine() }

    fun bothParts(input: List<String>): Int {
        val lines = input.toVentLines()
        val coordsMap = mutableMapOf<Point, Int>()
        var result = 0
        val seen = mutableSetOf<Point>()

        lines.forEach { line ->
            line.explode { point ->
                coordsMap[point] = coordsMap[point]?.inc() ?: 1
                if (coordsMap[point]!! > 1 && !seen.contains(point)) {
                    result++
                    seen.add(point)
                }
            }
        }
        return result
    }

    val testInput = readInputAsLines("Day05_Test")
    check(bothParts(testInput) == 12)

    val input = readInputAsLines("Day05")
    println(bothParts(input))
}
