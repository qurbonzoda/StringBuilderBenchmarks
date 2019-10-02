package stringBuilder

import kotlinx.benchmark.*
import kotlin.random.Random
import kotlin.random.nextInt

@State(Scope.Benchmark)
open class InsertBench {

    @Param("0", "1")
    var implementation = 0

    @Param("10", "100", "1000", "10000")
    var size: Int = 0

    var initialString = ""

    private var insertIndexes = intArrayOf()

    @Setup
    fun insertSetup() {
        initialString = String(CharArray(size) { Random.nextInt().toChar() })
        insertIndexes = IntArray(10) { Random.nextInt(size + it) }
    }

    @Benchmark
    fun insertChar(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, initialString)
        for (index in insertIndexes) {
            sb.insert(index, 'a')
        }
        return sb
    }

    @Benchmark
    fun insertInt(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, initialString)
        for (index in insertIndexes) {
            sb.insert(index, index)
        }
        return sb
    }
}

@State(Scope.Benchmark)
class InsertStringBench {

    @Param("0", "1")
    var implementation = 0

    @Param("10", "100", "1000", "10000")
    var size: Int = 0

    @Param("1", "10", "1000")
    var insertLength: Int = 0

    private var initialString = ""
    private var charArrayToInsert = charArrayOf()
    private var stringToInsert = ""
    private var insertIndexes = intArrayOf()
    private var replaceRanges = ArrayList<IntRange>()

    @Setup
    fun setup() {
        initialString = String(CharArray(size) { Random.nextInt().toChar() })
        charArrayToInsert = CharArray(insertLength) { Random.nextInt().toChar() }
        stringToInsert = String(charArrayToInsert)

        insertIndexes = IntArray(10) { Random.nextInt(size + it * insertLength) }

        replaceRanges.clear()
        var length = initialString.length
        repeat(10) {
            val start = Random.nextInt(length)
            val end = Random.nextInt(start..start + 2 * insertLength).coerceAtMost(length)
            length -= end - start
            length += insertLength

            replaceRanges.add(start until end)
        }

//        println("replaceRanges:")
//        println("initial string length: ${initialString.length}")
//        println("final string length: $length")
//        println("ranges: $this")
    }

    @Benchmark
    fun insertCharArray(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, initialString)
        for (index in insertIndexes) {
            sb.insert(index, charArrayToInsert)
        }
        return sb
    }

    @Benchmark
    fun insertString(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, initialString)
        for (index in insertIndexes) {
            sb.insert(index, stringToInsert)
        }
        return sb
    }

    @Benchmark
    fun replace(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, initialString)
        for (range in replaceRanges) {
            sb.replace(range.start, range.endInclusive + 1, stringToInsert)
        }
        return sb
    }
}