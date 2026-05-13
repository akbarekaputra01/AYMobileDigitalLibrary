package com.example.aymobiledigitallibrary.model

data class InteractionEvent(
    val participantId: String,
    val browsingMode: BrowsingMode?,
    val phase: String,
    val eventType: String,
    val itemId: String? = null,
    val value: String? = null,
    val timestampMillis: Long = System.currentTimeMillis()
)
