package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryItem
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.RefindingResult
import com.example.aymobiledigitallibrary.ui.components.CompactLibraryItemCard
import com.example.aymobiledigitallibrary.ui.components.SecondaryButton
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader

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
    val target = targets[trial]
    val shown = if (mode == BrowsingMode.PAGE_VIEW) allItems.filter { it.paginationPage == page } else allItems

    Column(Modifier.fillMaxSize().padding(16.dp).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TaskProgressHeader("Find This Material", trial + 1, targets.size)
        Text("Find the material shown below.")
        CompactLibraryItemCard(target) {}
        if (wrong > 0) Text("That is not the material. Keep looking.")
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(shown) { item ->
                CompactLibraryItemCard(item) {
                    if (item.id != target.id) {
                        wrong++
                        return@CompactLibraryItemCard
                    }
                    out += RefindingResult(participantId, mode, target.id, item.id, true, start.longValue, System.currentTimeMillis(), System.currentTimeMillis() - start.longValue, wrong, nextClicks, prevClicks, if (mode == BrowsingMode.PAGE_VIEW) page else null, if (mode == BrowsingMode.CONTINUOUS_LIST) item.scrollZoneIndex else null)
                    if (trial == targets.lastIndex) onDone(out) else { trial++; wrong = 0; nextClicks = 0; prevClicks = 0; page = 1; start.longValue = System.currentTimeMillis() }
                }
            }
        }
        if (mode == BrowsingMode.PAGE_VIEW) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SecondaryButton("Previous", { page--; prevClicks++ }, page > 1)
                SecondaryButton("Next", { page++; nextClicks++ }, page < 5)
            }
        }
    }
}
