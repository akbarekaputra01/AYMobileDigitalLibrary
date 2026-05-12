package com.example.aymobiledigitallibrary.model

data class SummaryMetrics(
    val globalLocationAccuracy: Double = 0.0,
    val meanGlobalAbsoluteError: Double = 0.0,
    val localRegionAccuracy: Double = 0.0,
    val relativeOrderAccuracy: Double = 0.0,
    val refindingSuccessRate: Double = 0.0,
    val meanRefindingTime: Double = 0.0,
    val totalWrongClicks: Int = 0,
    val totalScrollCount: Int = 0,
    val totalPageClicks: Int = 0,
    val meanWorkloadScore: Double = 0.0,
    val meanUsabilityScore: Double = 0.0
)

data class RefindingResult(val targetItemId:String,val success:Boolean,val timeToFindMs:Long,val wrongClickCount:Int,val scrollCount:Int=0,val maxScrollDepth:Float=0f,val pageClickCount:Int=0,val finalPage:Int=1,val timestamp:Long=System.currentTimeMillis())
data class DistractorAnswer(val questionIndex:Int,val number:Int,val correctAnswer:String,val participantAnswer:String,val responseTimeMs:Long,val isCorrect:Boolean,val timestamp:Long=System.currentTimeMillis())

data class ExperimentResult(
    val participantId: String = "",
    val participantInfo: ParticipantInfo? = null,
    val browsingMode: BrowsingMode? = null,
    val sessionStartTime: Long = 0L,
    val browsingStartTime: Long = 0L,
    val browsingEndTime: Long = 0L,
    val browsingDurationMs: Long = 0,
    val interactionEvents:List<InteractionEvent> = emptyList(),
    val distractorAnswers:List<DistractorAnswer> = emptyList(),
    val recallAnswers:List<RecallAnswer> = emptyList(),
    val refindingResults:List<RefindingResult> = emptyList(),
    val questionnaireResponses:List<QuestionnaireResponse> = emptyList(),
    val summaryMetrics: SummaryMetrics? = null
)
