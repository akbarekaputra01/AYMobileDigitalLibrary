package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.LocalContextRecallResult
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ScoringUtils

data class NeighborOption(val ids: List<String>)

@Composable
fun LocalContextRecallScreen(participantId: String, mode: BrowsingMode, onDone: (List<LocalContextRecallResult>) -> Unit) {
    val targets = remember { listOf(2, 3, 8, 9, 14, 15, 20, 26).map { LibraryRepository.items[it] } }
    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<NeighborOption?>(null) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<LocalContextRecallResult>() }
    val item = targets[idx]
    val correct = listOf(LibraryRepository.items[LibraryRepository.items.indexOf(item)-1].id, LibraryRepository.items[LibraryRepository.items.indexOf(item)+1].id)
    val distractorPool = LibraryRepository.items.filter { it.id !in correct && it.id != item.id }
    val options = remember(idx) {
        val d1 = listOf(distractorPool[0].id, distractorPool[1].id)
        val d2 = listOf(distractorPool[2].id, distractorPool[3].id)
        listOf(NeighborOption(correct), NeighborOption(d1), NeighborOption(d2)).shuffled()
    }
    val contextLabel = if (mode == BrowsingMode.PAGE_VIEW) "This material appeared on Page ${item.paginationPage}." else "This material appeared in Group ${item.scrollZoneIndex}."
    val helper = "Materials ${((item.scrollZoneIndex-1)*6)+1}–${item.scrollZoneIndex*6}"
    ScreenContainer(scrollable = true) {
        TaskProgressHeader("Nearby Context", idx + 1, targets.size)
        CompactLibraryItemCard(item = item) {}
        Text(contextLabel); Text(helper)
        Text("Which materials appeared closest to this material?")
        Text("Choose the nearby pair that best matches what you remember.")
        options.forEach { opt ->
            val titles = opt.ids.map { id -> LibraryRepository.items.first { it.id == id }.title }
            SelectableCard("Nearby materials", "• ${titles[0]}\n• ${titles[1]}", selected == opt) { selected = opt }
        }
        PrimaryButton("Continue", selected != null) {
            val picked = selected ?: return@PrimaryButton
            val end = System.currentTimeMillis()
            results += LocalContextRecallResult(participantId, mode, item.id, correct.joinToString(","), picked.ids.joinToString(","), ScoringUtils.calculateNeighborPairAccuracy(correct, picked.ids), end-start)
            if (idx == targets.lastIndex) onDone(results) else { idx++; selected = null; start = System.currentTimeMillis() }
        }
    }
}
