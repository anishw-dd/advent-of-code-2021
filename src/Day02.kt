
data class Instruction(val command: String, val distance: Int)

enum class Command(val value: String) {
    FORWARD("forward"),
    UP("up"),
    DOWN("down")
}

fun main() {
    fun buildInstructions(input: List<String>): List<Instruction> {
        return input.map { line ->
            val (command, dist) = line.split(' ')
            Instruction(command, dist.toInt())
        }
    }

    fun part1(instructions: List<Instruction>): Int {
        var horizontalDistTravelled = 0
        var verticalDistTravelled = 0

        instructions.forEach { instruction ->
            when (instruction.command) {
                Command.UP.value -> verticalDistTravelled -= instruction.distance
                Command.DOWN.value -> verticalDistTravelled += instruction.distance
                Command.FORWARD.value -> horizontalDistTravelled += instruction.distance
            }
        }

        return horizontalDistTravelled * verticalDistTravelled
    }


    fun part2(instructions: List<Instruction>): Int {
        var horizontalDistTravelled = 0
        var verticalDistTravelled = 0
        var aim = 0

        instructions.forEach { instruction ->
            when (instruction.command) {
                Command.UP.value -> aim -= instruction.distance
                Command.DOWN.value -> aim += instruction.distance
                Command.FORWARD.value -> {
                    horizontalDistTravelled += instruction.distance
                    verticalDistTravelled += aim * instruction.distance
                }

            }
        }

        return horizontalDistTravelled * verticalDistTravelled
    }

    val testInput = readInputAsLines("Day02_Test")
    check(part1(buildInstructions(testInput)) == 150)
    check(part2(buildInstructions(testInput)) == 900)

    val input = readInputAsLines("Day02")
    println(part1(buildInstructions(input)))
    println(part2(buildInstructions(input)))

}