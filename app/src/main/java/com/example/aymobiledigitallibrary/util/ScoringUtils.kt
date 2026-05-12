package com.example.aymobiledigitallibrary.util
import com.example.aymobiledigitallibrary.model.*
object ScoringUtils {
    fun accuracy(items: List<RecallAnswer>, t: TaskType) = items.filter { it.taskType == t }.let { if (it.isEmpty()) 0.0 else it.count { a -> a.isCorrect }.toDouble() / it.size }
    fun meanGlobalAbsoluteError(items: List<RecallAnswer>) = items.filter { it.taskType == TaskType.GLOBAL_LOCATION }.mapNotNull { it.absoluteError?.toDouble() }.let { if (it.isEmpty()) 0.0 else it.average() }
    fun refindingSuccessRate(items: List<RefindingResult>) = if (items.isEmpty()) 0.0 else items.count { it.success }.toDouble() / items.size
    fun meanRefindingTime(items: List<RefindingResult>) = items.map { it.timeToFindMs.toDouble() }.let { if (it.isEmpty()) 0.0 else it.average() }
    fun totalWrongClicks(items: List<RefindingResult>) = items.sumOf { it.wrongClickCount }
    fun totalScrollCount(items: List<RefindingResult>) = items.sumOf { it.scrollCount }
    fun totalPageClicks(items: List<RefindingResult>) = items.sumOf { it.pageClickCount }
    fun meanQuestionnaire(items: List<QuestionnaireResponse>, t: TaskType) = items.filter { it.taskType == t }.map { it.rating.toDouble() }.let { if (it.isEmpty()) 0.0 else it.average() }
}
