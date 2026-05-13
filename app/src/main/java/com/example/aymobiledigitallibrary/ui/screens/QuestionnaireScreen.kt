package com.example.aymobiledigitallibrary.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.model.QuestionnaireResponse
import com.example.aymobiledigitallibrary.model.TaskType
import com.example.aymobiledigitallibrary.ui.components.LikertScale
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer

@Composable
fun QuestionnaireScreen(items: List<String>, onDone: (List<QuestionnaireResponse>) -> Unit) {
    BackHandler(enabled = true) {}
    val responses = remember { mutableStateMapOf<Int, Int>() }
    var submitting by remember { mutableStateOf(false) }

    ScreenContainer(scrollable = true) {
        Text("Experience Questions", style = MaterialTheme.typography.headlineMedium)
        Text("Please rate your experience. 1 = Strongly Disagree, 5 = Strongly Agree.")
        items.forEachIndexed { idx, q ->
            OutlinedCard {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(q, style = MaterialTheme.typography.titleMedium)
                    LikertScale(responses[idx] ?: 0, { responses[idx] = it }, "1", "5")
                }
            }
        }
        PrimaryButton(
            text = "Continue",
            enabled = responses.size == items.size && !submitting,
            onClick = {
                submitting = true
                onDone(items.mapIndexed { i, t -> QuestionnaireResponse(TaskType.QUESTIONNAIRE, t, responses[i] ?: 0) })
            }
        )
    }
}
