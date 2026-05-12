package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.LocalContextRecallResult
import com.example.aymobiledigitallibrary.model.LocalRegion
import com.example.aymobiledigitallibrary.ui.components.CompactLibraryItemCard
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.ui.components.SelectableCard
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader
import com.example.aymobiledigitallibrary.util.ScoringUtils

@Composable
fun LocalContextRecallScreen(
    participantId: String,
    mode: BrowsingMode,
    onDone: (List<LocalContextRecallResult>) -> Unit
) {
    val targets = remember {
        listOf(0, 3, 7, 11, 15, 19, 23, 28).map { LibraryRepository.items[it] }
    }

    var idx by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<LocalRegion?>(null) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<LocalContextRecallResult>() }

    val item = targets[idx]

    ScreenContainer(scrollable = true) {
        TaskProgressHeader(
            title = "Nearby Placement",
            step = idx + 1,
            total = targets.size
        )

        CompactLibraryItemCard(item = item) {}

        Text("In its nearby group of materials, where was this material placed?")
        Text("Choose the closest placement.")

        SelectableCard(
            title = "Beginning Area",
            desc = "First two materials in that group",
            selected = selected == LocalRegion.TOP,
            onClick = {
                selected = LocalRegion.TOP
            }
        )

        SelectableCard(
            title = "Middle Area",
            desc = "Middle two materials in that group",
            selected = selected == LocalRegion.MIDDLE,
            onClick = {
                selected = LocalRegion.MIDDLE
            }
        )

        SelectableCard(
            title = "End Area",
            desc = "Last two materials in that group",
            selected = selected == LocalRegion.BOTTOM,
            onClick = {
                selected = LocalRegion.BOTTOM
            }
        )

        PrimaryButton(
            text = "Continue",
            enabled = selected != null,
            onClick = {
                val selectedPlacement = selected ?: return@PrimaryButton
                val endTime = System.currentTimeMillis()

                results += LocalContextRecallResult(
                    participantId = participantId,
                    browsingMode = mode,
                    targetItemId = item.id,
                    correctPlacement = item.localRegion,
                    selectedPlacement = selectedPlacement,
                    accuracy = ScoringUtils.calculateLocalContextAccuracy(
                        correct = item.localRegion,
                        selected = selectedPlacement
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
    }
}