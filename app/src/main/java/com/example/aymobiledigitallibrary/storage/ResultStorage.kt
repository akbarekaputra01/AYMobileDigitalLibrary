package com.example.aymobiledigitallibrary.storage

import android.content.Context
import com.example.aymobiledigitallibrary.model.*

data class ResultCounts(val distractorCompletedCount: Int, val globalRecallCount: Int, val localContextRecallCount: Int, val refindingCount: Int, val questionnaireCount: Int, val logCount:Int)

class ResultStorage(context: Context) {
    private val prefs = context.getSharedPreferences("results", Context.MODE_PRIVATE)
    private var distractorResult: DistractorResult? = null
    private val globalRecallResults = mutableListOf<GlobalRecallResult>()
    private val localContextRecallResults = mutableListOf<LocalContextRecallResult>()
    private val refindingResults = mutableListOf<RefindingResult>()
    private val questionnaireResponses = mutableListOf<QuestionnaireResponse>()
    private val interactionEvents = mutableListOf<InteractionEvent>()
    fun logEvent(participantId:String,browsingMode: BrowsingMode?,phase:String,eventType:String,itemId:String?=null,value:String?=null){ interactionEvents += InteractionEvent(participantId,browsingMode,phase,eventType,itemId,value) }
    fun saveDistractorResult(result: DistractorResult) { distractorResult = result }
    fun saveGlobalRecallResults(results: List<GlobalRecallResult>) { globalRecallResults.clear(); globalRecallResults.addAll(results) }
    fun saveLocalContextRecallResults(results: List<LocalContextRecallResult>) { localContextRecallResults.clear(); localContextRecallResults.addAll(results) }
    fun saveRefindingResults(results: List<RefindingResult>) { refindingResults.clear(); refindingResults.addAll(results) }
    fun saveQuestionnaireResponses(results: List<QuestionnaireResponse>) { questionnaireResponses.clear(); questionnaireResponses.addAll(results) }
    fun getCounts() = ResultCounts(distractorResult?.completedCount ?: 0, globalRecallResults.size, localContextRecallResults.size, refindingResults.size, questionnaireResponses.size, interactionEvents.size)
    fun buildExperimentResult(participantId: String, participantInfo: ParticipantInfo?, browsingMode: BrowsingMode?): ExperimentResult {
        val summary = SummaryMetrics(globalRecallResults.map { it.accuracy }.average().takeIf { !it.isNaN() } ?: 0.0,globalRecallResults.map { it.absoluteError.toDouble() }.average().takeIf { !it.isNaN() } ?: 0.0,localContextRecallResults.map { it.accuracy }.average().takeIf { !it.isNaN() } ?: 0.0,refindingResults.map { if (it.success) 1.0 else 0.0 }.average().takeIf { !it.isNaN() } ?: 0.0,refindingResults.map { it.timeToFindMillis.toDouble() }.average().takeIf { !it.isNaN() } ?: 0.0,refindingResults.sumOf { it.wrongClickCount },refindingResults.sumOf { it.nextClickCount },refindingResults.sumOf { it.previousClickCount },questionnaireResponses.map { it.rating.toDouble() }.average().takeIf { !it.isNaN() } ?: 0.0)
        return ExperimentResult(participantId, participantInfo, browsingMode, distractorResult = distractorResult, globalRecallResults = globalRecallResults.toList(), localContextRecallResults = localContextRecallResults.toList(), refindingResults = refindingResults.toList(), questionnaireResponses = questionnaireResponses.toList(), interactionEvents=interactionEvents.toList(), summaryMetrics = summary)
    }
    fun clearAll() { distractorResult = null; globalRecallResults.clear(); localContextRecallResults.clear(); refindingResults.clear(); questionnaireResponses.clear();interactionEvents.clear(); prefs.edit().clear().apply() }
}
