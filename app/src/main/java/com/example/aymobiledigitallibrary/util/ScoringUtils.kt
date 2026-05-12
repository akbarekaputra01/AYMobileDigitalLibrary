package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.model.LocalRegion

object ScoringUtils {
    fun calculateAccuracy(correctIndex: Int, selectedIndex: Int): Int {
        return if (correctIndex == selectedIndex) 1 else 0
    }

    fun calculateAbsoluteError(correctIndex: Int, selectedIndex: Int): Int {
        return kotlin.math.abs(correctIndex - selectedIndex)
    }

    fun calculateLocalContextAccuracy(correct: LocalRegion, selected: LocalRegion): Int {
        return if (correct == selected) 1 else 0
    }

    fun calculateRefindingSuccess(targetItemId: String, selectedItemId: String?): Boolean {
        return targetItemId == selectedItemId
    }
}
