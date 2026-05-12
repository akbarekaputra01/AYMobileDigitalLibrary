package com.example.aymobiledigitallibrary.model
data class InteractionEvent(val participantId:String,val browsingMode: BrowsingMode?,val phase: ExperimentPhase,val eventType:String,val itemId:String?=null,val value:String?=null,val timestamp:Long=System.currentTimeMillis())
