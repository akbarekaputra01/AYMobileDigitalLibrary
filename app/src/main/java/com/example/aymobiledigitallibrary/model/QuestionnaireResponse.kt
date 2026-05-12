package com.example.aymobiledigitallibrary.model
data class QuestionnaireResponse(val taskType:TaskType,val itemText:String,val rating:Int,val timestamp:Long=System.currentTimeMillis())
