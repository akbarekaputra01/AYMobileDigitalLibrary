package com.example.aymobiledigitallibrary.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.*

@Composable
fun AdminSummaryScreen(sessionId:String,mode:BrowsingMode,result:ExperimentResult,onReset:()->Unit = {}) {
    val metrics = SummaryMetrics(
        globalLocationAccuracy = ScoringUtils.accuracy(result.recallAnswers, TaskType.GLOBAL_LOCATION),
        meanGlobalAbsoluteError = ScoringUtils.meanGlobalAbsoluteError(result.recallAnswers),
        localRegionAccuracy = ScoringUtils.accuracy(result.recallAnswers, TaskType.LOCAL_REGION),
        relativeOrderAccuracy = ScoringUtils.accuracy(result.recallAnswers, TaskType.RELATIVE_ORDER),
        refindingSuccessRate = ScoringUtils.refindingSuccessRate(result.refindingResults),
        meanRefindingTime = ScoringUtils.meanRefindingTime(result.refindingResults),
        totalWrongClicks = ScoringUtils.totalWrongClicks(result.refindingResults),
        totalScrollCount = ScoringUtils.totalScrollCount(result.refindingResults),
        totalPageClicks = ScoringUtils.totalPageClicks(result.refindingResults),
        meanWorkloadScore = ScoringUtils.meanQuestionnaire(result.questionnaireResponses, TaskType.WORKLOAD),
        meanUsabilityScore = ScoringUtils.meanQuestionnaire(result.questionnaireResponses, TaskType.USABILITY)
    )
    val normalized = result.copy(summaryMetrics = metrics)
    val csv = CsvExportBuilder.build(normalized)
    val json = JsonExportBuilder.build(normalized)
    val context = LocalContext.current
    var confirm by remember { mutableStateOf(false) }

    if (confirm) AlertDialog(onDismissRequest = { confirm = false }, confirmButton = { TextButton({ confirm=false; onReset() }) { Text("Confirm") } }, dismissButton = { TextButton({ confirm=false }) { Text("Cancel") } }, title={Text("Start new session?")}, text={Text("Start a new session and clear current local results?")})

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle("Research Dashboard","Session diagnostics and technical overview.")
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Participant Configuration"); ResultMetricRow("Session ID", sessionId); ResultMetricRow("Browsing Mode", mode.name); ResultMetricRow("Library Items", LibraryRepository.items.size.toString()); ResultMetricRow("Status", "Completed"); ResultMetricRow("Browsing Duration", "${normalized.browsingDurationMs} ms") } }
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Progress"); LinearProgressIndicator(progress={1f}, modifier=Modifier.fillMaxWidth()); Text("100% Complete") } }
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Session Logs"); Text("${normalized.interactionEvents.size} events recorded"); normalized.interactionEvents.lastOrNull()?.let{Text("Last: ${it.eventType} at ${it.timestamp}")} } }
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Recall Metrics"); ResultMetricRow("Global Accuracy", "%.2f".format(metrics.globalLocationAccuracy)); ResultMetricRow("Mean Global Error", "%.2f".format(metrics.meanGlobalAbsoluteError)); ResultMetricRow("Local Accuracy", "%.2f".format(metrics.localRegionAccuracy)); ResultMetricRow("Relative Order", "%.2f".format(metrics.relativeOrderAccuracy)) } }
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Re-finding Metrics"); ResultMetricRow("Success Rate", "%.2f".format(metrics.refindingSuccessRate)); ResultMetricRow("Mean Time", "%.0f ms".format(metrics.meanRefindingTime)); ResultMetricRow("Wrong Clicks", metrics.totalWrongClicks.toString()); ResultMetricRow("Scroll Count", metrics.totalScrollCount.toString()); ResultMetricRow("Page Clicks", metrics.totalPageClicks.toString()) } }
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Questionnaire Metrics"); ResultMetricRow("Mean Workload", "%.2f".format(metrics.meanWorkloadScore)); ResultMetricRow("Mean Usability", "%.2f".format(metrics.meanUsabilityScore)) } }
        Text("CSV Preview (${if (ExportValidation.isValidCsv(csv)) "valid" else "check"})"); SelectionContainer { Text(csv) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SecondaryButton("Copy CSV", { copy(context, "csv", csv) }); SecondaryButton("Share CSV", { share(context, "text/csv", csv) }) }
        Text("JSON Preview (${if (ExportValidation.isValidJson(json)) "valid" else "invalid"})"); SelectionContainer { Text(json) }
        OutlinedCard { Column(Modifier.padding(14.dp)) { Text("Recent Event Stream"); normalized.interactionEvents.takeLast(8).forEach{ Text("${it.timestamp} • ${it.eventType} • ${it.itemId ?: it.value ?: ""}") } } }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SecondaryButton("Copy JSON", { copy(context, "json", json) }); SecondaryButton("Share JSON", { share(context, "application/json", json) }) }
        PrimaryButton("Start New Session", { confirm = true })
    }
}
private fun copy(context: Context, label: String, value: String){ (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(label, value)) }
private fun share(context: Context, mime: String, value: String){ context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = mime; putExtra(Intent.EXTRA_TEXT, value) }, "Share")) }
