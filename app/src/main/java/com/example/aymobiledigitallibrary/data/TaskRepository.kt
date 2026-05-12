package com.example.aymobiledigitallibrary.data

object TaskRepository {
    val distractorNumbers = listOf(47, 62, 31, 88, 75, 14, 53, 26, 91, 40)
    val globalTargetIndices = listOf(2,8,14,20,26)
    val localTargetIndices = listOf(1,6,9,16,23,30)
    val relativeOrderPairs = listOf(3 to 11, 7 to 19, 12 to 25, 16 to 22, 5 to 29)
    val refindingTargetIndices = listOf(5,16,27)
    val workloadItems = listOf(
        "I needed a lot of mental effort to complete the activities.",
        "I felt unsure about where materials were located.",
        "I felt frustrated when trying to find materials again.",
        "It was difficult to remember where materials appeared."
    )
    val usabilityItems = listOf(
        "The interface was easy to use.",
        "The item layout was clear.",
        "It was easy to browse the library.",
        "It was easy to find previously seen materials.",
        "I would use this interface for a mobile digital library."
    )
}
