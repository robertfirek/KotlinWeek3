package nicestring

import org.junit.Assert
import org.junit.Test

class TestNiceStrings {

    private fun testNiceString(string: String, expected: Boolean) {
        Assert.assertEquals("Wrong result for \"$string\".isNice()", expected, string.isNice())
    }

    @Test
    fun testExample1_0() = testNiceString("bac", false)

    @Test
    fun testExample1_1() = testNiceString("xxxxbec", false)

    @Test
    fun testExample1_2() = testNiceString("bu", false)

    @Test
    fun testExample2() = testNiceString("aza", false)

    @Test
    fun testExample3() = testNiceString("abaca", false)

    @Test
    fun testExample4() = testNiceString("baaa", true)

    @Test
    fun testExample5() = testNiceString("aaab", true)
}