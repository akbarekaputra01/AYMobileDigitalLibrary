package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.model.ExperimentResult

object CsvExportBuilder {
    fun build(r: ExperimentResult): String = buildString {
        appendLine("SECTION,KEY,VALUE")
        appendLine("SESSION_INFO,participantId,${r.participantId}")
        appendLine("SESSION_INFO,browsingMode,${r.browsingMode}")
        appendLine("PARTICIPANT_INFO,age,${r.participantInfo?.age ?: ""}")
        appendLine("PARTICIPANT_INFO,gender,${r.participantInfo?.gender ?: ""}")
        r.distractorResult?.let { appendLine("DISTRACTOR_RESULT,completedCount,${it.completedCount}") }
        r.globalRecallResults.forEach { appendLine("GLOBAL_RECALL,${it.targetItemId},${it.selectedIndex}|${it.correctIndex}|${it.accuracy}|${it.absoluteError}") }
        r.localContextRecallResults.forEach { appendLine("LOCAL_CONTEXT_RECALL,${it.targetItemId},${it.selectedPlacement}|${it.correctPlacement}|${it.accuracy}") }
        r.refindingResults.forEach { appendLine("REFINDING_RESULTS,${it.targetItemId},${it.timeToFindMillis}|${it.wrongClickCount}|${it.nextClickCount}|${it.previousClickCount}|${it.finalPage}|${it.finalScrollZone}") }
        r.questionnaireResponses.forEach { appendLine("QUESTIONNAIRE,${it.itemText.take(24)},${it.rating}") }
        r.summaryMetrics?.let {
            appendLine("SUMMARY_METRICS,globalLocationAccuracy,${it.globalLocationAccuracy}")
            appendLine("SUMMARY_METRICS,meanGlobalAbsoluteError,${it.meanGlobalAbsoluteError}")
            appendLine("SUMMARY_METRICS,localContextAccuracy,${it.localContextAccuracy}")
            appendLine("SUMMARY_METRICS,refindingSuccessRate,${it.refindingSuccessRate}")
            appendLine("SUMMARY_METRICS,meanRefindingTimeMillis,${it.meanRefindingTimeMillis}")
        }
    }
}
