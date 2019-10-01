package stringBuilder.stringBacked

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
    fun reverse_SplitReverseJoin(): String {
        return string.asDynamic().split("").reverse().join("")
    }

    @Benchmark
    fun reverse_OneByOne(): String {
        var result = ""
        var index = string.length - 1
        while (index >= 0) {
            result += string[index--]
        }
        return result
    }

    @Benchmark
    fun appendCharArray_String(): String {
        return string + String(charArray)
    }

    @Benchmark
    fun appendCharArray_OneByOne(): String {
        var result = string
        for (char in charArray) result += char
        return result
    }

    @Benchmark
    fun delete_Substring(): String {
        val start = Random.nextInt(size)
        val end = Random.nextInt(start, size + 1)
        return string.substring(0, start) + string.substring(end)
    }

    @Benchmark
    fun delete_Slice(): String {
        val start = Random.nextInt(size)
        val end = Random.nextInt(start, size + 1)
        return string.asDynamic().slice(0, start) + string.asDynamic().slice(end)
    }

    @Benchmark
    fun getChars_OneByOne(): CharArray {
        val start = Random.nextInt(size)
        val end = Random.nextInt(start, size + 1)
        var dstIndex = 0
        for (index in start until end) {
            charArray[dstIndex++] = string[index]
        }
        return charArray
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    @Benchmark
    fun getChars_toCharArray(): CharArray {
        val start = Random.nextInt(size)
        val end = Random.nextInt(start, size + 1)
        string.toCharArray().copyInto(charArray, 0, start, end)
        return charArray
    }

    @Benchmark
    fun insertString_Plus(): String {
        val offset = Random.nextInt(size)
        return string.substring(0, offset) + string + string.substring(offset)
    }

    @Benchmark
    fun insertString_ArrayJoin(): String {
        val offset = Random.nextInt(size)
        return js("[this.string.substring(0, offset), this.string, this.string.substring(offset)].join('')")
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