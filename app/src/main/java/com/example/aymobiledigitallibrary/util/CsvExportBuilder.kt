package com.example.aymobiledigitallibrary.util
import com.example.aymobiledigitallibrary.model.ExperimentResult
object CsvExportBuilder { fun build(r: ExperimentResult): String = buildString {
appendLine("SECTION,KEY,VALUE"); appendLine("SESSION_INFO,participantId,${r.participantId}"); appendLine("SESSION_INFO,browsingMode,${r.browsingMode}"); appendLine("SESSION_INFO,browsingDurationMs,${r.browsingDurationMs}")
appendLine("PARTICIPANT_INFO,age,${r.participantInfo?.age ?: ""}")
r.interactionEvents.forEach{appendLine("INTERACTION_EVENTS,${it.eventType},${it.timestamp}")}
r.distractorAnswers.forEach{appendLine("DISTRACTOR_RESULTS,Q${it.questionIndex},${it.participantAnswer}")}
r.recallAnswers.forEach{appendLine("RECALL_RESULTS,${it.taskType}:${it.itemId},${it.selectedAnswer}")}
r.refindingResults.forEach{appendLine("REFINDING_RESULTS,${it.targetItemId},${it.timeToFindMs}")}
r.questionnaireResponses.forEach{appendLine("QUESTIONNAIRE_RESPONSES,${it.taskType}:${it.itemText.take(20)},${it.rating}")}
r.summaryMetrics?.let{appendLine("SUMMARY_METRICS,globalLocationAccuracy,${it.globalLocationAccuracy}")}
} }
