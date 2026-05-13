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
        allItems.filter { it.paginationPage == page }
    } else {
        allItems
    }

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
            title = "Find This Material • ${trial + 1} of ${targets.size}",
            step = trial + 1,
            total = targets.size
        )

        Text(
            text = "Find the target material in the library below.",
            style = MaterialTheme.typography.bodySmall
        )

        RefindingTargetPanel(target = target)

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Library materials",
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
                text = "That is not the material. Keep looking.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shownItems) { item ->
                RefindingListItemCard(
                    item = item,
                    materialNumber = allItems.indexOfFirst { it.id == item.id } + 1,
                    onClick = {
                        if (item.id != target.id) {
                            wrong++
                            return@RefindingListItemCard
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
                                item.scrollZoneIndex
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
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "TARGET MATERIAL",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = target.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = target.authors,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${target.year} • ${target.documentType}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Category: ${target.category}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RefindingListItemCard(
    item: LibraryItem,
    materialNumber: Int,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Material ${materialNumber.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = item.authors,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${item.year} • ${item.documentType} • ${item.category}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}