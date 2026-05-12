package com.example.aymobiledigitallibrary.storage

import android.content.Context
import com.example.aymobiledigitallibrary.model.*
import org.json.JSONArray
import org.json.JSONObject

data class ResultCounts(
    val distractorCompletedCount: Int,
    val globalRecallCount: Int,
    val localContextRecallCount: Int,
    val refindingCount: Int
)

class ResultStorage(context: Context) {
    private val prefs = context.getSharedPreferences("results", Context.MODE_PRIVATE)
    private var distractorResult: DistractorResult? = null
    private val globalRecallResults = mutableListOf<GlobalRecallResult>()
    private val localContextRecallResults = mutableListOf<LocalContextRecallResult>()
    private val refindingResults = mutableListOf<RefindingResult>()

    fun saveDistractorResult(result: DistractorResult) { distractorResult = result; persist() }
    fun saveGlobalRecallResults(results: List<GlobalRecallResult>) { globalRecallResults.clear(); globalRecallResults.addAll(results); persist() }
    fun saveLocalContextRecallResults(results: List<LocalContextRecallResult>) { localContextRecallResults.clear(); localContextRecallResults.addAll(results); persist() }
    fun saveRefindingResults(results: List<RefindingResult>) { refindingResults.clear(); refindingResults.addAll(results); persist() }

    fun getCounts() = ResultCounts(distractorResult?.completedCount ?: 0, globalRecallResults.size, localContextRecallResults.size, refindingResults.size)

    private fun persist() {
        val root = JSONObject()
        root.put("distractor", distractorResult?.let { JSONObject().put("completedCount", it.completedCount).put("correctCount", it.correctCount) })
        root.put("globalRecall", JSONArray(globalRecallResults.map { JSONObject().put("targetItemId", it.targetItemId) }))
        root.put("localContextRecall", JSONArray(localContextRecallResults.map { JSONObject().put("targetItemId", it.targetItemId) }))
        root.put("refinding", JSONArray(refindingResults.map { JSONObject().put("targetItemId", it.targetItemId).put("success", it.success) }))
        prefs.edit().putString("phase2_results", root.toString()).apply()
    }

    fun clearAll() { distractorResult = null; globalRecallResults.clear(); localContextRecallResults.clear(); refindingResults.clear(); prefs.edit().clear().apply() }
}
