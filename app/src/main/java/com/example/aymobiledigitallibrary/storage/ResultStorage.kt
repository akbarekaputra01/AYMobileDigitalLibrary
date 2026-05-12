package com.example.aymobiledigitallibrary.storage

import android.content.Context
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.util.JsonExportBuilder
import org.json.JSONObject

class ResultStorage(context: Context) {
    private val p = context.getSharedPreferences("results", Context.MODE_PRIVATE)
    private val events = mutableListOf<InteractionEvent>()
    private val distractors = mutableListOf<DistractorAnswer>()
    private val recalls = mutableListOf<RecallAnswer>()
    private val refinding = mutableListOf<RefindingResult>()
    private val questionnaires = mutableListOf<QuestionnaireResponse>()
    var sessionStartTime: Long = System.currentTimeMillis()
    var browsingStartTime: Long = 0L
    var browsingEndTime: Long = 0L
    var browsingDurationMs: Long = 0L

    fun logEvent(e: InteractionEvent) { events += e }
    fun addDistractor(a: DistractorAnswer) { distractors += a }
    fun addRecall(a: RecallAnswer) { recalls += a }
    fun addRefinding(r: RefindingResult) { refinding += r }
    fun addQuestionnaire(q: QuestionnaireResponse) { questionnaires += q }

    fun getResult(participantId: String = "", participantInfo: ParticipantInfo? = null, mode: BrowsingMode? = null, metrics: SummaryMetrics? = null) =
        ExperimentResult(participantId, participantInfo, mode, sessionStartTime, browsingStartTime, browsingEndTime, browsingDurationMs, events.toList(), distractors.toList(), recalls.toList(), refinding.toList(), questionnaires.toList(), metrics)

    fun persistSnapshot(participantId: String, participantInfo: ParticipantInfo?, mode: BrowsingMode?, metrics: SummaryMetrics? = null) {
        val json = JsonExportBuilder.build(getResult(participantId, participantInfo, mode, metrics))
        p.edit().putString("snapshot_json", json).apply()
    }

    fun getSnapshotJson(): String? = p.getString("snapshot_json", null)

    fun getSnapshotSummary(): JSONObject? = getSnapshotJson()?.let { JSONObject(it) }

    fun clearAll() {
        events.clear(); distractors.clear(); recalls.clear(); refinding.clear(); questionnaires.clear()
        browsingDurationMs = 0L; browsingStartTime = 0L; browsingEndTime = 0L; sessionStartTime = System.currentTimeMillis()
        p.edit().clear().apply()
    }
}
