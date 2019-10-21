package stringBuilder

import stringBuilder.arrayBacked.CharArrayStringBuilder
import stringBuilder.hybrid.HybridStringBuilder
import stringBuilder.stringBacked.StringStringBuilder

const val STRING_STRING_BUILDER = 0
const val CHAR_ARRAY_STRING_BUILDER = 1
const val HYBRID_STRING_BUILDER = 2

fun createEmptyStringBuilder(implementation: Int): IStringBuilder {
    return when (implementation) {
        STRING_STRING_BUILDER -> StringStringBuilder()
        CHAR_ARRAY_STRING_BUILDER -> CharArrayStringBuilder()
        HYBRID_STRING_BUILDER -> HybridStringBuilder()
        else -> throw IllegalArgumentException("Unknown implementation: $implementation")
    }
}

fun stringBuilderFromString(implementation: Int, string: String): IStringBuilder {
    return when (implementation) {
        STRING_STRING_BUILDER -> StringStringBuilder(string)
        CHAR_ARRAY_STRING_BUILDER -> CharArrayStringBuilder(string)
        HYBRID_STRING_BUILDER -> HybridStringBuilder(string)
        else -> throw IllegalArgumentException("Unknown implementation: $implementation")
    }
}