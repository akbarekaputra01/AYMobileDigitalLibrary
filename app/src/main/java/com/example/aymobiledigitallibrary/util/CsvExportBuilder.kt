package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.model.ExperimentResult

object CsvExportBuilder {
    fun build(r: ExperimentResult): String = buildString {
        appendLine("SECTION,KEY,VALUE")
        appendLine("SESSION_INFO,participantId,${r.participantId}")
        appendLine("SESSION_INFO,browsingMode,${r.browsingMode}")
        appendLine("PARTICIPANT_INFO,age,${r.participantInfo?.age ?: ""}")
        appendLine("PARTICIPANT_INFO,gender,${r.participantInfo?.gender ?: ""}")
        appendLine("PARTICIPANT_INFO,scrollingFamiliarity,${r.participantInfo?.scrollingFamiliarity ?: ""}")
        appendLine("PARTICIPANT_INFO,paginationFamiliarity,${r.participantInfo?.paginationFamiliarity ?: ""}")
        appendLine("PARTICIPANT_INFO,readingAppFrequency,${r.participantInfo?.readingAppFrequency ?: ""}")
        appendLine("PARTICIPANT_INFO,digitalLibraryFrequency,${r.participantInfo?.digitalLibraryFrequency ?: ""}")
        appendLine("PARTICIPANT_INFO,spatialAbility,${r.participantInfo?.spatialAbility ?: ""}")
        r.globalRecallResults.forEach { appendLine("GLOBAL_RECALL,${it.targetItemId},${it.correctIndex}|${it.selectedIndex}|${it.accuracy}|${it.absoluteError}|${it.responseTimeMillis}") }
        r.localContextRecallResults.forEach { appendLine("LOCAL_CONTEXT_RECALL,${it.targetItemId},${it.correctNeighborItemIdsCsv}|${it.selectedNeighborItemIdsCsv}|${it.accuracy}|${it.responseTimeMillis}") }
        r.refindingResults.forEach { appendLine("REFINDING_RESULTS,${it.targetItemId},${it.selectedItemId}|${it.success}|${it.startTimeMillis}|${it.endTimeMillis}|${it.timeToFindMillis}|${it.wrongClickCount}|${it.nextClickCount}|${it.previousClickCount}|${it.finalPage}|${it.finalScrollZone}") }
        r.questionnaireResponses.forEach { appendLine("QUESTIONNAIRE,${it.itemText.take(24)},${it.rating}") }
        r.interactionEvents.forEach { appendLine("INTERACTION_LOG,${it.eventType},${it.phase}|${it.timestampMillis}|${it.itemId ?: ""}|${it.value ?: ""}") }
    }
}
