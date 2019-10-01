package stringBuilder

interface IStringBuilder : Appendable, CharSequence {
    override val length: Int

    override fun get(index: Int): Char

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence

    override fun append(c: Char): IStringBuilder
    override fun append(csq: CharSequence?): IStringBuilder
    override fun append(csq: CharSequence?, start: Int, end: Int): IStringBuilder

    fun append(obj: Any?): IStringBuilder

    fun reverse(): IStringBuilder

    fun clear(): IStringBuilder

    override fun toString(): String

    // new methods

    val capacity: Int

    fun append(str: CharArray): IStringBuilder
    fun append(str: CharArray, offset: Int, len: Int): IStringBuilder
    fun append(str: String): IStringBuilder
    fun append(i: Int): IStringBuilder
    fun appendCodePoint(codePoint: Int): IStringBuilder

    fun codePointAt(index: Int): Int
    fun codePointBefore(index: Int): Int
    fun codePointCount(beginIndex: Int, endIndex: Int): Int

    fun delete(start: Int, end: Int): IStringBuilder
    fun deleteCharAt(index: Int): IStringBuilder

    fun ensureCapacity(minimumCapacity: Int)

    fun getChars(srcBegin: Int, srcEnd: Int, dst: CharArray, dstBegin: Int)

    fun indexOf(str: String): Int
    fun indexOf(str: String, fromIndex: Int): Int

    fun insert(offset: Int, char: Char): IStringBuilder
    fun insert(offset: Int, str: CharArray): IStringBuilder
    fun insert(index: Int, str: CharArray, offset: Int, len: Int): IStringBuilder
    fun insert(dstOffset: Int, charSequence: CharSequence): IStringBuilder
    fun insert(offset: Int, obj: Any?): IStringBuilder
    fun insert(offset: Int, str: String): IStringBuilder
    fun insert(offset: Int, i: Int): IStringBuilder

    fun replace(start: Int, end: Int, str: String): IStringBuilder

    fun set(index: Int, char: Char): IStringBuilder

    // make `length` property `var` ?
    fun setLength(newLength: Int): IStringBuilder

    fun substring(start: Int, end: Int): String

    fun trimToSize(): IStringBuilder
}