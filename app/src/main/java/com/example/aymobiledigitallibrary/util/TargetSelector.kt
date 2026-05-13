package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.data.LibraryRepository

object TargetSelector {
    fun globalTargets(): List<Int> = listOf(2, 3, 8, 9, 14, 15, 20, 26)
    fun localTargets(): List<Int> = listOf(2, 3, 8, 9, 14, 15, 20, 26)
    fun refindingTargets(): List<Int> = listOf(22, 2, 28, 9, 16)
    fun itemAt(index: Int) = LibraryRepository.items[index]
}
