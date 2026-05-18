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

import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSummaryScreen(
    sessionId: String,
    mode: BrowsingMode,
    resultStorage: ResultStorage,
    participantInfo: ParticipantInfo?,
    onReset: () -> Unit = {}
) {
    val context = LocalContext.current
    val counts = resultStorage.getCounts()
    val result = resultStorage.buildExperimentResult(sessionId, participantInfo, mode)
    val summary = result.summaryMetrics

    var showSheetFor by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()

    fun saveFileLocally(content: String, filename: String): File? {
        return runCatching {
            val folder = context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS)
            folder?.mkdirs()
            val file = File(folder, filename)
            file.writeText(content)
            file
        }.onFailure {
            it.printStackTrace()
            Toast.makeText(context, "Failed to save: ${it.message}", Toast.LENGTH_SHORT).show()
        }.getOrNull()
    }

    fun shareFile(content: String, filename: String, mimeType: String) {
        val file = saveFileLocally(content, filename) ?: return
        runCatching {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share $filename"))
            Toast.makeText(context, "Opening share menu...", Toast.LENGTH_SHORT).show()
        }.onFailure {
            it.printStackTrace()
            Toast.makeText(context, "Failed to share: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    ScreenContainer(scrollable = true) {
        Text(
            text = "Admin Summary",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Participant ID: $sessionId")
        Text("Browsing mode: ${mode.name}")

        participantInfo?.let {
            Text("Age: ${it.age}")
            Text("Gender: ${it.gender}")
            Text("Scrolling familiarity: ${it.scrollingFamiliarity}")
            Text("Pagination familiarity: ${it.paginationFamiliarity}")
            Text("Reading app frequency: ${it.readingAppFrequency}")
            Text("Digital library frequency: ${it.digitalLibraryFrequency}")
            Text("Spatial ability: ${it.spatialAbility}")
        }

        Text("Global recall completed count: ${counts.globalRecallCount}")
        Text("Local context completed count: ${counts.localContextRecallCount}")
        Text("Re-finding completed count: ${counts.refindingCount}")
        Text("Questionnaire completed count: ${counts.questionnaireCount}")
        Text("Log count: ${counts.logCount}")

        summary?.let {
            Text("Global location accuracy: ${"%.2f".format(it.globalLocationAccuracy)}")
            Text("Mean global absolute error: ${"%.2f".format(it.meanGlobalAbsoluteError)}")
            Text("Local context accuracy: ${"%.2f".format(it.localContextAccuracy)}")
            Text("Re-finding success rate: ${"%.2f".format(it.refindingSuccessRate)}")
            Text("Mean re-finding time: ${"%.0f".format(it.meanRefindingTimeMillis)}")
        }

        PrimaryButton(
            text = "Export CSV",
            onClick = {
                resultStorage.logEvent(sessionId, mode, "admin", "open_export_csv_sheet")
                showSheetFor = "csv"
            }
        )

        PrimaryButton(
            text = "Export JSON",
            onClick = {
                resultStorage.logEvent(sessionId, mode, "admin", "open_export_json_sheet")
                showSheetFor = "json"
            }
        )

        PrimaryButton(
            text = "Reset Data",
            onClick = onReset
        )
    }

    if (showSheetFor != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheetFor = null },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Export ${showSheetFor?.uppercase()}",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = {
                        val format = showSheetFor!!
                        if (format == "csv") {
                            resultStorage.logEvent(sessionId, mode, "admin", "share_csv_clicked")
                            shareFile(CsvExportBuilder.build(result), "mobile_library_result_${sessionId}.csv", "text/csv")
                        } else {
                            resultStorage.logEvent(sessionId, mode, "admin", "share_json_clicked")
                            shareFile(JsonExportBuilder.build(result), "mobile_library_result_${sessionId}.json", "application/json")
                        }
                        showSheetFor = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Share to other Apps")
                }

                Button(
                    onClick = {
                        val format = showSheetFor!!
                        if (format == "csv") {
                            resultStorage.logEvent(sessionId, mode, "admin", "download_csv_clicked")
                            val f = saveFileLocally(CsvExportBuilder.build(result), "mobile_library_result_${sessionId}.csv")
                            if (f != null) Toast.makeText(context, "Saved to Documents: ${f.name}", Toast.LENGTH_LONG).show()
                        } else {
                            resultStorage.logEvent(sessionId, mode, "admin", "download_json_clicked")
                            val f = saveFileLocally(JsonExportBuilder.build(result), "mobile_library_result_${sessionId}.json")
                            if (f != null) Toast.makeText(context, "Saved to Documents: ${f.name}", Toast.LENGTH_LONG).show()
                        }
                        showSheetFor = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Download to Documents")
                }
            }
        }
    }
}