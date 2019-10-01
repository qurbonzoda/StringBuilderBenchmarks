package stringBuilder.arrayBacked

import kotlinx.benchmark.*
import kotlin.random.Random

@State(Scope.Benchmark)
@Measurement(iterations = 3, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
class MethodImplementationBench {

    @Param("1", "10", "1000", "100000")
    var size = 0

    var string = ""
    var charArray = charArrayOf()

    @Setup
    fun setup() {
        string = ""
        repeat(size) { string += Random.nextInt().toChar() }

        charArray = CharArray(size) { Random.nextInt().toChar() }
    }

    @Benchmark
    fun appendString_CharIn(): CharArray {
        var length = 0
        for (char in string) charArray[length++] = char
        return charArray
    }

    @Benchmark
    fun appendString_CharByIndex(): CharArray {
        var length = 0
        for (index in 0 until string.length) charArray[length++] = string[index]
        return charArray
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    @Benchmark
    fun appendString_toCharArray(): CharArray {
        string.toCharArray().copyInto(charArray, 0, 0, string.length)
        return charArray
    }
}