package stringBuilder

import kotlinx.benchmark.*

@State(Scope.Benchmark)
open class ToStringBench {

    @Param("0", "1")
    var implementation = 0

    @Param("10", "100", "1000")
    var size: Int = 0

    @Benchmark
    fun toStringAppendChar(): String {
        val sb = createEmptyStringBuilder(implementation)
        var result = ""
        repeat(size) {
            result = sb.toString()
            sb.append('a')
        }
        return result
    }
}