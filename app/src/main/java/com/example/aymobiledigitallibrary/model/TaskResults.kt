package com.example.aymobiledigitallibrary.model

data class DistractorResult(
    val participantId: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val completedCount: Int,
    val correctCount: Int
)

data class GlobalRecallResult(
    val participantId: String,
    val browsingMode: BrowsingMode,
    val targetItemId: String,
    val correctIndex: Int,
    val selectedIndex: Int,
    val accuracy: Int,
    val absoluteError: Int,
    val responseTimeMillis: Long
)

data class LocalContextRecallResult(
    val participantId: String,
    val browsingMode: BrowsingMode,
    val targetItemId: String,
    val correctNeighborItemIdsCsv: String,
    val selectedNeighborItemIdsCsv: String,
    val accuracy: Int,
    val responseTimeMillis: Long
)

data class RefindingResult(
    val participantId: String,
    val browsingMode: BrowsingMode,
    val targetItemId: String,
    val selectedItemId: String?,
    val success: Boolean,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val timeToFindMillis: Long,
    val wrongClickCount: Int,
    val nextClickCount: Int,
    val previousClickCount: Int,
    val finalPage: Int?,
    val finalScrollZone: Int?
)
