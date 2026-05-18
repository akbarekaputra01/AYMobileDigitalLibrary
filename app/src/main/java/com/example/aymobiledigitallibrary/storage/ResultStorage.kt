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
    private fun Iterable<Double>.stdDev(mean: Double): Double {
        val size = this.count()
        if (size <= 1) return 0.0
        val variance = this.sumOf { (it - mean) * (it - mean) } / (size - 1)
        return kotlin.math.sqrt(variance)
    }

    fun buildExperimentResult(participantId: String, participantInfo: ParticipantInfo?, browsingMode: BrowsingMode?): ExperimentResult {
        val globalAccuracies = globalRecallResults.map { it.accuracy.toDouble() }
        val globalErrors = globalRecallResults.map { it.absoluteError.toDouble() }
        val localAccuracies = localContextRecallResults.map { it.accuracy.toDouble() }
        val refindingTimes = refindingResults.map { it.timeToFindMillis.toDouble() }
        val experienceScores = questionnaireResponses.map { it.rating.toDouble() }

        val meanGlobalAcc = globalAccuracies.average().takeIf { !it.isNaN() } ?: 0.0
        val meanGlobalErr = globalErrors.average().takeIf { !it.isNaN() } ?: 0.0
        val meanLocalAcc = localAccuracies.average().takeIf { !it.isNaN() } ?: 0.0
        val meanRefindTime = refindingTimes.average().takeIf { !it.isNaN() } ?: 0.0
        val meanExpScore = experienceScores.average().takeIf { !it.isNaN() } ?: 0.0

        val summary = SummaryMetrics(
            globalLocationAccuracy = meanGlobalAcc,
            globalLocationAccuracySd = globalAccuracies.stdDev(meanGlobalAcc),
            meanGlobalAbsoluteError = meanGlobalErr,
            globalAbsoluteErrorSd = globalErrors.stdDev(meanGlobalErr),
            localContextAccuracy = meanLocalAcc,
            localContextAccuracySd = localAccuracies.stdDev(meanLocalAcc),
            refindingSuccessRate = refindingResults.map { if (it.success) 1.0 else 0.0 }.average().takeIf { !it.isNaN() } ?: 0.0,
            meanRefindingTimeMillis = meanRefindTime,
            refindingTimeMillisSd = refindingTimes.stdDev(meanRefindTime),
            totalWrongClicks = refindingResults.sumOf { it.wrongClickCount },
            totalNextClicks = refindingResults.sumOf { it.nextClickCount },
            totalPreviousClicks = refindingResults.sumOf { it.previousClickCount },
            meanExperienceScore = meanExpScore,
            experienceScoreSd = experienceScores.stdDev(meanExpScore)
        )
        return ExperimentResult(participantId, participantInfo, browsingMode, distractorResult = distractorResult, globalRecallResults = globalRecallResults.toList(), localContextRecallResults = localContextRecallResults.toList(), refindingResults = refindingResults.toList(), questionnaireResponses = questionnaireResponses.toList(), interactionEvents=interactionEvents.toList(), summaryMetrics = summary)
    }
    fun clearAll() { distractorResult = null; globalRecallResults.clear(); localContextRecallResults.clear(); refindingResults.clear(); questionnaireResponses.clear();interactionEvents.clear(); prefs.edit().clear().apply() }
}
