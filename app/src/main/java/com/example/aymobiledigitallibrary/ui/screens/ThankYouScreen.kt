package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton

@Composable
fun ThankYouScreen(
    onResearcherSummary: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var showPinDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                showError = false
                pin = ""
            },
            title = {
                Text(text = "Researcher Access")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = {
                            pin = it
                            showError = false
                        },
                        label = {
                            Text(text = "PIN")
                        },
                        singleLine = true
                    )

                    if (showError) {
                        Text(
                            text = "Incorrect PIN.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (pin == "2026") {
                            showPinDialog = false
                            onResearcherSummary()
                        } else {
                            showError = true
                        }
                    }
                ) {
                    Text(text = "Continue")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPinDialog = false
                        showError = false
                        pin = ""
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedCard {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Thank You",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "Your responses have been saved.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Please return the device to the researcher.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                PrimaryButton(
                    text = "Researcher Summary",
                    onClick = {
                        showPinDialog = true
                    }
                )
            }
        }
    }
}