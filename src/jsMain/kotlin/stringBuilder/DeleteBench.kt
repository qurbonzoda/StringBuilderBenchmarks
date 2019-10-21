package stringBuilder

import kotlinx.benchmark.*
import kotlin.random.Random

@State(Scope.Benchmark)
class DeleteBench {

    @Param("0", "1")
    var implementation = 0

    @Param("10", "100", "1000", "10000")
    var size: Int = 0

    private var stringForDelete = ""
    private var deleteCharIndexes = intArrayOf()
    private var deleteRanges = ArrayList<IntRange>()

    @Setup
    fun setup() {
        stringForDelete = String(CharArray(size) { 'a' + Random.nextInt(26) } )
        deleteCharIndexes = IntArray(10) { Random.nextInt(0, size - it) }

        deleteRanges.clear()
        var length = size
        repeat(10) {
            if (length == 0) return@repeat
            val start = Random.nextInt(length)
            val end = (start + 10).coerceAtMost(length)
            length -= end - start

            deleteRanges.add(start until end)
        }

//        println("deleteRanges:")
//        println("initial string length: $size")
//        println("final string length: $length")
//        println("ranges: $this")
    }

    @Benchmark
    fun deleteCharAt(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, stringForDelete)
        for (index in deleteCharIndexes) {
            sb.deleteCharAt(index)
        }
        return sb
    }

    @Benchmark
    fun deleteSubstring(): IStringBuilder {
        val sb = stringBuilderFromString(implementation, stringForDelete)
        for (range in deleteRanges) {
            sb.delete(range.start, range.endInclusive + 1)
        }
        return sb
    }
}