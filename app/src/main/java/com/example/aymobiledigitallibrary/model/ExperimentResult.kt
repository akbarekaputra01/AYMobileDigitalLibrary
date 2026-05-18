package com.example.aymobiledigitallibrary.model

data class SummaryMetrics(
    val globalLocationAccuracy: Double = 0.0,
    val globalLocationAccuracySd: Double = 0.0,
    val meanGlobalAbsoluteError: Double = 0.0,
    val globalAbsoluteErrorSd: Double = 0.0,
    val localContextAccuracy: Double = 0.0,
    val localContextAccuracySd: Double = 0.0,
    val refindingSuccessRate: Double = 0.0,
    val meanRefindingTimeMillis: Double = 0.0,
    val refindingTimeMillisSd: Double = 0.0,
    val totalWrongClicks: Int = 0,
    val totalNextClicks: Int = 0,
    val totalPreviousClicks: Int = 0,
    val meanExperienceScore: Double = 0.0,
    val experienceScoreSd: Double = 0.0
)

data class ExperimentResult(
    val participantId: String = "",
    val participantInfo: ParticipantInfo? = null,
    val browsingMode: BrowsingMode? = null,
    val sessionStartTime: Long = 0L,
    val distractorResult: DistractorResult? = null,
    val globalRecallResults: List<GlobalRecallResult> = emptyList(),
    val localContextRecallResults: List<LocalContextRecallResult> = emptyList(),
    val refindingResults: List<RefindingResult> = emptyList(),
    val questionnaireResponses: List<QuestionnaireResponse> = emptyList(),
    val interactionEvents: List<InteractionEvent> = emptyList(),
    val summaryMetrics: SummaryMetrics? = null
)
