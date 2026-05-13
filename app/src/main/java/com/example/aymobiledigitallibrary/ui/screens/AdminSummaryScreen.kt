package com.example.aymobiledigitallibrary.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.ParticipantInfo
import com.example.aymobiledigitallibrary.storage.ResultStorage
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.util.CsvExportBuilder
import com.example.aymobiledigitallibrary.util.JsonExportBuilder

@Composable
fun AdminSummaryScreen(sessionId: String, mode: BrowsingMode, resultStorage: ResultStorage, participantInfo: ParticipantInfo?, onReset: () -> Unit = {}) {
    val ctx = LocalContext.current
    val counts = resultStorage.getCounts(); val result = resultStorage.buildExperimentResult(sessionId, participantInfo, mode); val summary = result.summaryMetrics
    var pending = ""
    val csvLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri -> uri?.let { resultStorage.logEvent(sessionId, mode, "admin", "export_csv_clicked"); runCatching { ctx.contentResolver.openOutputStream(it)?.use { os -> os.write(CsvExportBuilder.build(result).toByteArray()) } }.onSuccess { Toast.makeText(ctx, "CSV exported successfully", Toast.LENGTH_SHORT).show() }.onFailure { Toast.makeText(ctx, "CSV export failed", Toast.LENGTH_SHORT).show() } } }
    val jsonLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri -> uri?.let { resultStorage.logEvent(sessionId, mode, "admin", "export_json_clicked"); runCatching { ctx.contentResolver.openOutputStream(it)?.use { os -> os.write(JsonExportBuilder.build(result).toByteArray()) } }.onSuccess { Toast.makeText(ctx, "JSON exported successfully", Toast.LENGTH_SHORT).show() }.onFailure { Toast.makeText(ctx, "JSON export failed", Toast.LENGTH_SHORT).show() } } }
    ScreenContainer(scrollable = true) {
        Text("Admin Summary", style = MaterialTheme.typography.headlineMedium)
        Text("Participant ID: $sessionId"); Text("Browsing mode: ${mode.name}")
        participantInfo?.let { Text("Age: ${it.age}"); Text("Gender: ${it.gender}"); Text("Scrolling familiarity: ${it.scrollingFamiliarity}"); Text("Pagination familiarity: ${it.paginationFamiliarity}"); Text("Reading app frequency: ${it.readingAppFrequency}"); Text("Digital library frequency: ${it.digitalLibraryFrequency}"); Text("Spatial ability: ${it.spatialAbility}") }
        Text("Global recall completed count: ${counts.globalRecallCount}"); Text("Local context completed count: ${counts.localContextRecallCount}"); Text("Re-finding completed count: ${counts.refindingCount}"); Text("Questionnaire completed count: ${counts.questionnaireCount}"); Text("Log count: ${counts.logCount}")
        summary?.let { Text("Global location accuracy: ${"%.2f".format(it.globalLocationAccuracy)}"); Text("Mean global absolute error: ${"%.2f".format(it.meanGlobalAbsoluteError)}"); Text("Local context accuracy: ${"%.2f".format(it.localContextAccuracy)}"); Text("Re-finding success rate: ${"%.2f".format(it.refindingSuccessRate)}"); Text("Mean re-finding time: ${"%.0f".format(it.meanRefindingTimeMillis)}") }
        PrimaryButton("Export CSV") { csvLauncher.launch("mobile_library_result_${sessionId}.csv") }
        PrimaryButton("Export JSON") { jsonLauncher.launch("mobile_library_result_${sessionId}.json") }
        PrimaryButton("Reset Data", onClick = onReset)
    }
}
