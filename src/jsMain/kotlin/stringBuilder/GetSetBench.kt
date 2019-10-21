package stringBuilder

import kotlinx.benchmark.*
import stringBuilder.stringBacked.StringStringBuilder
import kotlin.random.Random

@State(Scope.Benchmark)
class GetSetBench {

    @Param("0", "1", "2")
    var implementation: Int = 0

    @Param("10", "100", "1000", "10000", "100000")
    var size: Int = 0

    private var sb: IStringBuilder = StringStringBuilder()

    private var getSetIndexes = intArrayOf()

    private var charArrayForGetChars = CharArray(10)

    private val getRanges = ArrayList<IntRange>()

    @Setup
    fun setup() {
        sb = createEmptyStringBuilder(implementation)
        repeat(size) { sb.append('o') }

        getSetIndexes = IntArray(10) { Random.nextInt(size) }

        getRanges.clear()
        repeat(10) {
            val start = Random.nextInt(size)
            val end = (start + 10).coerceAtMost(size)
            getRanges.add(start until end)
        }

//        println("getRanges:")
//        println("builder length: $sbGetSetLength")
//        println("ranges: $this")
    }

    @Benchmark
    fun getChar(): Char {
        var result = '0'
        for (index in getSetIndexes) { result = sb[index] }
        return result
    }

    @Benchmark
    fun getCharAll(): Char {
        var result = '0'
        for (index in 0 until size) { result = sb[index] }
        return result
    }

    @Benchmark
    fun setChar(): IStringBuilder {
        for (index in getSetIndexes) { sb.set(index, 'a') }
        return sb
    }

    @Benchmark
    fun getChars(): CharArray {
        for (range in getRanges) {
            sb.getChars(range.start, range.endInclusive + 1, charArrayForGetChars, 0)
        }
        return charArrayForGetChars
    }

    @Benchmark
    fun reverse(): IStringBuilder {
        repeat(10) { sb.reverse() }
        return sb
    }

    @Benchmark
    fun substring(): String {
        var result = ""
        for (range in getRanges) {
            result = sb.substring(range.start, range.endInclusive + 1)
        }
        return result
    }
}