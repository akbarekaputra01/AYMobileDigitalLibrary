package com.example.aymobiledigitallibrary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LikertScale(value:Int,onValue:(Int)->Unit,start:String,end:String){
    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){ (1..5).forEach{ FilterChip(selected=value==it,onClick={onValue(it)},label={Text("$it")}) } }
    Text("$start = Strongly Disagree    $end = Strongly Agree", style = MaterialTheme.typography.bodySmall)
}
