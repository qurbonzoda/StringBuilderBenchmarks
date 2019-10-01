package stringBuilder.arrayBacked

import stringBuilder.IStringBuilder

private const val EXTRA_SPACE = 16

public class CharArrayStringBuilder(capacity: Int) : IStringBuilder {

    private var chars: CharArray

    init {
        require(capacity >= 0)
        chars = CharArray(capacity)
    }

    constructor(content: String) : this(content.length + EXTRA_SPACE) {
        append(content)
    }

    constructor(content: CharSequence) : this(content.length + EXTRA_SPACE) {
        append(content)
    }

    constructor() : this(EXTRA_SPACE)

    override var length: Int = 0
        private set

    override fun get(index: Int): Char =
        chars.getOrElse(index) { throw IndexOutOfBoundsException("index: $index, length: $length}") }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = substring(startIndex, endIndex)

    override fun append(c: Char): CharArrayStringBuilder {
        ensureCapacity(length + 1)
        chars[length++] = c
        return this
    }

    override fun append(csq: CharSequence?): CharArrayStringBuilder {
        val nonnullCsq = csq ?: "null"
        return append(csq, 0, nonnullCsq.length)
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): CharArrayStringBuilder {
        val nonnullCsq = csq ?: "null"
        require(start >= 0 && start <= end && end <= nonnullCsq.length)

        ensureCapacity(length + end - start)
        for (index in start until end) chars[length++] = nonnullCsq[index]
        return this
    }

    override fun append(obj: Any?): CharArrayStringBuilder {
        return append(obj.toString())
    }

    override fun reverse(): CharArrayStringBuilder {
        reversePrefix(chars, length)
        return this
    }

    override fun clear(): CharArrayStringBuilder {
        length = 0
        return this
    }

    override fun toString(): String = String(chars, 0, length)

    // new methods

    override val capacity: Int
        get() = chars.size

    override fun append(str: CharArray): CharArrayStringBuilder {
        ensureCapacity(length + str.size)
        str.copyInto(chars, length, 0, str.size)
        length += str.size
        return this
    }

    override fun append(str: CharArray, offset: Int, len: Int): CharArrayStringBuilder {
        ensureCapacity(length + len)
        str.copyInto(chars, length, offset, offset + len)
        length += len
        return this
    }

    override fun append(str: String): CharArrayStringBuilder {
        ensureCapacity(length + str.length)
        for (index in 0 until str.length) chars[length++] = str[index]
        return this
    }

    override fun append(i: Int): CharArrayStringBuilder {
        val intLength = if (i < 0) stringSizeOfInt(-i) + 1 else stringSizeOfInt(i)
        ensureCapacity(length + intLength)

        getIntChars(i, intLength, chars, length)
        length += intLength

        return this
    }

    override fun appendCodePoint(codePoint: Int): CharArrayStringBuilder {
        require(codePoint >= 0)

        if (codePoint <= Char.MAX_VALUE.toInt()) {
            ensureCapacity(length + 1)
            chars[length++] = codePoint.toChar()
        } else {
            ensureCapacity(length + 2)
            val highSurrogate = ((codePoint - 0x10000) shr 16) and 0x03FF or 0xD800
            val lowSurrogate = codePoint and 0x03FF or 0xDC00
            chars[length++] = highSurrogate.toChar()
            chars[length++] = lowSurrogate.toChar()
        }
        return this
    }

