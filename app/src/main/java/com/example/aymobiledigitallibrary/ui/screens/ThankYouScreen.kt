package com.example.aymobiledigitallibrary.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.ui.components.PrimaryButton

@Composable
fun ThankYouScreen(onResearcherSummary: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(false) }
    var err by remember { mutableStateOf(false) }
    if (show) AlertDialog(onDismissRequest = { show = false }, confirmButton = { TextButton({ if (pin == "2026") onResearcherSummary() else err = true }) { Text("Continue") } }, dismissButton = { TextButton({ show = false }) { Text("Cancel") } }, title = { Text("Researcher Access") }, text = { Column { OutlinedTextField(pin, { pin = it }, label = { Text("PIN") }); if (err) Text("Incorrect PIN.", color = MaterialTheme.colorScheme.error) } })
    Box(Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center) {
        OutlinedCard { Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) { Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(70.dp)); Text("Thank You", style = MaterialTheme.typography.headlineLarge); Text("Your responses have been saved."); Text("Please return the device to the researcher."); PrimaryButton("Researcher Summary") { show = true } } }
    }
}
