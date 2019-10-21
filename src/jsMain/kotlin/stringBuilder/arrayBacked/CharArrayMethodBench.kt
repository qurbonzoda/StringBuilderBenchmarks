package stringBuilder.arrayBacked

import kotlinx.benchmark.*
import kotlin.random.Random

@State(Scope.Benchmark)
class CharArrayMethodBench {

    @Param("1", "10", "100", "1000", "10000", "100000")
    var size = 0

    private var string = ""
    private var charArrayToAppend = charArrayOf()
    private var charArray = charArrayOf()

    @Setup
    fun setup() {
        string = ""
        repeat(size) { string += Random.nextInt().toChar() }
        charArrayToAppend = CharArray(size) { Random.nextInt().toChar() }

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

    @Benchmark
    fun appendCharArray_CharIn(): CharArray {
        var length = 0
        for (char in charArrayToAppend) charArray[length++] = char
        return charArray
    }

    @Benchmark
    fun appendCharArray_CharByIndex(): CharArray {
        var length = 0
        for (index in 0 until charArrayToAppend.size) charArray[length++] = charArrayToAppend[index]
        return charArray
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    @Benchmark
    fun appendCharArray_CopyInto(): CharArray {
        charArrayToAppend.copyInto(charArray, 0, 0, charArrayToAppend.size)
        return charArray
    }
}