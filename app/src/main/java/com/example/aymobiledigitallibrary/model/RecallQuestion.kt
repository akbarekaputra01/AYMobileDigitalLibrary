package com.example.aymobiledigitallibrary.model
data class RecallQuestion(val taskType:TaskType,val itemId:String,val prompt:String,val options:List<String>,val correctAnswer:String)
