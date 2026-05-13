package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.LocalContextRecallResult
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ScoringUtils
import kotlin.random.Random

data class NeighborOption(val ids: List<String>)

@Composable
fun LocalContextRecallScreen(participantId: String, mode: BrowsingMode, onDone: (List<LocalContextRecallResult>) -> Unit) {
    val targets = remember { listOf(20, 2, 26, 9, 15, 3, 14, 8).map { LibraryRepository.items[it] } }
    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<NeighborOption?>(null) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<LocalContextRecallResult>() }
    val item = targets[idx]
    val correct = listOf(LibraryRepository.items[LibraryRepository.items.indexOf(item)-1].id, LibraryRepository.items[LibraryRepository.items.indexOf(item)+1].id)
    val correctSet = correct.toSet()
    val options = remember(participantId, idx) {
        val itemIndex = LibraryRepository.items.indexOf(item)
        val targetGroupStart = (itemIndex / 6) * 6
        val targetGroupEnd = targetGroupStart + 5
        val distractorCandidates = LibraryRepository.items.withIndex()
            .filter { (index, candidate) ->
                index % 6 in 1..4 &&
                    index !in targetGroupStart..targetGroupEnd &&
                    candidate.id != item.id &&
                    candidate.id !in correctSet
            }
            .map { it.index }
        val random = Random(participantId.hashCode() * 31 + idx * 101)
        val selectedDistractorCenters = distractorCandidates.shuffled(random).take(2)
        val distractorPairs = selectedDistractorCenters.map { centerIndex ->
            listOf(
                LibraryRepository.items[centerIndex - 1].id,
                LibraryRepository.items[centerIndex + 1].id
            )
        }
        listOf(NeighborOption(correct)) + distractorPairs.map { NeighborOption(it) }
            .shuffled(random)
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
        Spacer(modifier = Modifier.height(16.dp))
    }
}
