package com.example.townpass.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DateTimeUtilsTest {

    @Test
    fun formatDate_returnsFormattedDate() {
        val result = DateTimeUtils.formatDate(1717430400000L)
        assertTrue(result.contains("2024"))
    }

    @Test
    fun formatRange_handlesValidTimestamps() {
        val range = DateTimeUtils.formatRange(1717430400000L, 1735574400000L)
        assertTrue(range.contains("-"))
    }

    @Test
    fun formatRange_handlesEmpty() {
        assertEquals("", DateTimeUtils.formatRange(0L, 0L))
    }
}
