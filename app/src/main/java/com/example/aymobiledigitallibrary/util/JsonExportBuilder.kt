package com.example.aymobiledigitallibrary.util
import com.example.aymobiledigitallibrary.model.ExperimentResult
object JsonExportBuilder { fun build(participantId:String, mode:String, result:ExperimentResult)= """{"participantId":"$participantId","browsingMode":"$mode","result":"${result}"}""" }
