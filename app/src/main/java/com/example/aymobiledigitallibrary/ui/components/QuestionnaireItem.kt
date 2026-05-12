package com.example.aymobiledigitallibrary.ui.components
import androidx.compose.foundation.layout.*;import androidx.compose.material3.*;import androidx.compose.runtime.Composable
@Composable fun QuestionnaireItem(text:String,value:Int,onValue:(Int)->Unit){Text(text);LikertScale(value,onValue,"1","5")}
