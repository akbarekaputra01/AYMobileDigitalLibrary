package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.LocalContextRecallResult
import com.example.aymobiledigitallibrary.ui.components.LibraryItemCard
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.ui.components.SelectableCard
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader
import com.example.aymobiledigitallibrary.util.ScoringUtils
import kotlin.random.Random

private data class NeighborOption(
    val ids: List<String>
)

@Composable
fun LocalContextRecallScreen(
    participantId: String,
    mode: BrowsingMode,
    onDone: (List<LocalContextRecallResult>) -> Unit
) {
    val allItems = LibraryRepository.items

    val targets = remember {
        listOf(20, 2, 26, 9, 15, 3, 14, 8).map { allItems[it] }
    }

    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<NeighborOption?>(null) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<LocalContextRecallResult>() }

    val item = targets[idx]
    val itemIndex = allItems.indexOfFirst { it.id == item.id }

    val correctNeighborIds = listOf(
        allItems[itemIndex - 1].id,
        allItems[itemIndex + 1].id
    )

    val correctSet = correctNeighborIds.toSet()

    val options = remember(participantId, idx) {
        val targetGroupStart = (itemIndex / 6) * 6
        val targetGroupEnd = targetGroupStart + 5

        val distractorCandidates = allItems.withIndex()
            .filter { indexedItem ->
                val index = indexedItem.index
                val candidate = indexedItem.value

                index % 6 in 1..4 &&
                        index !in targetGroupStart..targetGroupEnd &&
                        candidate.id != item.id &&
                        candidate.id !in correctSet
            }
            .map { it.index }

        val random = Random(participantId.hashCode() * 31 + idx * 101)

        val selectedDistractorCenters = distractorCandidates
            .shuffled(random)
            .take(2)

        val distractorPairs = selectedDistractorCenters.map { centerIndex ->
            listOf(
                allItems[centerIndex - 1].id,
                allItems[centerIndex + 1].id
            )
        }

        (listOf(NeighborOption(correctNeighborIds)) + distractorPairs.map { NeighborOption(it) })
            .shuffled(random)
    }

    val calculatedPageOrZone = (itemIndex / 6) + 1

    val materialStart = ((calculatedPageOrZone - 1) * 6) + 1
    val materialEnd = calculatedPageOrZone * 6

    val contextLabel = if (mode == BrowsingMode.PAGE_VIEW) {
        "This book appeared on Page $calculatedPageOrZone."
    } else {
        "This book appeared in Group $calculatedPageOrZone."
    }

    val contextHelper = "Books $materialStart–$materialEnd"

    ScreenContainer(scrollable = true) {
        TaskProgressHeader(
            title = "Nearby Context",
            step = idx + 1,
            total = targets.size
        )

        LibraryItemCard(item = item) {}

        Text(contextLabel)
        Text(contextHelper)

        Text("Which books appeared closest to this book?")
        Text("Choose the nearby pair that best matches what you remember.")

        options.forEach { option ->
            val titles = option.ids.map { id ->
                allItems.first { it.id == id }.title
            }

            SelectableCard(
                title = "Nearby books",
                desc = "• ${titles[0]}\n• ${titles[1]}",
                selected = selected == option,
                onClick = {
                    selected = option
                }
            )
        }

        PrimaryButton(
            text = "Continue",
            enabled = selected != null,
            onClick = {
                val picked = selected ?: return@PrimaryButton
                val endTime = System.currentTimeMillis()

                results += LocalContextRecallResult(
                    participantId = participantId,
                    browsingMode = mode,
                    targetItemId = item.id,
                    correctNeighborItemIdsCsv = correctNeighborIds.joinToString(","),
                    selectedNeighborItemIdsCsv = picked.ids.joinToString(","),
                    accuracy = ScoringUtils.calculateNeighborPairAccuracy(
                        correctIds = correctNeighborIds,
                        selectedIds = picked.ids
                    ),
                    responseTimeMillis = endTime - start
                )

                if (idx == targets.lastIndex) {
                    onDone(results)
                } else {
                    idx++
                    selected = null
                    start = System.currentTimeMillis()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}