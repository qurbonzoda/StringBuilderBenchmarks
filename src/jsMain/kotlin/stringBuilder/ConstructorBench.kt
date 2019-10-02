package stringBuilder

import kotlinx.benchmark.*
import kotlin.random.Random

@State(Scope.Benchmark)
open class ConstructorBench {

    @Param("0", "1")
    var implementation = 0

    @Param("10", "100", "1000", "10000", "100000")
    var size: Int = 0

    private var string = ""

    @Setup
    fun setup() {
        string = String(CharArray(size) { Random.nextInt().toChar() })
    }

    @Benchmark
    fun emptyStringBuilder(): IStringBuilder {
        return createEmptyStringBuilder(implementation)
    }

    @Benchmark
    fun builderFromString(): IStringBuilder {
        return stringBuilderFromString(implementation, string)
    }
}