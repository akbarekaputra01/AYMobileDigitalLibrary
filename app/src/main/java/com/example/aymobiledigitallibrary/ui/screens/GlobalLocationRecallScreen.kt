package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.GlobalRecallResult
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ScoringUtils

@Composable
fun GlobalLocationRecallScreen(participantId: String, mode: BrowsingMode, onDone: (List<GlobalRecallResult>) -> Unit) {
    val targets = remember { listOf(2, 3, 8, 9, 14, 15, 20, 26).map { LibraryRepository.items[it] } }
    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<GlobalRecallResult>() }
    val options = if (mode == BrowsingMode.PAGE_VIEW) (1..5).map { "Page $it" } else (1..5).map { "Group $it" }
    val helper = listOf("Materials 1–6", "Materials 7–12", "Materials 13–18", "Materials 19–24", "Materials 25–30")
    val item = targets[idx]
    ScreenContainer(scrollable = true) {
        TaskProgressHeader(title = "Global Location Recall", step = idx + 1, total = targets.size)
        CompactLibraryItemCard(item = item) {}
        Text("Where did this material appear in the library?")
        options.forEachIndexed { i, t -> SelectableCard(t, helper[i], selected == i + 1) { selected = i + 1 } }
        PrimaryButton("Continue", selected != -1) {
            val correct = if (mode == BrowsingMode.PAGE_VIEW) item.paginationPage else item.scrollZoneIndex
            val end = System.currentTimeMillis()
            results += GlobalRecallResult(participantId, mode, item.id, correct, selected, ScoringUtils.calculateAccuracy(correct, selected), ScoringUtils.calculateAbsoluteError(correct, selected), end - start)
            if (idx == targets.lastIndex) onDone(results) else { idx++; selected = -1; start = System.currentTimeMillis() }
        }
    }
}
