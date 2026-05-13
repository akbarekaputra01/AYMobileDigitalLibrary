package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.ParticipantInfo
import com.example.aymobiledigitallibrary.storage.ResultStorage
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.util.CsvExportBuilder
import com.example.aymobiledigitallibrary.util.JsonExportBuilder

@Composable
fun AdminSummaryScreen(sessionId: String, mode: BrowsingMode, resultStorage: ResultStorage, participantInfo: ParticipantInfo?, onReset: () -> Unit = {}) {
    val counts = resultStorage.getCounts()
    val result = resultStorage.buildExperimentResult(sessionId, participantInfo, mode)
    val summary = result.summaryMetrics

    ScreenContainer(scrollable = true) {
        Text("Admin Summary", style = MaterialTheme.typography.headlineMedium)
        Text("Participant ID: $sessionId")
        Text("Browsing Mode: ${mode.name}")
        Text("Global recall completed count: ${counts.globalRecallCount}")
        Text("Local context recall completed count: ${counts.localContextRecallCount}")
        Text("Re-finding completed count: ${counts.refindingCount}")
        Text("Questionnaire completed count: ${counts.questionnaireCount}")
        summary?.let {
            Text("Global location accuracy: ${"%.2f".format(it.globalLocationAccuracy)}")
            Text("Mean global absolute error: ${"%.2f".format(it.meanGlobalAbsoluteError)}")
            Text("Local context accuracy: ${"%.2f".format(it.localContextAccuracy)}")
            Text("Re-finding success rate: ${"%.2f".format(it.refindingSuccessRate)}")
            Text("Mean re-finding time (ms): ${"%.0f".format(it.meanRefindingTimeMillis)}")
        }
        PrimaryButton("Export CSV") { CsvExportBuilder.build(result) }
        PrimaryButton("Export JSON") { JsonExportBuilder.build(result) }
        PrimaryButton("Reset Data", onReset)
    }
}
