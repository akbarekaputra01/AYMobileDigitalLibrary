package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryItem
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.RefindingResult
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ScoringUtils

@Composable
fun RefindingTaskScreen(participantId: String, mode: BrowsingMode, allItems: List<LibraryItem>, onDone: (List<RefindingResult>) -> Unit) {
    val targets = remember { listOf(2, 9, 16, 22, 28).map { allItems[it] } }
    var trial by remember { mutableIntStateOf(0) }
    var wrong by remember { mutableIntStateOf(0) }
    var nextClicks by remember { mutableIntStateOf(0) }
    var prevClicks by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(1) }
    val start = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val out = remember { mutableStateListOf<RefindingResult>() }
    val listState = rememberLazyListState()
    val target = targets[trial]
    val shown = if (mode == BrowsingMode.PAGE_VIEW) allItems.filter { it.paginationPage == page } else allItems

    ScreenContainer {
        Text("Find This Material")
        Text("Find the material shown below.")
        CompactLibraryItemCard(target) {}
        LazyColumn(state = listState, modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(shown) { item -> CompactLibraryItemCard(item) {
                val success = ScoringUtils.calculateRefindingSuccess(target.id, item.id)
                if (!success) return@CompactLibraryItemCard wrong++
                out += RefindingResult(participantId, mode, target.id, item.id, true, start.longValue, System.currentTimeMillis(), System.currentTimeMillis() - start.longValue, wrong, nextClicks, prevClicks, if (mode == BrowsingMode.PAGE_VIEW) page else null, if (mode == BrowsingMode.CONTINUOUS_LIST) null else null)
                if (trial == targets.lastIndex) onDone(out) else { trial++; wrong = 0; nextClicks = 0; prevClicks = 0; page = 1; start.longValue = System.currentTimeMillis() }
            } }
        }
        if (mode == BrowsingMode.PAGE_VIEW) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SecondaryButton("Previous", { page--; prevClicks++ }, page > 1)
            SecondaryButton("Next", { page++; nextClicks++ }, page < 5)
        }
        if (wrong > 0) Text("That is not the material. Keep looking.")
    }
}
