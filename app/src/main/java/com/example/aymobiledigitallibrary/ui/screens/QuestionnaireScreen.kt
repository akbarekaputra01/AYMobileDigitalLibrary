package com.example.aymobiledigitallibrary.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.ui.components.*

@Composable
fun QuestionnaireScreen(title: String, items: List<String>, type: TaskType, onDone: (List<QuestionnaireResponse>) -> Unit) {
    BackHandler(enabled = true) {}
    val responses = remember { mutableStateMapOf<Int, Int>() }
    var submitting by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Experience Questions", style = MaterialTheme.typography.headlineMedium)
        Text("We value your feedback", style = MaterialTheme.typography.headlineLarge)
        Text("Please rate your experience using the archive. 1 = Strongly Disagree, 5 = Strongly Agree.")
        items.forEachIndexed { idx, q ->
            OutlinedCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text(q, style = MaterialTheme.typography.headlineSmall); LikertScale(responses[idx] ?: 0, { responses[idx] = it }, "1", "5") } }
        }
        val done = responses.size == items.size && !submitting
        PrimaryButton(
            text = if (type == TaskType.USABILITY) "Submit Feedback" else "Continue", // Use your actual parameter name for the string here
            enabled = done,
            onClick = {
                submitting = true
                onDone(items.mapIndexed { i, t -> QuestionnaireResponse(type, t, responses[i] ?: 0) })
            }
        )
    }
}
