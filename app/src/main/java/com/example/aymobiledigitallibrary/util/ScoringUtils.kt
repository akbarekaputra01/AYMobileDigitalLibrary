package com.example.aymobiledigitallibrary.util

object ScoringUtils {
    fun calculateAccuracy(correct: Int, selected: Int): Int = if (correct == selected) 1 else 0
    fun calculateAbsoluteError(correct: Int, selected: Int): Int = kotlin.math.abs(correct - selected)
    fun calculateNeighborPairAccuracy(correctIds: List<String>, selectedIds: List<String>): Int = if (correctIds.toSet() == selectedIds.toSet()) 1 else 0
}
