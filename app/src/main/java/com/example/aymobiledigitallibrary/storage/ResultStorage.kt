package com.example.aymobiledigitallibrary.storage

import android.content.Context
import com.example.aymobiledigitallibrary.model.*
import org.json.JSONArray
import org.json.JSONObject

data class ResultCounts(
    val distractorCompletedCount: Int,
    val globalRecallCount: Int,
    val localContextRecallCount: Int,
    val refindingCount: Int,
    val questionnaireCount: Int
)

class ResultStorage(context: Context) {
    private val prefs = context.getSharedPreferences("results", Context.MODE_PRIVATE)
    private var distractorResult: DistractorResult? = null
    private val globalRecallResults = mutableListOf<GlobalRecallResult>()
    private val localContextRecallResults = mutableListOf<LocalContextRecallResult>()
    private val refindingResults = mutableListOf<RefindingResult>()
    private val questionnaireResponses = mutableListOf<QuestionnaireResponse>()

    fun saveDistractorResult(result: DistractorResult) { distractorResult = result; persist() }
    fun saveGlobalRecallResults(results: List<GlobalRecallResult>) { globalRecallResults.clear(); globalRecallResults.addAll(results); persist() }
    fun saveLocalContextRecallResults(results: List<LocalContextRecallResult>) { localContextRecallResults.clear(); localContextRecallResults.addAll(results); persist() }
    fun saveRefindingResults(results: List<RefindingResult>) { refindingResults.clear(); refindingResults.addAll(results); persist() }
    fun saveQuestionnaireResponses(results: List<QuestionnaireResponse>) { questionnaireResponses.clear(); questionnaireResponses.addAll(results); persist() }

    fun getCounts() = ResultCounts(distractorResult?.completedCount ?: 0, globalRecallResults.size, localContextRecallResults.size, refindingResults.size, questionnaireResponses.size)

    fun buildExperimentResult(participantId: String, participantInfo: ParticipantInfo?, browsingMode: BrowsingMode?): ExperimentResult {
        val summary = SummaryMetrics(
            globalLocationAccuracy = globalRecallResults.map { it.accuracy }.average().takeIf { !it.isNaN() } ?: 0.0,
            meanGlobalAbsoluteError = globalRecallResults.map { it.absoluteError.toDouble() }.average().takeIf { !it.isNaN() } ?: 0.0,
            localContextAccuracy = localContextRecallResults.map { it.accuracy }.average().takeIf { !it.isNaN() } ?: 0.0,
            refindingSuccessRate = refindingResults.map { if (it.success) 1.0 else 0.0 }.average().takeIf { !it.isNaN() } ?: 0.0,
            meanRefindingTimeMillis = refindingResults.map { it.timeToFindMillis.toDouble() }.average().takeIf { !it.isNaN() } ?: 0.0,
            totalWrongClicks = refindingResults.sumOf { it.wrongClickCount },
            totalNextClicks = refindingResults.sumOf { it.nextClickCount },
            totalPreviousClicks = refindingResults.sumOf { it.previousClickCount },
            meanExperienceScore = questionnaireResponses.map { it.rating.toDouble() }.average().takeIf { !it.isNaN() } ?: 0.0
        )
        return ExperimentResult(participantId, participantInfo, browsingMode, distractorResult = distractorResult, globalRecallResults = globalRecallResults.toList(), localContextRecallResults = localContextRecallResults.toList(), refindingResults = refindingResults.toList(), questionnaireResponses = questionnaireResponses.toList(), summaryMetrics = summary)
    }

    private fun persist() {
        val root = JSONObject()
        root.put("distractor", distractorResult?.let { JSONObject().put("completedCount", it.completedCount).put("correctCount", it.correctCount) })
        root.put("globalRecall", JSONArray(globalRecallResults.map { JSONObject().put("targetItemId", it.targetItemId) }))
        root.put("localContextRecall", JSONArray(localContextRecallResults.map { JSONObject().put("targetItemId", it.targetItemId) }))
        root.put("refinding", JSONArray(refindingResults.map { JSONObject().put("targetItemId", it.targetItemId).put("success", it.success) }))
        root.put("questionnaire", JSONArray(questionnaireResponses.map { JSONObject().put("rating", it.rating) }))
        prefs.edit().putString("phase2_results", root.toString()).apply()
    }

    fun clearAll() { distractorResult = null; globalRecallResults.clear(); localContextRecallResults.clear(); refindingResults.clear(); questionnaireResponses.clear(); prefs.edit().clear().apply() }
}
