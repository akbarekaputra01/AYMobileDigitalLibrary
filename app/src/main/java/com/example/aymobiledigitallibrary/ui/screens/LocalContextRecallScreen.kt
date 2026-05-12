package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ScoringUtils

@Composable
fun LocalContextRecallScreen(participantId: String, mode: BrowsingMode, onDone: (List<LocalContextRecallResult>) -> Unit) {
    val targets = remember { listOf(0, 3, 7, 11, 15, 19, 23, 28).map { LibraryRepository.items[it] } }
    val placements = listOf(LocalRegion.TOP, LocalRegion.MIDDLE, LocalRegion.BOTTOM)
    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<LocalRegion?>(null) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val out = remember { mutableStateListOf<LocalContextRecallResult>() }
    val item = targets[idx]
    ScreenContainer { TaskProgressHeader("Nearby Placement", idx + 1, targets.size); CompactLibraryItemCard(item) {}; Text("In its nearby group of materials, where was this material placed?"); Text("Choose the closest placement.")
        SelectableCard("Beginning Area", "First two materials in that group", selected == LocalRegion.TOP) { selected = LocalRegion.TOP }
        SelectableCard("Middle Area", "Middle two materials in that group", selected == LocalRegion.MIDDLE) { selected = LocalRegion.MIDDLE }
        SelectableCard("End Area", "Last two materials in that group", selected == LocalRegion.BOTTOM) { selected = LocalRegion.BOTTOM }
        PrimaryButton("Continue", enabled = selected != null) {
            val s = selected ?: return@PrimaryButton
            out += LocalContextRecallResult(participantId, mode, item.id, item.localRegion, s, ScoringUtils.calculateLocalContextAccuracy(item.localRegion, s), System.currentTimeMillis() - start)
            if (idx == targets.lastIndex) onDone(out) else { idx++; selected = null; start = System.currentTimeMillis() }
        }
    }
}
