package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryItem
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.RefindingResult
import com.example.aymobiledigitallibrary.ui.components.CompactLibraryItemCard
import com.example.aymobiledigitallibrary.ui.components.SecondaryButton
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader

@Composable
fun RefindingTaskScreen(participantId: String, mode: BrowsingMode, allItems: List<LibraryItem>, onDone: (List<RefindingResult>) -> Unit) {
    val targets = remember { listOf(22, 2, 28, 9, 16).map { allItems[it] } }
    var trial by remember { mutableIntStateOf(0) }; var wrong by remember { mutableIntStateOf(0) }; var nextClicks by remember { mutableIntStateOf(0) }; var prevClicks by remember { mutableIntStateOf(0) }; var page by remember { mutableIntStateOf(1) }
    val start = remember { mutableLongStateOf(System.currentTimeMillis()) }; val out = remember { mutableStateListOf<RefindingResult>() }
    val target = targets[trial]; val shown = if (mode == BrowsingMode.PAGE_VIEW) allItems.filter { it.paginationPage == page } else allItems
    Column(Modifier.fillMaxSize().padding(16.dp).statusBarsPadding().navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TaskProgressHeader("Find This Material • ${trial + 1} of ${targets.size}", trial + 1, targets.size)
        Text("Find the target material shown below.", style = MaterialTheme.typography.bodyMedium)
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            )
        ) {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("TARGET MATERIAL", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                CompactLibraryItemCard(target) {}
                Text("Find this item in the library below.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        HorizontalDivider()
        Text("Library materials", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        if (mode == BrowsingMode.PAGE_VIEW) Text("Page $page of 5")
        if (wrong > 0) Text("That is not the material. Keep looking.")
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) { items(shown) { item -> CompactLibraryItemCard(item) { if (item.id != target.id) { wrong++; return@CompactLibraryItemCard }; val end=System.currentTimeMillis(); out += RefindingResult(participantId, mode, target.id, item.id, true, start.longValue, end, end-start.longValue, wrong, nextClicks, prevClicks, if (mode==BrowsingMode.PAGE_VIEW) page else null, if (mode==BrowsingMode.CONTINUOUS_LIST) item.scrollZoneIndex else null); if (trial==targets.lastIndex) onDone(out) else { trial++; wrong=0; nextClicks=0; prevClicks=0; page=1; start.longValue=System.currentTimeMillis() } } } }
        if (mode == BrowsingMode.PAGE_VIEW) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { SecondaryButton("Previous", { page--; prevClicks++ }, page > 1); SecondaryButton("Next", { page++; nextClicks++ }, page < 5) }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
