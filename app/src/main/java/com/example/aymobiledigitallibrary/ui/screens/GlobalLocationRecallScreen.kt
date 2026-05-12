package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ScoringUtils

@Composable
fun GlobalLocationRecallScreen(participantId: String, mode: BrowsingMode, onDone: (List<GlobalRecallResult>) -> Unit) {
    val targets = remember { listOf(0, 3, 7, 11, 15, 19, 23, 28).map { LibraryRepository.items[it] } }
    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val out = remember { mutableStateListOf<GlobalRecallResult>() }
    val options = if (mode == BrowsingMode.PAGE_VIEW) listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5") else listOf("Early List", "Upper-Middle List", "Middle List", "Lower-Middle List", "Late List")
    val item = targets[idx]
    ScreenContainer { TaskProgressHeader("Where Did You See This?", idx + 1, targets.size); CompactLibraryItemCard(item) {}; Text("Where did this material appear in the library?");
        options.forEachIndexed { i, s -> SelectableCard(s, selected == i + 1, helperText = null) { selected = i + 1 } }
        PrimaryButton("Continue", enabled = selected != -1) {
            val correct = if (mode == BrowsingMode.PAGE_VIEW) item.paginationPage else item.scrollZoneIndex
            out += GlobalRecallResult(participantId, mode, item.id, correct, selected, ScoringUtils.calculateAccuracy(correct, selected), ScoringUtils.calculateAbsoluteError(correct, selected), System.currentTimeMillis() - start)
            if (idx == targets.lastIndex) onDone(out) else { idx++; selected = -1; start = System.currentTimeMillis() }
        }
    }
}
