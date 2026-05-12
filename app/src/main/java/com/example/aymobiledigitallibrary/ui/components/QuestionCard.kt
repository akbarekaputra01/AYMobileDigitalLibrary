package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.foundation.layout.*;import androidx.compose.material3.*;import androidx.compose.runtime.Composable;import androidx.compose.ui.Modifier;import androidx.compose.ui.unit.dp
@Composable fun QuestionCard(content:@Composable ColumnScope.()->Unit){OutlinedCard(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp),content=content)}}
