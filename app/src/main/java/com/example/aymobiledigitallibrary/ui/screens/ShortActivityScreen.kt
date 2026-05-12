package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.model.DistractorAnswer
import com.example.aymobiledigitallibrary.ui.components.ChoiceButton
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.QuestionCard
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer
import com.example.aymobiledigitallibrary.ui.components.TaskProgressHeader

@Composable
fun ShortActivityScreen(
    numbers: List<Int>,
    onDone: (List<DistractorAnswer>) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val answers = remember { mutableStateListOf<DistractorAnswer>() }

    val currentNumber = numbers[currentIndex]

    ScreenContainer {
        TaskProgressHeader(
            title = "Short Activity",
            step = currentIndex + 1,
            total = numbers.size
        )

        QuestionCard {
            Text(
                text = "Complete this brief activity before continuing.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Is the following number even or odd?",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = currentNumber.toString(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ChoiceButton(
                    text = "Even",
                    selected = selectedAnswer == "Even",
                    onClick = {
                        selectedAnswer = "Even"
                    }
                )

                ChoiceButton(
                    text = "Odd",
                    selected = selectedAnswer == "Odd",
                    onClick = {
                        selectedAnswer = "Odd"
                    }
                )
            }

            PrimaryButton(
                text = "Continue",
                enabled = selectedAnswer != null,
                onClick = {
                    val correctAnswer = if (currentNumber % 2 == 0) {
                        "Even"
                    } else {
                        "Odd"
                    }

                    val responseTime = System.currentTimeMillis() - startTime
                    val selected = selectedAnswer ?: return@PrimaryButton

                    answers += DistractorAnswer(
                        questionIndex = currentIndex + 1,
                        number = currentNumber,
                        correctAnswer = correctAnswer,
                        participantAnswer = selected,
                        responseTimeMs = responseTime,
                        isCorrect = correctAnswer == selected
                    )

                    if (currentIndex == numbers.lastIndex) {
                        onDone(answers)
                    } else {
                        currentIndex++
                        selectedAnswer = null
                        startTime = System.currentTimeMillis()
                    }
                }
            )
        }
    }
}