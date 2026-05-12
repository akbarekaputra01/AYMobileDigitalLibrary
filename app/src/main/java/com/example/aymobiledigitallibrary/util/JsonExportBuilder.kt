package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.model.*
import org.json.JSONArray
import org.json.JSONObject

object JsonExportBuilder {
    fun build(result: ExperimentResult): String {
        val root = JSONObject()
        root.put("participantId", result.participantId)
        root.put("browsingMode", result.browsingMode?.name)
        root.put("sessionStartTime", result.sessionStartTime)
        root.put("browsingStartTime", result.browsingStartTime)
        root.put("browsingEndTime", result.browsingEndTime)
        root.put("browsingDurationMs", result.browsingDurationMs)
        root.put("participantInfo", JSONObject().apply {
            result.participantInfo?.let {
                put("age", it.age); put("gender", it.gender); put("scrollingFamiliarity", it.scrollingFamiliarity)
                put("paginationFamiliarity", it.paginationFamiliarity); put("readingAppFrequency", it.readingAppFrequency)
                put("digitalLibraryFrequency", it.digitalLibraryFrequency); put("spatialAbility", it.spatialAbility)
            }
        })
        root.put("interactionEvents", JSONArray(result.interactionEvents.map { JSONObject().put("eventType", it.eventType).put("phase", it.phase.name).put("itemId", it.itemId).put("value", it.value).put("timestamp", it.timestamp) }))
        root.put("distractorAnswers", JSONArray(result.distractorAnswers.map { JSONObject().put("questionIndex", it.questionIndex).put("number", it.number).put("participantAnswer", it.participantAnswer).put("responseTimeMs", it.responseTimeMs) }))
        root.put("recallAnswers", JSONArray(result.recallAnswers.map { JSONObject().put("taskType", it.taskType.name).put("itemId", it.itemId).put("selectedAnswer", it.selectedAnswer).put("isCorrect", it.isCorrect).put("absoluteError", it.absoluteError).put("responseTimeMs", it.responseTimeMs) }))
        root.put("refindingResults", JSONArray(result.refindingResults.map { JSONObject().put("targetItemId", it.targetItemId).put("success", it.success).put("timeToFindMs", it.timeToFindMs).put("wrongClickCount", it.wrongClickCount).put("scrollCount", it.scrollCount).put("pageClickCount", it.pageClickCount).put("finalPage", it.finalPage) }))
        root.put("questionnaireResponses", JSONArray(result.questionnaireResponses.map { JSONObject().put("taskType", it.taskType.name).put("itemText", it.itemText).put("rating", it.rating) }))
        result.summaryMetrics?.let { root.put("summaryMetrics", JSONObject().put("globalLocationAccuracy", it.globalLocationAccuracy).put("meanGlobalAbsoluteError", it.meanGlobalAbsoluteError).put("localRegionAccuracy", it.localRegionAccuracy).put("relativeOrderAccuracy", it.relativeOrderAccuracy).put("refindingSuccessRate", it.refindingSuccessRate).put("meanRefindingTime", it.meanRefindingTime).put("totalWrongClicks", it.totalWrongClicks).put("totalScrollCount", it.totalScrollCount).put("totalPageClicks", it.totalPageClicks).put("meanWorkloadScore", it.meanWorkloadScore).put("meanUsabilityScore", it.meanUsabilityScore)) }
        return root.toString(2)
    }
}
