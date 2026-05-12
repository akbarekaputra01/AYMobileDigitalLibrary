package com.example.aymobiledigitallibrary.model
data class RecallAnswer(val taskType:TaskType,val itemId:String,val correctAnswer:String,val selectedAnswer:String,val absoluteError:Int?=null,val isCorrect:Boolean,val responseTimeMs:Long,val timestamp:Long=System.currentTimeMillis())
