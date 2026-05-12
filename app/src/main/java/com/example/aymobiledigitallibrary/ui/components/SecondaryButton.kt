package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.foundation.layout.heightIn;import androidx.compose.material3.*;import androidx.compose.runtime.Composable;import androidx.compose.ui.Modifier;import androidx.compose.foundation.shape.RoundedCornerShape;import androidx.compose.ui.unit.dp
@Composable fun SecondaryButton(text:String,onClick:()->Unit,enabled:Boolean=true)=OutlinedButton(onClick=onClick,enabled=enabled,modifier=Modifier.heightIn(min=48.dp),shape=RoundedCornerShape(12.dp)){Text(text)}
