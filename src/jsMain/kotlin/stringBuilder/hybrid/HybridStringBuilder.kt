package stringBuilder.hybrid

import stringBuilder.IStringBuilder

private const val EXTRA_SPACE = 16

public class HybridStringBuilder(content: String = "") : IStringBuilder {
    constructor(capacity: Int) : this() {}

    private var chars: CharArray? = null
    private var string: String? = content

    init {
        require(capacity >= 0)
    }

    constructor(content: CharSequence) : this(content.toString())

    override var length: Int = 0
        get() {
            val str = string
            if (str != null) return str.length
            return field
        }
        private set

    override fun get(index: Int): Char {
        require(index >= 0 && index < length)
        val str = string
        if (str != null) return str[index]
        return chars!![index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = substring(startIndex, endIndex)

    override fun append(c: Char): HybridStringBuilder {
        val str = string
        if (str != null) string = str + c
        else {
            ensureCharsCapacity(length + 1)
            chars!![length++] = c
        }
        return this
    }

    override fun append(csq: CharSequence?): HybridStringBuilder {
        val nonnullCsq = csq ?: "null"
        return append(csq, 0, nonnullCsq.length)
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): HybridStringBuilder {
        val nonnullCsq = csq ?: "null"
        require(start >= 0 && start <= end && end <= nonnullCsq.length)

        val str = string
        if (str != null) string = str + nonnullCsq
        else copyToChars(nonnullCsq, start, end)
        return this
    }

    override fun append(obj: Any?): HybridStringBuilder {
        return append(obj.toString())
    }

    override fun reverse(): HybridStringBuilder {
        val str = string
        if (str != null) {
            var reversed = ""
            var index = str.length - 1
            while (index >= 0) {
                reversed += str[index--]
            }
            string = reversed
        } else {
            reversePrefix(chars!!, length)
        }
        return this
    }

    override fun clear(): HybridStringBuilder {
        string = ""
        length = 0
        return this
    }

    override fun toString(): String {
        return string ?: String(chars!!, 0, length)
    }

    // new methods

    override val capacity: Int  // remove ???
        get() = length

    override fun append(str: CharArray): HybridStringBuilder {
        return append(str, 0, str.size)
    }

    override fun append(str: CharArray, offset: Int, len: Int): HybridStringBuilder {
        val s = string
        if (s != null) {
            string = s + String(str, offset, len)
        } else {
            ensureCharsCapacity(length + len)
            str.copyInto(chars!!, length, offset, offset + len)
            length += len
        }
        return this
    }

    override fun append(str: String): HybridStringBuilder {
        val s = string
        if (s != null) string = s + str
        else copyToChars(str, 0, str.length)
        return this
    }

    override fun append(i: Int): HybridStringBuilder {
        val str = string
        if (str != null) {
            string = str + i
        } else {
            val intLength = if (i < 0) stringSizeOfInt(-i) + 1 else stringSizeOfInt(i)
            ensureCharsCapacity(length + intLength)

            getIntChars(i, intLength, chars!!, length)
            length += intLength
        }
        return this
    }

    override fun appendCodePoint(codePoint: Int): HybridStringBuilder {
        require(codePoint >= 0)

        val str = string
        if (str != null) {
            if (codePoint <= Char.MAX_VALUE.toInt()) {
                string += codePoint.toChar()
            } else {
                val highSurrogate = ((codePoint - 0x10000) shr 16) and 0x03FF or 0xD800
                val lowSurrogate = codePoint and 0x03FF or 0xDC00
                string = string + highSurrogate.toChar() + lowSurrogate.toChar()
            }
        } else {
            if (codePoint <= Char.MAX_VALUE.toInt()) {
                ensureCharsCapacity(length + 1)
                chars!![length++] = codePoint.toChar()
            } else {
                ensureCharsCapacity(length + 2)
                val highSurrogate = ((codePoint - 0x10000) shr 16) and 0x03FF or 0xD800
                val lowSurrogate = codePoint and 0x03FF or 0xDC00
                val ch = chars!!
                ch[length++] = highSurrogate.toChar()
                ch[length++] = lowSurrogate.toChar()
            }
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

    override fun delete(start: Int, end: Int): HybridStringBuilder {
        require(start <= end)
        val str = string
        string = if (str != null) {
            str.substring(0, start) + str.substring(end)
        } else {
            convertCharsToStringRemoving(start, end)
        }
        return this
    }

    override fun deleteCharAt(index: Int): HybridStringBuilder {
        val str = string
        string = if (str != null) {
            str.substring(0, index) + str.substring(index + 1)
        } else {
            convertCharsToStringRemoving(index, index + 1)
        }
        return this
    }

    override fun ensureCapacity(minimumCapacity: Int) {
        val ch = chars
        if (ch != null) ensureCapacity(ch, minimumCapacity)
    }

    private fun ensureCharsCapacity(minimumCapacity: Int) {
        ensureCapacity(chars!!, minimumCapacity)
    }

    private fun ensureCapacity(ch: CharArray, minimumCapacity: Int) {
        if (ch.size >= minimumCapacity) return

        var newCapacity = (ch.size + 1) * 2
        if (newCapacity < 0) {
            newCapacity = Int.MAX_VALUE
        } else if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity
        }
        chars = ch.copyOf(newCapacity)
    }

    override fun getChars(srcBegin: Int, srcEnd: Int, dst: CharArray, dstBegin: Int) {
        val str = string
        if (str != null) {
            var dstIndex = dstBegin
            for (index in srcBegin until srcEnd) {
                dst[dstIndex++] = str[index]
            }
        } else {
            chars!!.copyInto(dst, dstBegin, srcBegin, srcEnd)
        }
    }

    override fun indexOf(str: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun indexOf(str: String, fromIndex: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(offset: Int, char: Char): HybridStringBuilder {
        val s = string ?: convertCharsToString()
        string = s.substring(0, offset) + char + s.substring(offset)
        return this
    }

    override fun insert(offset: Int, str: CharArray): HybridStringBuilder {
        // string is slower
        val s = string ?: convertCharsToString()
        string = s.substring(0, offset) + String(str) + s.substring(offset)
        return this
    }

    override fun insert(index: Int, str: CharArray, offset: Int, len: Int): HybridStringBuilder {
        val s = string ?: convertCharsToString()
        string = s.substring(0, index) + String(str, offset, len) + s.substring(index)

        return this
    }

    override fun insert(dstOffset: Int, charSequence: CharSequence): HybridStringBuilder {
        val s = string ?: convertCharsToString()
        string = s.substring(0, dstOffset) + charSequence.toString() + s.substring(dstOffset)

        return this
    }

    override fun insert(offset: Int, obj: Any?): HybridStringBuilder {
        return insert(offset, obj.toString())
    }

    override fun insert(offset: Int, str: String): HybridStringBuilder {
        require(offset >= 0 && offset <= length)

        val s = string ?: convertCharsToString()
        string = s.substring(0, offset) + str + s.substring(offset)

        return this
    }

    override fun insert(offset: Int, i: Int): HybridStringBuilder {
        val str = string ?: convertCharsToString()
        string = str.substring(0, offset) + i + str.substring(offset)

        return this
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    override fun replace(start: Int, end: Int, str: String): HybridStringBuilder {
        require(end >= start)

        val s = string ?: convertCharsToString()
        string = s.substring(0, start) + str + s.substring(end)

        return this
    }

    override fun set(index: Int, char: Char): HybridStringBuilder {
        val str = string
        if (str != null) {
            val ch = charsFromString(str)
            ch[index] = char
            string = null
            chars = ch
            length = str.length
        } else {
            chars!![index] = char
        }
        return this
    }

    override fun setLength(newLength: Int): HybridStringBuilder {
        val str = string
        if (str != null) {
            if (newLength <= str.length) string = str.substring(0, newLength)
            else {
                var s = str
                for (i in str.length until newLength) s += '\u0000'
                string = s
            }
        } else {
            if (newLength > length) {
                chars!!.fill('\u0000', length, newLength.coerceAtMost(capacity))
                ensureCharsCapacity(newLength)
            }
            length = newLength
        }
        return this
    }

    override fun substring(start: Int, end: Int): String {
        val str = string
        if (str != null) return str.substring(start, end)
        return String(chars!!, start, end - start)
    }

    override fun trimToSize(): HybridStringBuilder {
        val ch = chars
        if (ch != null && ch.size != length) {
            chars = ch.copyOf(length)
        }
        return this
    }

    private inline fun copyToChars(csq: CharSequence, start: Int, end: Int) {
        ensureCharsCapacity(length + end - start)
        val ch = chars!!
        for (index in start until end) ch[length++] = csq[index]
    }

    private inline fun convertCharsToString(): String {
        val length = this.length
        val chars = this.chars!!
        this.chars = null
        var str = ""
        for (i in 0 until length) {
            str += chars[i]
        }
        return str
    }

    private inline fun convertCharsToStringRemoving(start: Int, end: Int): String {
        val length = this.length
        val chars = this.chars!!
        this.chars = null
        var str = ""
        for (i in 0 until start) str += chars[i]
        for (j in end until length) str += chars[j]
        return str
    }

    private inline fun charsFromString(string: String): CharArray {
        val length = string.length
        val chars = CharArray(length + EXTRA_SPACE)
        for (i in 0 until length) {
            chars[i]  = string[i]
        }
        return chars
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