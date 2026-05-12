package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.ui.components.SectionTitle
import com.example.aymobiledigitallibrary.ui.components.SelectableCard
import kotlin.random.Random

@Composable
fun BrowsingModeSetupScreen(
    onApply: (BrowsingMode) -> Unit
) {
    var selectedMode by remember { mutableStateOf("CONT") }

    ScreenContainer {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(
                "Browsing Mode Setup",
                "Select how materials will be displayed in this session."
            )

            SelectableCard(
                title = "Continuous List",
                desc = "Browse all materials in one continuous vertical list.",
                selected = selectedMode == "CONT",
                onClick = {
                    selectedMode = "CONT"
                }
            )

            SelectableCard(
                title = "Page View",
                desc = "Browse materials across separate pages.",
                selected = selectedMode == "PAGE",
                onClick = {
                    selectedMode = "PAGE"
                }
            )

            SelectableCard(
                title = "Random Assignment",
                desc = "Let the system choose a browsing mode.",
                selected = selectedMode == "RAND",
                onClick = {
                    selectedMode = "RAND"
                }
            )

            PrimaryButton(
                text = "Apply & Start",
                onClick = {
                    val mode = when (selectedMode) {
                        "CONT" -> BrowsingMode.CONTINUOUS_LIST
                        "PAGE" -> BrowsingMode.PAGE_VIEW
                        else -> {
                            if (Random.nextBoolean()) {
                                BrowsingMode.CONTINUOUS_LIST
                            } else {
                                BrowsingMode.PAGE_VIEW
                            }
                        }
                    }

                    onApply(mode)
                }
            )
        }
    }
}