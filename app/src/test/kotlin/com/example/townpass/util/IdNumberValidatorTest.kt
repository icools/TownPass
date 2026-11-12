package com.example.townpass.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IdNumberValidatorTest {

    @Test
    fun validIdNumbers_returnTrue() {
        // A123456789:
        // A=10 -> 1*1 + 0*9 = 1
        // 1*8 + 2*7 + 3*6 + 4*5 + 5*4 + 6*3 + 7*2 + 8*1 + 9*1
        // 1 + 8 + 14 + 18 + 20 + 20 + 18 + 14 + 8 + 9 = 130 (130 % 10 == 0) -> Valid!
        assertTrue(IdNumberValidator.isValid("A123456789"))
        assertTrue(IdNumberValidator.isValid("a123456789")) // lowercase handling
    }

    @Test
    fun invalidIdNumbers_returnFalse() {
        assertFalse(IdNumberValidator.isValid(null))
        assertFalse(IdNumberValidator.isValid(""))
        assertFalse(IdNumberValidator.isValid("1234567890")) // Missing starting letter
        assertFalse(IdNumberValidator.isValid("A12345678")) // Too short
        assertFalse(IdNumberValidator.isValid("A1234567890")) // Too long
        assertFalse(IdNumberValidator.isValid("A323456789")) // Second digit not 1 or 2
        assertFalse(IdNumberValidator.isValid("A123456788")) // Incorrect checksum
    }
}
