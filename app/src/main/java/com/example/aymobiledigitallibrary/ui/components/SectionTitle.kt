package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
@Composable fun SectionTitle(t:String,s:String?=null){Text(t,style=MaterialTheme.typography.headlineMedium);if(s!=null) Text(s,color=MaterialTheme.colorScheme.onSurfaceVariant)}
