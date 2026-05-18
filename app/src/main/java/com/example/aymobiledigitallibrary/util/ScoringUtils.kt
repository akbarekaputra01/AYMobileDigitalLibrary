package com.example.aymobiledigitallibrary.util

object ScoringUtils {
    fun calculateAccuracy(correct: Int, selected: Int): Int {
        val diff = kotlin.math.abs(correct - selected)
        return when (diff) {
            0 -> 100
            1 -> 50
            else -> 0
        }
    }
    fun calculateAbsoluteError(correct: Int, selected: Int): Int = kotlin.math.abs(correct - selected)
    fun calculateNeighborPairAccuracy(correctIds: List<String>, selectedIds: List<String>): Int = if (correctIds.toSet() == selectedIds.toSet()) 100 else 0
}
