package com.example.aymobiledigitallibrary.util

import com.example.aymobiledigitallibrary.model.ExperimentResult

object CsvExportBuilder {
    fun build(r: ExperimentResult): String = buildString {
        appendLine("=== SESSION INFO ===")
        appendLine("Participant ID,${r.participantId}")
        appendLine("Browsing Mode,${r.browsingMode}")
        appendLine()

        appendLine("=== PARTICIPANT INFO ===")
        appendLine("Age,${r.participantInfo?.age ?: ""}")
        appendLine("Gender,${r.participantInfo?.gender ?: ""}")
        appendLine("Scrolling Familiarity,${r.participantInfo?.scrollingFamiliarity ?: ""}")
        appendLine("Pagination Familiarity,${r.participantInfo?.paginationFamiliarity ?: ""}")
        appendLine("Reading App Frequency,${r.participantInfo?.readingAppFrequency ?: ""}")
        appendLine("Digital Library Frequency,${r.participantInfo?.digitalLibraryFrequency ?: ""}")
        appendLine("Spatial Ability,${r.participantInfo?.spatialAbility ?: ""}")
        appendLine()

        r.summaryMetrics?.let { s ->
            appendLine("=== SUMMARY METRICS ===")
            appendLine("Global Location Accuracy,${s.globalLocationAccuracy}")
            appendLine("Mean Global Absolute Error,${s.meanGlobalAbsoluteError}")
            appendLine("Local Context Accuracy,${s.localContextAccuracy}")
            appendLine("Re-finding Success Rate,${s.refindingSuccessRate}")
            appendLine("Mean Re-finding Time (ms),${s.meanRefindingTimeMillis}")
            appendLine("Total Wrong Clicks,${s.totalWrongClicks}")
            appendLine("Total Next Clicks,${s.totalNextClicks}")
            appendLine("Total Previous Clicks,${s.totalPreviousClicks}")
            appendLine("Mean Experience Score,${s.meanExperienceScore}")
            appendLine()
        }

        appendLine("=== RAW DATA: GLOBAL RECALL ===")
        appendLine("Target Item ID,Correct Index,Selected Index,Accuracy,Absolute Error,Response Time (ms)")
        r.globalRecallResults.forEach { 
            appendLine("${it.targetItemId},${it.correctIndex},${it.selectedIndex},${it.accuracy},${it.absoluteError},${it.responseTimeMillis}")
        }
        appendLine()

        appendLine("=== RAW DATA: LOCAL CONTEXT RECALL ===")
        appendLine("Target Item ID,Correct Neighbors,Selected Neighbors,Accuracy,Response Time (ms)")
        r.localContextRecallResults.forEach { 
            appendLine("${it.targetItemId},${it.correctNeighborItemIdsCsv},${it.selectedNeighborItemIdsCsv},${it.accuracy},${it.responseTimeMillis}")
        }
        appendLine()

        appendLine("=== RAW DATA: RE-FINDING ===")
        appendLine("Target Item ID,Selected Item ID,Success,Start Time,End Time,Time to Find (ms),Wrong Clicks,Next Clicks,Prev Clicks,Final Page,Final Scroll Zone")
        r.refindingResults.forEach { 
            appendLine("${it.targetItemId},${it.selectedItemId},${it.success},${it.startTimeMillis},${it.endTimeMillis},${it.timeToFindMillis},${it.wrongClickCount},${it.nextClickCount},${it.previousClickCount},${it.finalPage ?: ""},${it.finalScrollZone ?: ""}")
        }
        appendLine()

        appendLine("=== RAW DATA: QUESTIONNAIRE ===")
        appendLine("Question,Rating")
        r.questionnaireResponses.forEach { 
            val text = it.itemText.replace("\"", "\"\"")
            appendLine("\"$text\",${it.rating}")
        }
        appendLine()

        appendLine("=== RAW DATA: INTERACTION LOGS ===")
        appendLine("Event Type,Phase,Timestamp,Item ID,Value")
        r.interactionEvents.forEach { 
            appendLine("${it.eventType},${it.phase},${it.timestampMillis},${it.itemId ?: ""},${it.value ?: ""}")
        }
    }
}
