package com.example.townpass.util

object IdNumberValidator {
    private val letterMap = mapOf(
        'A' to 10, 'B' to 11, 'C' to 12, 'D' to 13, 'E' to 14,
        'F' to 15, 'G' to 16, 'H' to 17, 'I' to 34, 'J' to 18,
        'K' to 19, 'L' to 20, 'M' to 21, 'N' to 22, 'O' to 35,
        'P' to 23, 'Q' to 24, 'R' to 25, 'S' to 26, 'T' to 27,
        'U' to 28, 'V' to 29, 'W' to 32, 'X' to 30, 'Y' to 31,
        'Z' to 33
    )

    private val weights = intArrayOf(1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1)

    fun isValid(id: String?): Boolean {
        if (id.isNullOrBlank()) return false
        val trimmed = id.trim().uppercase()
        val regex = Regex("^[A-Z][1289]\\d{8}$")
        if (!regex.matches(trimmed)) return false

        val letterVal = letterMap[trimmed[0]] ?: return false
        val n1 = letterVal / 10
        val n2 = letterVal % 10

        val numbers = IntArray(11)
        numbers[0] = n1
        numbers[1] = n2
        for (i in 1..9) {
            numbers[i + 1] = trimmed[i] - '0'
        }

        var sum = 0
        for (i in 0..10) {
            sum += numbers[i] * weights[i]
        }

        return sum % 10 == 0
    }
}
