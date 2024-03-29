package stringBuilder

import kotlinx.benchmark.*
import kotlin.random.Random

@State(Scope.Benchmark)
open class AppendBench {

    @Param("0", "1", "2")
    var implementation = 0

    @Param("10", "100", "1000", "10000", "100000")
    var size: Int = 0

    @Benchmark
    fun appendChar(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        repeat(size) { sb.append('a') }
        return sb
    }

    @Benchmark
    fun appendInt(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        var int = 0
        while (sb.length < size) sb.append(int++)
        return sb
    }

    @Benchmark
    fun ensureCapacityAppendChar(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        sb.ensureCapacity(size)
        repeat(size) { sb.append('a') }
        return sb
    }
}

@State(Scope.Benchmark)
class AppendStringBench {

    @Param("0", "1", "2")
    var implementation = 0

    @Param("10", "100", "1000", "10000", "100000")
    var size: Int = 0

    @Param("10")    // configurable
    var appendLength: Int = 0

    private var charArrayToAppend = charArrayOf()
    private var stringToAppend = ""

    @Setup
    fun setup() {
        charArrayToAppend = CharArray(appendLength) { Random.nextInt().toChar() }
        stringToAppend = String(charArrayToAppend)
    }

    @Benchmark
    fun appendCharArray(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        while (sb.length < size) sb.append(charArrayToAppend)
        return sb
    }

    @Benchmark
    fun appendString(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        while (sb.length < size) sb.append(stringToAppend)
        return sb
    }
}