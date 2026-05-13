package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.model.ParticipantInfo
import com.example.aymobiledigitallibrary.ui.components.*

@Composable
fun SessionSetupScreen(
    initialId: String,
    onContinue: (ParticipantInfo) -> Unit
) {
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var s1 by remember { mutableIntStateOf(0) }
    var s2 by remember { mutableIntStateOf(0) }
    var s3 by remember { mutableIntStateOf(0) }
    var s4 by remember { mutableIntStateOf(0) }
    var s5 by remember { mutableIntStateOf(0) }

    val ok = age.isNotBlank() &&
            gender.isNotBlank() &&
            listOf(s1, s2, s3, s4, s5).all { it in 1..5 }

    ScreenContainer(scrollable = true) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SectionTitle("Session Setup")

            Text(
                "This ID is automatically generated and does not contain your personal identity.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "ID: $initialId",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Gender", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row {
                    listOf("Female", "Male", "Other").forEach {
                        FilterChip(
                            selected = gender == it,
                            onClick = { gender = it },
                            label = { Text(it) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

            HorizontalDivider()

            SetupLikertQuestion(
                question = "1. Familiarity with mobile scrolling",
                value = s1,
                onChange = { s1 = it },
                startLabel = "Not at all",
                endLabel = "Extremely"
            )

            SetupLikertQuestion(
                question = "2. Familiarity with page-based reading",
                value = s2,
                onChange = { s2 = it },
                startLabel = "Not at all",
                endLabel = "Extremely"
            )

            SetupLikertQuestion(
                question = "3. Frequency of using reading apps",
                value = s3,
                onChange = { s3 = it },
                startLabel = "Rarely",
                endLabel = "Daily"
            )

            SetupLikertQuestion(
                question = "4. Frequency of using digital library apps",
                value = s4,
                onChange = { s4 = it },
                startLabel = "Rarely",
                endLabel = "Daily"
            )

            SetupLikertQuestion(
                question = "5. Self-rated spatial ability",
                value = s5,
                onChange = { s5 = it },
                startLabel = "Poor",
                endLabel = "Excellent"
            )

            Spacer(modifier = Modifier.height(8.dp))

            PrimaryButton(
                text = "Continue",
                enabled = ok,
                onClick = {
                    onContinue(ParticipantInfo(initialId, age, gender, s1, s2, s3, s4, s5))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SetupLikertQuestion(
    question: String,
    value: Int,
    onChange: (Int) -> Unit,
    startLabel: String,
    endLabel: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        LikertScale(
            value = value,
            onValue = onChange,
            start = startLabel,
            end = endLabel
        )
    }
}