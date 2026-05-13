package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.GlobalRecallResult
import com.example.aymobiledigitallibrary.ui.components.CompactLibraryItemCard
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.ui.components.SelectableCard
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader
import com.example.aymobiledigitallibrary.util.ScoringUtils

@Composable
fun GlobalLocationRecallScreen(
    participantId: String,
    mode: BrowsingMode,
    onDone: (List<GlobalRecallResult>) -> Unit
) {
    val targets = remember {
        listOf(2, 3, 8, 9, 14, 15, 20, 26).map { LibraryRepository.items[it] }
    }

    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<GlobalRecallResult>() }

    val options = if (mode == BrowsingMode.PAGE_VIEW) {
        (1..5).map { "Page $it" }
    } else {
        (1..5).map { "Group $it" }
    }

    val helpers = listOf(
        "Materials 1–6",
        "Materials 7–12",
        "Materials 13–18",
        "Materials 19–24",
        "Materials 25–30"
    )

    val item = targets[idx]

    ScreenContainer(scrollable = true) {
        TaskProgressHeader(
            title = "Where Did You See This?",
            step = idx + 1,
            total = targets.size
        )

        CompactLibraryItemCard(item = item) {}

        Text("Where did this material appear in the library?")

        options.forEachIndexed { optionIndex, optionTitle ->
            val optionValue = optionIndex + 1

            SelectableCard(
                title = optionTitle,
                desc = helpers[optionIndex],
                selected = selected == optionValue,
                onClick = {
                    selected = optionValue
                }
            )
        }

        PrimaryButton(
            text = "Continue",
            enabled = selected != -1,
            onClick = {
                val correct = if (mode == BrowsingMode.PAGE_VIEW) {
                    item.paginationPage
                } else {
                    item.scrollZoneIndex
                }

                val endTime = System.currentTimeMillis()

                results += GlobalRecallResult(
                    participantId = participantId,
                    browsingMode = mode,
                    targetItemId = item.id,
                    correctIndex = correct,
                    selectedIndex = selected,
                    accuracy = ScoringUtils.calculateAccuracy(correct, selected),
                    absoluteError = ScoringUtils.calculateAbsoluteError(correct, selected),
                    responseTimeMillis = endTime - start
                )

                if (idx == targets.lastIndex) {
                    onDone(results)
                } else {
                    idx++
                    selected = -1
                    start = System.currentTimeMillis()
                }
            }
        )
    }
}