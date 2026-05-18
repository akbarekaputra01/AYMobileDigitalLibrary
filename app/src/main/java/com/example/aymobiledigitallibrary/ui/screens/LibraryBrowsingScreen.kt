package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.ui.components.*

@Composable
fun LibraryBrowsingScreen(
    mode: BrowsingMode,
    onItemTap: (String) -> Unit,
    onPageClick: (String) -> Unit,
    onFinish: () -> Unit
) {
    val items = LibraryRepository.items
    var page by remember { mutableIntStateOf(1) }
    val state = rememberLazyListState()

    // Log browsing started only once
    LaunchedEffect(Unit) {
        onItemTap("browsing_started_flag") // Hacky but works since we don't have a direct onStarted callback in the args anymore
    }

    // Track scroll zone for CONTINUOUS_LIST
    var currentScrollZone by remember { mutableIntStateOf(1) }
    
    LaunchedEffect(state.firstVisibleItemIndex) {
        if (mode == BrowsingMode.CONTINUOUS_LIST) {
            val newZone = (state.firstVisibleItemIndex / 6) + 1
            if (newZone != currentScrollZone && newZone > 0) {
                currentScrollZone = newZone
                onPageClick(newZone.toString())
            }
        }
    }

    ScreenContainer(scrollable = false) {
        SectionTitle("Library", "Browse the books naturally.")
        ModeLabel(if (mode == BrowsingMode.CONTINUOUS_LIST) "Continuous List" else "Page View")

        if (mode == BrowsingMode.CONTINUOUS_LIST) {
            LinearProgressIndicator(progress = { (state.firstVisibleItemIndex + 1) / 30f })
            LazyColumn(
                state = state,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) {
                    LibraryItemCard(it) { onItemTap(it.id) }
                }
            }
        } else {
            Text("Page $page of 5")
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items.chunked(6).getOrElse(page - 1) { emptyList() }) {
                    LibraryItemCard(it) { onItemTap(it.id) }
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButton("Previous", {
                    page--
                    onPageClick(page.toString())
                }, page > 1)
                SecondaryButton("Next", {
                    page++
                    onPageClick(page.toString())
                }, page < 5)
            }
        }
        PrimaryButton("Finish Browsing", onFinish)
    }
}
