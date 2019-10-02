package stringBuilder.stringBacked

import stringBuilder.IStringBuilder

public class StringStringBuilder(content: String = "") : IStringBuilder {
    constructor(capacity: Int) : this() {}

    constructor(content: CharSequence) : this(content.toString()) {}

    constructor() : this("")

    private var string: String = content

    override val length: Int
        get() = string.length

    override fun get(index: Int): Char {
        require(index >= 0 && index < length)
        return string[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = string.substring(startIndex, endIndex)

    override fun append(c: Char): StringStringBuilder {
        string += c
        return this
    }

    override fun append(csq: CharSequence?): StringStringBuilder {
        string += csq.toString()
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): StringStringBuilder {
        string += csq.toString().substring(start, end)
        return this
    }

    override fun append(obj: Any?): StringStringBuilder {
        string += obj.toString()
        return this
    }

    override fun reverse(): StringStringBuilder {
        var reversed = ""
        var index = string.length - 1
        while (index >= 0) {
            reversed += string[index--]
        }
        string = reversed
        return this
    }

    override fun clear(): StringStringBuilder {
        string = ""
        return this
    }

    override fun toString(): String = string

    // new methods

    override val capacity: Int
        get() = TODO("not implemented") // ensureCapacity and this method should be related/consistent.

    override fun append(str: CharArray): StringStringBuilder {
        string += String(str)
        return this
    }

    override fun append(str: CharArray, offset: Int, len: Int): StringStringBuilder {
        string += String(str, offset, len)
        return this
    }

    override fun append(str: String): StringStringBuilder {
        string += str
        return this
    }

    override fun append(i: Int): StringStringBuilder {
        string += i.toString()
        return this
    }

    override fun appendCodePoint(codePoint: Int): StringStringBuilder {
        require(codePoint >= 0)

        if (codePoint <= Char.MAX_VALUE.toInt()) {
            string += codePoint.toChar()
        } else {

            val highSurrogate = ((codePoint - 0x10000) shr 16) and 0x03FF or 0xD800
            val lowSurrogate = codePoint and 0x03FF or 0xDC00
            string = string + highSurrogate.toChar() + lowSurrogate.toChar()
        }
        return this
    }

    override fun codePointAt(index: Int): Int {
        return string.asDynamic().codePointAt(index)
    }

    override fun codePointBefore(index: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun codePointCount(beginIndex: Int, endIndex: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(start: Int, end: Int): StringStringBuilder {
        string = string.substring(0, start) + string.substring(end)
        return this
    }

    override fun deleteCharAt(index: Int): StringStringBuilder {
        string = string.substring(0, index) + string.substring(index + 1)
        return this
    }

    override fun ensureCapacity(minimumCapacity: Int) {
        // do nothing
    }

    override fun getChars(srcBegin: Int, srcEnd: Int, dst: CharArray, dstBegin: Int) {
        var dstIndex = dstBegin
        for (index in srcBegin until srcEnd) {
            dst[dstIndex++] = string[index]
        }
    }

    override fun indexOf(str: String): Int {
        return string.indexOf(str)
    }

    override fun indexOf(str: String, fromIndex: Int): Int {
        return string.indexOf(str, fromIndex)
    }

    override fun insert(offset: Int, char: Char): StringStringBuilder {
        string = string.substring(0, offset) + char + string.substring(offset)
        return this
    }

    override fun insert(offset: Int, str: CharArray): StringStringBuilder {
        // benchmark Array.join function
        string = string.substring(0, offset) + String(str) + string.substring(offset)
        return this
    }

    override fun insert(index: Int, str: CharArray, offset: Int, len: Int): StringStringBuilder {
        string = string.substring(0, index) + String(str, offset, len) + string.substring(index)
        return this
    }

    override fun insert(dstOffset: Int, charSequence: CharSequence): StringStringBuilder {
        string = string.substring(0, dstOffset) + charSequence.toString() + string.substring(dstOffset)
        return this
    }

    override fun insert(offset: Int, obj: Any?): StringStringBuilder {
        string = string.substring(0, offset) + obj.toString() + string.substring(offset)
        return this
    }

    override fun insert(offset: Int, str: String): StringStringBuilder {
        // benchmark Array.join function
        string = string.substring(0, offset) + str + string.substring(offset)
        return this
    }

    override fun insert(offset: Int, i: Int): StringStringBuilder {
        // benchmark Array.join function
        string = string.substring(0, offset) + i + string.substring(offset)
        return this
    }

    override fun replace(start: Int, end: Int, str: String): StringStringBuilder {
        string = string.substring(0, start) + str + string.substring(end)
        return this
    }

    override fun set(index: Int, char: Char): StringStringBuilder {
        string = string.substring(0, index) + char + string.substring(index + 1)
        return this
    }

    override fun setLength(newLength: Int): StringStringBuilder {
        if (newLength <= length) {
            string = string.substring(0, newLength)
        } else {
            // maybe optimizable
            string += String(CharArray(newLength - length))
        }
        return this
    }

    override fun substring(start: Int, end: Int): String {
        return string.substring(start, end)
    }

    override fun trimToSize(): StringStringBuilder {
        return this
    }
}