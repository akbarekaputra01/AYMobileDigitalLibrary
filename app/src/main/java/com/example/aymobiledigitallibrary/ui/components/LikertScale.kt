package com.example.aymobiledigitallibrary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LikertScale(value: Int, onValue: (Int) -> Unit, start: String, end: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        (1..5).forEach { score ->
            Box(
                modifier = Modifier.defaultMinSize(minWidth = 52.dp),
                contentAlignment = Alignment.Center
            ) {
                FilterChip(
                    selected = value == score,
                    onClick = { onValue(score) },
                    label = { Text("$score", textAlign = TextAlign.Center) }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(6.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "1 = $start",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "5 = $end",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}
