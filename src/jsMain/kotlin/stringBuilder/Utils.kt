package stringBuilder

import stringBuilder.arrayBacked.CharArrayStringBuilder
import stringBuilder.stringBacked.StringStringBuilder

const val STRING_STRING_BUILDER = 0
const val CHAR_ARRAY_STRING_BUILDER = 1

fun createEmptyStringBuilder(implementation: Int): IStringBuilder {
    return when (implementation) {
        STRING_STRING_BUILDER -> StringStringBuilder()
        CHAR_ARRAY_STRING_BUILDER -> CharArrayStringBuilder()
        else -> throw IllegalArgumentException("Unknown implementation: $implementation")
    }
}

fun stringBuilderFromString(implementation: Int, string: String): IStringBuilder {
    return when (implementation) {
        STRING_STRING_BUILDER -> StringStringBuilder(string)
        CHAR_ARRAY_STRING_BUILDER -> CharArrayStringBuilder(string)
        else -> throw IllegalArgumentException("Unknown implementation: $implementation")
    }
}