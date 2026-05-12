package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.foundation.layout.*;import androidx.compose.material3.*;import androidx.compose.runtime.Composable;import androidx.compose.ui.Modifier
@Composable fun ResultMetricRow(label:String,value:String){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(label);Text(value)}}
