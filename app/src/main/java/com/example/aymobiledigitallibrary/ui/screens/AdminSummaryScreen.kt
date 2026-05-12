package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.storage.ResultStorage
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer

@Composable
fun AdminSummaryScreen(sessionId: String, mode: BrowsingMode, resultStorage: ResultStorage, onReset: () -> Unit = {}) {
    val counts = resultStorage.getCounts()
    ScreenContainer {
        Column {
            Text("Admin Placeholder")
            Text("Participant ID: $sessionId")
            Text("Browsing Mode: ${mode.name}")
            Text("Library Items: ${LibraryRepository.items.size}")
            Text("Distractor completed count: ${counts.distractorCompletedCount}")
            Text("Global recall completed count: ${counts.globalRecallCount}")
            Text("Local context recall completed count: ${counts.localContextRecallCount}")
            Text("Re-finding completed count: ${counts.refindingCount}")
            Text("Full logging and export will be implemented in a later phase.")
            PrimaryButton("Start New Session", onReset)
        }
    }
}
