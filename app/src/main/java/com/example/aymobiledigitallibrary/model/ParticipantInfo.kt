package com.example.aymobiledigitallibrary.model

data class ParticipantInfo(
    val participantId: String,
    val age: String,
    val gender: String,
    val scrollingFamiliarity: Int,
    val paginationFamiliarity: Int,
    val readingAppFrequency: Int,
    val digitalLibraryFrequency: Int,
    val spatialAbility: Int
)
