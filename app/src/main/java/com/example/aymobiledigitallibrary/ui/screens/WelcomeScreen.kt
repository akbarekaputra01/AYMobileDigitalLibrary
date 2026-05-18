package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    ScreenContainer {
        Spacer(modifier = Modifier.height(24.dp))

        Card {
            Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                        modifier =
                                Modifier.size(72.dp)
                                        .background(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = RoundedCornerShape(18.dp)
                                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                ) {
                    Text(
                            text = "LIB",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                    )
                }

                Text(
                        text = "Mobile Digital Library",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                )

                Text(
                        text = "Explore academic books on your phone",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                )

                Text(
                        text =
                                "Browse a collection of academic books and complete a few short activities related to what you viewed.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                )

                AssistChip(
                        onClick = {},
                        enabled = false,
                        label = { Text(text = "Estimated time: 10-15 mins") }
                )

                PrimaryButton(text = "Start", onClick = { onStart() })
            }
        }
    }
}
