package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryItem
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.model.RefindingResult
import com.example.aymobiledigitallibrary.ui.components.LibraryItemCard
import com.example.aymobiledigitallibrary.ui.components.SecondaryButton
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader

@Composable
fun RefindingTaskScreen(
    participantId: String,
    mode: BrowsingMode,
    allItems: List<LibraryItem>,
    onDone: (List<RefindingResult>) -> Unit
) {
    val targets = remember {
        listOf(22, 2, 28, 9, 16).map { allItems[it] }
    }

    var trial by remember { mutableIntStateOf(0) }
    var wrong by remember { mutableIntStateOf(0) }
    var nextClicks by remember { mutableIntStateOf(0) }
    var prevClicks by remember { mutableIntStateOf(0) }
    var page by remember { mutableIntStateOf(1) }

    val start = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val results = remember { mutableStateListOf<RefindingResult>() }

    val target = targets[trial]
    val shownItems = if (mode == BrowsingMode.PAGE_VIEW) {
        allItems.chunked(6).getOrElse(page - 1) { emptyList() }
    } else {
        allItems
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskProgressHeader(
            title = "Find This Book • ${trial + 1} of ${targets.size}",
            step = trial + 1,
            total = targets.size
        )

        Text(
            text = "Find the target in the library below.",
            style = MaterialTheme.typography.bodySmall
        )

        RefindingTargetPanel(target = target)

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            if (mode == BrowsingMode.PAGE_VIEW) {
                Text(
                    text = "Page $page of 5",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (wrong > 0) {
            Text(
                text = "That is not the target. Keep looking.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shownItems) { item ->
                LibraryItemCard(
                    item = item,
                    onClick = {
                        if (item.id != target.id) {
                            wrong++
                            return@LibraryItemCard
                        }

                            val end = System.currentTimeMillis()

                            results += RefindingResult(
                                participantId = participantId,
                                browsingMode = mode,
                                targetItemId = target.id,
                                selectedItemId = item.id,
                                success = true,
                                startTimeMillis = start.longValue,
                                endTimeMillis = end,
                                timeToFindMillis = end - start.longValue,
                                wrongClickCount = wrong,
                                nextClickCount = nextClicks,
                                previousClickCount = prevClicks,
                                finalPage = if (mode == BrowsingMode.PAGE_VIEW) page else null,
                                finalScrollZone = if (mode == BrowsingMode.CONTINUOUS_LIST) {
                                    (allItems.indexOf(item) / 6) + 1
                                } else {
                                    null
                                }
                            )

                            if (trial == targets.lastIndex) {
                                onDone(results)
                            } else {
                                trial++
                                wrong = 0
                                nextClicks = 0
                                prevClicks = 0
                                page = 1
                                start.longValue = System.currentTimeMillis()
                                scope.launch { listState.scrollToItem(0) }
                            }
                        }
                    )
                }
            }

        if (mode == BrowsingMode.PAGE_VIEW) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButton(
                    text = "Previous",
                    onClick = {
                        page--
                        prevClicks++
                    },
                    enabled = page > 1
                )

                SecondaryButton(
                    text = "Next",
                    onClick = {
                        page++
                        nextClicks++
                    },
                    enabled = page < 5
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RefindingTargetPanel(
    target: LibraryItem
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "TARGET",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        LibraryItemCard(item = target, onClick = {})
    }
}