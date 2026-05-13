package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.model.ExperimentResult
import org.json.JSONArray
import org.json.JSONObject

object JsonExportBuilder {
    fun build(result: ExperimentResult): String {
        val root = JSONObject()
        root.put("participantId", result.participantId)
        root.put("browsingMode", result.browsingMode?.name)
        root.put("participantInfo", JSONObject().apply { result.participantInfo?.let { put("age", it.age); put("gender", it.gender); put("scrollingFamiliarity", it.scrollingFamiliarity); put("paginationFamiliarity", it.paginationFamiliarity); put("readingAppFrequency", it.readingAppFrequency); put("digitalLibraryFrequency", it.digitalLibraryFrequency); put("spatialAbility", it.spatialAbility) } })
        root.put("globalRecallResults", JSONArray(result.globalRecallResults.map { JSONObject().put("targetItemId", it.targetItemId).put("correctIndex", it.correctIndex).put("selectedIndex", it.selectedIndex).put("accuracy", it.accuracy).put("absoluteError", it.absoluteError).put("responseTimeMillis", it.responseTimeMillis) }))
        root.put("localContextRecallResults", JSONArray(result.localContextRecallResults.map { JSONObject().put("targetItemId", it.targetItemId).put("correctNeighborItemIds", it.correctNeighborItemIdsCsv).put("selectedNeighborItemIds", it.selectedNeighborItemIdsCsv).put("accuracy", it.accuracy).put("responseTimeMillis", it.responseTimeMillis) }))
        root.put("refindingResults", JSONArray(result.refindingResults.map { JSONObject().put("targetItemId", it.targetItemId).put("selectedItemId", it.selectedItemId).put("success", it.success).put("startTimeMillis", it.startTimeMillis).put("endTimeMillis", it.endTimeMillis).put("timeToFindMillis", it.timeToFindMillis).put("wrongClickCount", it.wrongClickCount).put("nextClickCount", it.nextClickCount).put("previousClickCount", it.previousClickCount).put("finalPage", it.finalPage).put("finalScrollZone", it.finalScrollZone) }))
        root.put("questionnaireResponses", JSONArray(result.questionnaireResponses.map { JSONObject().put("itemText", it.itemText).put("rating", it.rating) }))
        root.put("interactionLogs", JSONArray(result.interactionEvents.map { JSONObject().put("eventType", it.eventType).put("phase", it.phase).put("timestampMillis", it.timestampMillis).put("itemId", it.itemId).put("value", it.value) }))
        result.summaryMetrics?.let { root.put("summaryMetrics", JSONObject().put("globalLocationAccuracy", it.globalLocationAccuracy).put("meanGlobalAbsoluteError", it.meanGlobalAbsoluteError).put("localContextAccuracy", it.localContextAccuracy).put("refindingSuccessRate", it.refindingSuccessRate).put("meanRefindingTimeMillis", it.meanRefindingTimeMillis).put("totalWrongClicks", it.totalWrongClicks).put("totalNextClicks", it.totalNextClicks).put("totalPreviousClicks", it.totalPreviousClicks).put("meanExperienceScore", it.meanExperienceScore)) }
        return root.toString(2)
    }
}
