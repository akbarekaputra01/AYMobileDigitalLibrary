package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.material3.*;import androidx.compose.runtime.Composable
@Composable fun ChoiceButton(text:String,selected:Boolean,onClick:()->Unit){OutlinedButton(onClick=onClick,colors=ButtonDefaults.outlinedButtonColors(containerColor=if(selected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface)){Text(text)}}
