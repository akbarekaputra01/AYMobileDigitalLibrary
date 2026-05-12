package com.example.aymobiledigitallibrary.storage

import android.content.Context
import com.example.aymobiledigitallibrary.model.*

class ResultStorage(context: Context) {
    private val p = context.getSharedPreferences("results", Context.MODE_PRIVATE)
    private val events = mutableListOf<InteractionEvent>()
    private val distractors = mutableListOf<DistractorAnswer>()
    private val recalls = mutableListOf<RecallAnswer>()
    private val refinding = mutableListOf<RefindingResult>()
    private val questionnaires = mutableListOf<QuestionnaireResponse>()
    var browsingDurationMs: Long = 0L

    fun logEvent(e: InteractionEvent) { events += e }
    fun addDistractor(a: DistractorAnswer) { distractors += a }
    fun addRecall(a: RecallAnswer) { recalls += a }
    fun addRefinding(r: RefindingResult) { refinding += r }
    fun addQuestionnaire(q: QuestionnaireResponse) { questionnaires += q }
    fun getResult() = ExperimentResult(browsingDurationMs, events.toList(), distractors.toList(), recalls.toList(), refinding.toList(), questionnaires.toList())
    fun persistSnapshot() { p.edit().putString("snapshot", getResult().toString()).apply() }
}
