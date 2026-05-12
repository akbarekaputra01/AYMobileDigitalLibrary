package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.foundation.layout.*;import androidx.compose.material3.*;import androidx.compose.runtime.Composable;import androidx.compose.ui.Modifier;import androidx.compose.foundation.shape.RoundedCornerShape;import androidx.compose.ui.unit.dp
@Composable fun PrimaryButton(text:String,onClick:()->Unit,enabled:Boolean=true)=Button(onClick=onClick,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp),enabled=enabled,shape=RoundedCornerShape(12.dp)){Text(text)}