    override fun codePointAt(index: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun codePointBefore(index: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun codePointCount(beginIndex: Int, endIndex: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(start: Int, end: Int): CharArrayStringBuilder {
        require(start <= end)
        chars.copyInto(destination = chars, destinationOffset = start, startIndex = end, endIndex = length)
        length -= end - start
        return this
    }

    override fun deleteCharAt(index: Int): CharArrayStringBuilder {
        chars.copyInto(destination = chars, destinationOffset = index, startIndex = index + 1, endIndex = length)
        length -= 1
        return this
    }

    override fun ensureCapacity(minimumCapacity: Int) {
        if (chars.size >= minimumCapacity) return

        var newCapacity = (chars.size + 1) * 2
        if (newCapacity < 0) {
            newCapacity = Int.MAX_VALUE
        } else if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity
        }
        chars = chars.copyOf(newCapacity)
    }

    override fun getChars(srcBegin: Int, srcEnd: Int, dst: CharArray, dstBegin: Int) {
        chars.copyInto(dst, dstBegin, srcBegin, srcEnd)
    }

    override fun indexOf(str: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun indexOf(str: String, fromIndex: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(offset: Int, char: Char): CharArrayStringBuilder {
        ensureCapacity(length + 1)
        chars.copyInto(destination = chars, destinationOffset = offset + 1, startIndex = offset, endIndex = length)
        chars[offset] = char
        length += 1
        return this
    }

    override fun insert(offset: Int, str: CharArray): CharArrayStringBuilder {
        ensureCapacity(length + str.size)
        chars.copyInto(destination = chars, destinationOffset = offset + str.size, startIndex = offset, endIndex = length)
        str.copyInto(destination = chars, destinationOffset = offset, startIndex = 0, endIndex = str.size)
        length += str.size
        return this
    }

    override fun insert(index: Int, str: CharArray, offset: Int, len: Int): CharArrayStringBuilder {
        ensureCapacity(length + len)
        chars.copyInto(destination = chars, destinationOffset = index + len, startIndex = index, endIndex = length)
        str.copyInto(destination = chars, destinationOffset = index, startIndex = offset, endIndex = offset + len)
        length += len
        return this
    }

    override fun insert(dstOffset: Int, charSequence: CharSequence): CharArrayStringBuilder {
        ensureCapacity(length + charSequence.length)
        chars.copyInto(destination = chars, destinationOffset = dstOffset + chars.size, startIndex = dstOffset, endIndex = length)

        var index = dstOffset
        for (char in charSequence) chars[index++] = char
        length += charSequence.length

        return this
    }

    override fun insert(offset: Int, obj: Any?): CharArrayStringBuilder {
        return insert(offset, obj.toString())
    }

    override fun insert(offset: Int, str: String): CharArrayStringBuilder {
        require(offset >= 0 && offset <= length)

        ensureCapacity(length + str.length)
        chars.copyInto(destination = chars, destinationOffset = offset + str.length, startIndex = offset, endIndex = length)
        var insertIndex = offset
        for (index in 0 until str.length) chars[insertIndex++] = str[index]
        length += str.length

        return this
    }

    override fun insert(offset: Int, i: Int): CharArrayStringBuilder {
        if (i == Int.MIN_VALUE) return insert(offset, "-2147483648")
        val intLength = if (i < 0) stringSizeOfInt(-i) + 1 else stringSizeOfInt(i)
        ensureCapacity(length + intLength)

        chars.copyInto(chars, offset + intLength, offset, length)
        getIntChars(i, intLength, chars, offset)
        length += intLength

        return this
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    override fun replace(start: Int, end: Int, str: String): CharArrayStringBuilder {
        require(end >= start)

        val lengthDiff = str.length - (end - start)
        ensureCapacity(length + lengthDiff)
        chars.copyInto(destination = chars, destinationOffset = start + str.length, startIndex = end, endIndex = length)
        var replaceIndex = start
        for (index in 0 until str.length) chars[replaceIndex++] = str[index]
        length += lengthDiff

        return this
    }

    override fun set(index: Int, char: Char): CharArrayStringBuilder {
        chars[index] = char
        return this
    }

    override fun setLength(newLength: Int): CharArrayStringBuilder {
        if (newLength > length) {
            chars.fill('\u0000', length, newLength.coerceAtMost(capacity))
            ensureCapacity(newLength)
        }
        length = newLength
        return this
    }

    override fun substring(start: Int, end: Int): String {
        return String(chars, start, end - start)
    }

    override fun trimToSize(): CharArrayStringBuilder {
        if (chars.size != length) {
            chars = chars.copyOf(length)
        }
        return this
    }
}

private fun reversePrefix(chars: CharArray, endIndex: Int) {
    val midPoint = (endIndex / 2) - 1
    if (midPoint < 0) return
    var reverseIndex = endIndex - 1
    for (index in 0..midPoint) {
        val tmp = chars[index]
        chars[index] = chars[reverseIndex]
        chars[reverseIndex] = tmp
        reverseIndex--
    }
}

private fun stringSizeOfInt(int: Int): Int {
    require(int >= 0)

    var exp = 10
    for (size in 1 until 10) {
        if (int < exp) return size
        exp *= 10
    }
    return 10
}

private fun getIntChars(int: Int, size: Int, dst: CharArray, dstBegin: Int) {
    val start: Int
    var positiveInt = if (int < 0) {
        dst[dstBegin] = '-'
        start = dstBegin + 1
        -int
    } else {
        start = dstBegin
        int
    }

    val end = dstBegin + size - 1

    for (index in end downTo start) {
        dst[index] = ((positiveInt % 10) + 48).toChar()
        positiveInt /= 10
    }
}