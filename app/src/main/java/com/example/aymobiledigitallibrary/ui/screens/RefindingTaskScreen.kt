package com.example.aymobiledigitallibrary.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryItem
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.ui.components.*
import com.example.aymobiledigitallibrary.util.ExperimentConfig

@Composable
fun RefindingTaskScreen(mode: BrowsingMode, allItems: List<LibraryItem>, targets: List<LibraryItem>, onDone: (List<RefindingResult>) -> Unit) {
    BackHandler(enabled = true) {}
    var trial by remember { mutableIntStateOf(0) }
    var wrong by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(1) }
    var clicks by remember { mutableIntStateOf(0) }
    var start by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var foundDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val out = remember { mutableStateListOf<RefindingResult>() }
    val target = targets[trial]

    if (foundDialog) AlertDialog(onDismissRequest = {}, confirmButton = { TextButton({
        foundDialog = false
        if (trial == targets.lastIndex) onDone(out) else { trial++; wrong = 0; clicks = 0; page = 1; start = System.currentTimeMillis() }
    }) { Text("Continue") } }, title = { Text("Material found.") }, text = { Text("Continue to the next material.") })

    Column(Modifier.fillMaxSize()) {
        OutlinedCard(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Find This Material", style = MaterialTheme.typography.headlineSmall)
                Text("Find the material shown below.")
                Text("Target ${trial + 1} / ${targets.size}", style = MaterialTheme.typography.labelSmall)
                CompactLibraryItemCard(target) {}
            }
        }
        val shown = if (mode == BrowsingMode.PAGE_VIEW) allItems.filter { it.paginationPage == page } else allItems
        LazyColumn(state = listState, modifier = Modifier.weight(1f).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(shown) { item ->
                CompactLibraryItemCard(item) {
                    if (item.id == target.id) {
                        out += RefindingResult(target.id, true, System.currentTimeMillis() - start, wrong, scrollCount = listState.firstVisibleItemIndex, maxScrollDepth = (listState.firstVisibleItemIndex.toFloat() / ExperimentConfig.TOTAL_LIBRARY_ITEMS), pageClickCount = clicks, finalPage = page)
                        foundDialog = true
                    } else wrong++
                }
            }
        }
        if (mode == BrowsingMode.PAGE_VIEW) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                SecondaryButton("Previous", { page--; clicks++ }, page > 1)
                Text("Page $page of ${ExperimentConfig.TOTAL_PAGES}")
                SecondaryButton("Next", { page++; clicks++ }, page < ExperimentConfig.TOTAL_PAGES)
            }
        }
    }
}
