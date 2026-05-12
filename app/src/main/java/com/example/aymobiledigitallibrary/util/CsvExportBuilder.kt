package com.example.aymobiledigitallibrary.util
import com.example.aymobiledigitallibrary.model.*
object CsvExportBuilder { fun build(participantId:String, mode:String, result:ExperimentResult):String = buildString {
appendLine("section,key,value");appendLine("session,participantId,$participantId");appendLine("session,browsingMode,$mode");appendLine("session,browsingDurationMs,${result.browsingDurationMs}")
result.interactionEvents.forEach{appendLine("event,${it.eventType},${it.timestamp}")}
result.recallAnswers.forEach{appendLine("recall,${it.taskType}:${it.itemId},${it.selectedAnswer}")}
result.refindingResults.forEach{appendLine("refinding,${it.targetItemId},${it.timeToFindMs}")}
result.questionnaireResponses.forEach{appendLine("questionnaire,${it.taskType}:${it.itemText.take(20)},${it.rating}")}
} }
