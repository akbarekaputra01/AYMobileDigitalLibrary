package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.model.DistractorResult
import com.example.aymobiledigitallibrary.ui.components.*
import kotlin.random.Random

@Composable
fun ShortActivityScreen(participantId: String, onDone: (DistractorResult) -> Unit) {
    val numbers = remember { List(10) { Random.nextInt(10, 99) } }
    var index by remember { mutableIntStateOf(0) }
    var correct by remember { mutableIntStateOf(0) }
    val startTime = remember { System.currentTimeMillis() }

    ScreenContainer {
        TaskProgressHeader("Short Activity", index + 1, 10)
        QuestionCard {
            Text("Complete this short activity before continuing.")
            Spacer(Modifier.height(12.dp))
            Text(numbers[index].toString(), style = MaterialTheme.typography.headlineLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ChoiceButton("Odd", false) { if (numbers[index] % 2 != 0) correct++; next(index, startTime, participantId, correct, onDone) { index = it } }
                ChoiceButton("Even", false) { if (numbers[index] % 2 == 0) correct++; next(index, startTime, participantId, correct, onDone) { index = it } }
            }
        }
    }
}

private fun next(index: Int, startTime: Long, participantId: String, correct: Int, onDone: (DistractorResult) -> Unit, setIndex: (Int) -> Unit) {
    if (index == 9) {
        onDone(DistractorResult(participantId, startTime, System.currentTimeMillis(), 10, correct))
    } else setIndex(index + 1)
}
