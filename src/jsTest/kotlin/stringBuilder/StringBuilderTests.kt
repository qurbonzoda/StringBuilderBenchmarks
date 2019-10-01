package stringBuilder

import stringBuilder.arrayBacked.CharArrayStringBuilder
import stringBuilder.stringBacked.StringStringBuilder
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class StringBuilderTests {

    private fun createStringBuilders() = listOf(StringStringBuilder(), CharArrayStringBuilder())
    private fun createStringBuilders(string: String) = listOf(StringStringBuilder(string), CharArrayStringBuilder(string))

    @Test
    fun empty() {
        createStringBuilders().forEach { sb ->
            assertEquals(0, sb.length)
            assertEquals("", sb.toString())
        }
    }

    @Test
    fun appendChar() {
        val times = 100
        val expected = "a".repeat(times)

        createStringBuilders().forEach { sb ->
            repeat(times) { sb.append("a") }
            assertEquals(expected, sb.toString())
        }
    }

    @Test
    fun appendInt() {
        val times = 100
        val expected = (0..99).fold("") { res, idx -> res + idx }

        createStringBuilders().forEach { sb ->
            repeat(times) { sb.append(it) }
            assertEquals(expected, sb.toString())
        }
    }

    @Test
    fun appendString() {
        val times = 100
        val expected = "foo".repeat(times)

        createStringBuilders().forEach { sb ->
            repeat(times) { sb.append("foo") }
            assertEquals(expected, sb.toString())
        }
    }

    @Test
    fun appendCharArray() {
        val times = 100
        val expected = "foo".repeat(times)

        createStringBuilders().forEach { sb ->
            repeat(times) { sb.append(charArrayOf('f', 'o', 'o')) }
            assertEquals(expected, sb.toString())
        }
    }

    @Test
    fun getChar() {
        val times = 100
        val expected = mutableListOf<Char>()

        val stringBuilders = createStringBuilders()
        repeat(times) {
            val char = Random.nextInt().toChar()
            stringBuilders.forEach { it.append(char) }
            expected.add(char)
        }

        expected.forEachIndexed { index, char ->
            stringBuilders.forEach { assertEquals(char, it[index]) }
        }
    }

    @Test
    fun setChar() {
        createStringBuilders("my set test").forEach { sb ->
            sb.set(0, 'M')
            assertEquals("My set test", sb.toString())
            sb.set(2, 'm')
            assertEquals("Mymset test", sb.toString())
            sb.set(10, 'T')
            assertEquals("Mymset tesT", sb.toString())
        }
    }

    @Test
    fun deleteChar() {
        createStringBuilders("my delete test").forEach { sb ->
            sb.deleteCharAt(0)
            assertEquals("y delete test", sb.toString())
            sb.deleteCharAt(5)
            assertEquals("y delte test", sb.toString())
            sb.deleteCharAt(11)
            assertEquals("y delte tes", sb.toString())
        }
    }

    @Test
    fun deleteSubstring() {
        createStringBuilders("my delete substring test").forEach { sb ->
            sb.delete(0, 2)
            assertEquals(" delete substring test", sb.toString())
            sb.delete(7, 17)
            assertEquals(" delete test", sb.toString())
            sb.delete(8, 12)
            assertEquals(" delete ", sb.toString())
        }
    }

    @Test
    fun getChars() {
        createStringBuilders("my getChars test").forEach { sb ->
            val chars = CharArray(10) { '_' }

            sb.getChars(0, 2, chars, 8)
            assertEquals("________my", String(chars))
            sb.getChars(6, 11, chars, 3)
            assertEquals("___Charsmy", String(chars))
            sb.getChars(13, 16, chars, 0)
            assertEquals("estCharsmy", String(chars))
        }
    }

    @Test
    fun insertChar() {
        createStringBuilders("my insert char test").forEach { sb ->
            sb.insert(0, '_')
            assertEquals("_my insert char test", sb.toString())
            sb.insert(10, 'T')
            assertEquals("_my insertT char test", sb.toString())
            sb.insert(21, '_')
            assertEquals("_my insertT char test_", sb.toString())
        }
    }

    @Test
    fun insertCharArray() {
        createStringBuilders("my insert CharArray test").forEach { sb ->
            if (sb is CharArrayStringBuilder) return@forEach
            sb.insert(0, charArrayOf('_'))
            assertEquals("_my insert CharArray test", sb.toString())
            sb.insert(10, charArrayOf('T'))
            assertEquals("_my insertT CharArray test", sb.toString())
            sb.insert(26, charArrayOf('_', '*', '#'), 0, 1)
            assertEquals("_my insertT CharArray test_", sb.toString())
        }
    }

    @Test
    fun insertString() {
        createStringBuilders("my insert string test").forEach { sb ->
            sb.insert(0, "_")
            assertEquals("_my insert string test", sb.toString())
            sb.insert(10, "TtT")
            assertEquals("_my insertTtT string test", sb.toString())
            sb.insert(25, "_!_")
            assertEquals("_my insertTtT string test_!_", sb.toString())
        }
    }

    @Test
    fun insertInt() {
        createStringBuilders("my insert int test").forEach { sb ->
            sb.insert(0, 0)
            assertEquals("0my insert int test", sb.toString())
            sb.insert(10, Int.MAX_VALUE)
            assertEquals("0my insert2147483647 int test", sb.toString())
            sb.insert(24, Int.MIN_VALUE + 1)
            assertEquals("0my insert2147483647 int-2147483647 test", sb.toString())
            sb.insert(40, Int.MIN_VALUE)
            assertEquals("0my insert2147483647 int-2147483647 test-2147483648", sb.toString())
        }
    }

    @Test
    fun replace() {
        createStringBuilders("my replace test").forEach { sb ->
            sb.replace(0, 4, "R")
            assertEquals("Replace test", sb.toString())
            sb.replace(7, 7, " empty string")
            assertEquals("Replace empty string test", sb.toString())
            sb.replace(20, 25, "")
            assertEquals("Replace empty string", sb.toString())
        }
    }

    @Test
    fun setLength() {
        createStringBuilders("my setLength test").forEach { sb ->
            sb.setLength(17)
            assertEquals("my setLength test", sb.toString())
            sb.setLength(0)
            assertEquals("", sb.toString())
            sb.setLength(5)
            assertEquals("\u0000\u0000\u0000\u0000\u0000", sb.toString())
        }
    }

    @Test
    fun reverse() {
        createStringBuilders("my reverse test").forEach { sb ->
            sb.reverse()
            assertEquals("tset esrever ym", sb.toString())
        }
    }

    @Test
    fun substring() {
        createStringBuilders("my substring test").forEach { sb ->
            assertEquals("my ", sb.substring(0, 3))
            assertEquals("substring", sb.substring(3, 12))
            assertEquals("ing test", sb.substring(9, 17))
        }
    }
}