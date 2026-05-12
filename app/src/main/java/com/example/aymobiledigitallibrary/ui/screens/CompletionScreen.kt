package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton
import com.example.aymobiledigitallibrary.ui.components.ScreenContainer

@Composable
fun CompletionScreen(onFinish: () -> Unit) {
    ScreenContainer {
        Text("Thank You")
        Text("Your responses have been saved.")
        PrimaryButton("Finish", onFinish)
    }
}
