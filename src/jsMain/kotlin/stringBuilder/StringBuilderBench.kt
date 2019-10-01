package stringBuilder

import kotlinx.benchmark.*
import stringBuilder.arrayBacked.CharArrayStringBuilder
import stringBuilder.stringBacked.StringStringBuilder
import kotlin.math.log2
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt


private const val STRING_STRING_BUILDER = 0
private const val CHAR_ARRAY_STRING_BUILDER = 1

private const val BENCHMARK_SIZE = 10000
private const val BENCHMARK_SIZE_MULTIPLIER = 10


@State(Scope.Benchmark)
@Measurement(iterations = 3, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
class StringBuilderBench {

    @Param("0", "1")
    var implementation = 0

    private fun createEmptyStringBuilder(implementation: Int): IStringBuilder {
        return when (implementation) {
            STRING_STRING_BUILDER -> StringStringBuilder()
            CHAR_ARRAY_STRING_BUILDER -> CharArrayStringBuilder()
            else -> throw IllegalArgumentException("Unknown implementation: $implementation")
        }
    }

    @Benchmark
    fun emptyStringBuilder(): IStringBuilder {
        return createEmptyStringBuilder(implementation)
    }

    @Benchmark
    fun appendChar(): IStringBuilder {
        val times = BENCHMARK_SIZE

        val sb = createEmptyStringBuilder(implementation)
        repeat(times) { sb.append('a') }
        return sb
    }

    @Benchmark
    fun appendInt(): IStringBuilder {
        val times = BENCHMARK_SIZE

        val sb = createEmptyStringBuilder(implementation)
        repeat(times) { sb.append(it) }
        return sb
    }

    private val charArrayToAppend = CharArray(BENCHMARK_SIZE_MULTIPLIER) { Random.nextInt().toChar() }

    @Benchmark
    fun appendCharArray(): IStringBuilder {
        val times = BENCHMARK_SIZE

        val sb = createEmptyStringBuilder(implementation)
        repeat(times) { sb.append(charArrayToAppend) }
        return sb
    }

    private val stringToAppend = String(charArrayToAppend)

    @Benchmark
    fun appendString(): IStringBuilder {
        val times = BENCHMARK_SIZE

        val sb = StringStringBuilder()
        repeat(times) { sb.append(stringToAppend) }
        return sb
    }

    private val sbGetSetLength = BENCHMARK_SIZE
    private val sbStringGetSet = StringStringBuilder().apply { repeat(sbGetSetLength) { append('o') } }
    private val sbCharArrayGetSet = CharArrayStringBuilder().apply { repeat(sbGetSetLength) { append('o') } }

    private fun stringBuilderForGetSet(implementation: Int): IStringBuilder {
        return when (implementation) {
            STRING_STRING_BUILDER -> sbStringGetSet
            CHAR_ARRAY_STRING_BUILDER -> sbCharArrayGetSet
            else -> throw IllegalArgumentException("Unknown implementation: $implementation")
        }
    }

    @Benchmark
    fun getChar(): Char {
        val sb = stringBuilderForGetSet(implementation)
        var result = '0'
        repeat(sbGetSetLength) { result = sb[it] }
        return result
    }

    @Benchmark
    fun setChar() {
        val sb = stringBuilderForGetSet(implementation)
        repeat(sbGetSetLength) { sb.set(it, 'a') }
    }

    private val stringForDeleteLength = BENCHMARK_SIZE
    private val stringForDelete = String(CharArray(stringForDeleteLength) { 'a' + Random.nextInt(26) } )

    private fun stringBuilderFromString(implementation: Int, string: String): IStringBuilder {
        return when (implementation) {
            STRING_STRING_BUILDER -> StringStringBuilder(string)
            CHAR_ARRAY_STRING_BUILDER -> CharArrayStringBuilder(string)
            else -> throw IllegalArgumentException("Unknown implementation: $implementation")
        }
    }

    @Benchmark
    fun builderFromStringForDelete(): IStringBuilder {
        return stringBuilderFromString(implementation, stringForDelete)
    }

    private val deleteCharIndexes = IntArray(stringForDeleteLength) { Random.nextInt(0..it) }.reversedArray()

    @Benchmark
    fun deleteCharAt(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, stringForDelete)
        for (index in deleteCharIndexes) {
            sb.deleteCharAt(index)
        }
        return sb
    }

    private val deleteSubstringRanges = ArrayList<IntRange>().apply {
        var length = stringForDeleteLength

        while (length != 0) {
            val start = Random.nextInt(length)
            val end = Random.nextInt(start..length) // possible empty range

            add(start until end)
            length -= end - start
        }

//        println("deleteSubstringRanges:")
//        println("initial string length: $stringForDeleteLength")
//        println("final string length: $length")
//        println("ranges: $this")
    }

    @Benchmark
    fun deleteSubstring(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, stringForDelete)
        for (range in deleteSubstringRanges) {
            sb.delete(range.start, range.endInclusive + 1)
        }
        return sb
    }

    private val getCharsOperationCount = BENCHMARK_SIZE_MULTIPLIER
    private val getCharsRanges = ArrayList<IntRange>().apply {
        repeat(getCharsOperationCount) {
            val start = Random.nextInt(sbGetSetLength)
            val end = Random.nextInt(start..sbGetSetLength) // possible empty range

            add(start until end)
        }

//        println("getCharsRanges:")
//        println("builder length: $sbGetSetLength")
//        println("ranges: $this")
    }
    private val charArrayForGetChars = CharArray(sbGetSetLength)

    @Benchmark
    fun getChars(): CharArray {
        val sb = stringBuilderForGetSet(implementation)
        for (range in getCharsRanges) {
            sb.getChars(range.start, range.endInclusive + 1, charArrayForGetChars, 0)
        }
        return charArrayForGetChars
    }

    private val insertCharCount = BENCHMARK_SIZE
    private val insertCharIndexes = IntArray(insertCharCount) { Random.nextInt(0..it) }

    @Benchmark
    fun insertChar(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        for (index in insertCharIndexes) {
            sb.insert(index, 'a')
        }
        return sb
    }

    private val insertCharArrayCount = BENCHMARK_SIZE
    private val charArrayToInsert = CharArray(BENCHMARK_SIZE_MULTIPLIER) { Random.nextInt().toChar() }
    private val insertCharArrayIndexes = IntArray(insertCharArrayCount) { Random.nextInt(0..it * charArrayToInsert.size) }

    @Benchmark
    fun insertCharArray(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        for (index in insertCharArrayIndexes) {
            sb.insert(index, charArrayToInsert)
        }
        return sb
    }

    private val insertStringCount = BENCHMARK_SIZE
    private val stringToInsert = String(charArrayToInsert)
    private val insertStringIndexes = IntArray(insertStringCount) { Random.nextInt(0..it * stringToInsert.length) }

    @Benchmark
    fun insertString(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        for (index in insertStringIndexes) {
            sb.insert(index, stringToInsert)
        }
        return sb
    }

    private val insertIntCount = BENCHMARK_SIZE
    private var insertIntResultStringLength = 0
    private val insertIntIndexes = IntArray(insertIntCount) { index ->
        Random.nextInt(0..insertIntResultStringLength).also { insertIntResultStringLength += index.toString().length }
    }

    @Benchmark
    fun insertInt(): IStringBuilder {
        val sb = createEmptyStringBuilder(implementation)
        repeat(insertIntCount) { sb.insert(insertIntIndexes[it], it) }
        return sb
    }

    private val replaceCount = BENCHMARK_SIZE_MULTIPLIER
    private val initialReplaceString = String(CharArray(BENCHMARK_SIZE) { 'a' + Random.nextInt(26) })
    private var stringToReplaceWith = String(CharArray(BENCHMARK_SIZE_MULTIPLIER) { 'A' + Random.nextInt(26) })
    private val replaceRanges = ArrayList<IntRange>().apply {
        var length = initialReplaceString.length
        repeat(replaceCount) {
            val start = Random.nextInt(length)
            val end = Random.nextInt(start..start + 2 * stringToReplaceWith.length).coerceAtMost(length)
            length -= end - start
            length += stringToReplaceWith.length

            add(start until end)
        }

//        println("replaceRanges:")
//        println("initial string length: ${initialReplaceString.length}")
//        println("final string length: $length")
//        println("ranges: $this")
    }

    @Benchmark
    fun builderFromInitialReplaceString(): IStringBuilder {
        return stringBuilderFromString(implementation, initialReplaceString)
    }

    @Benchmark
    fun replace(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, initialReplaceString)
        for (range in replaceRanges) {
            sb.replace(range.start, range.endInclusive + 1, stringToReplaceWith)
        }
        return sb
    }

    @Benchmark
    fun reverse(): IStringBuilder {
        val times = BENCHMARK_SIZE_MULTIPLIER

        val sb = stringBuilderForGetSet(implementation)
        repeat(times) { sb.reverse() }
        return sb
    }

    @Benchmark
    fun substring(): String {
        val sb = stringBuilderForGetSet(implementation)
        var result = ""
        for (range in getCharsRanges) {
            result = sb.substring(range.start, range.endInclusive + 1)
        }
        return result
    }

    private val toStringAppendCharCount = sqrt(BENCHMARK_SIZE.toDouble()).toInt()

    @Benchmark
    fun toStringAppendChar(): String {
        val sb = createEmptyStringBuilder(implementation)
        var result = ""
        repeat(toStringAppendCharCount) {
            sb.append('a')
            result = sb.toString()
        }
        return result
    }

    private val toStringAppendStringCount = log2(BENCHMARK_SIZE.toDouble()).toInt()

    @Benchmark
    fun toStringAppendString(): String {
        val sb = createEmptyStringBuilder(implementation)
        var result = "a"
        repeat(toStringAppendStringCount) {
            sb.append(result)
            result = sb.toString()
        }
        return result
    }
}