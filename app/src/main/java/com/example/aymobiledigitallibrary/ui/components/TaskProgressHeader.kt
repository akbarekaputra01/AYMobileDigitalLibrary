package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.material3.*;import androidx.compose.runtime.Composable
@Composable fun TaskProgressHeader(title:String,step:Int,total:Int){Text(title,style=MaterialTheme.typography.headlineMedium);LinearProgressIndicator(progress={step.toFloat()/total.coerceAtLeast(1)})}
