package com.example.aymobiledigitallibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aymobiledigitallibrary.ui.theme.AYMobileDigitalLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AYMobileDigitalLibraryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Single-activity screen flow with beginner-friendly state management.
enum class Screen {
    HOME,
    PAGINATION,
    INFINITE_SCROLL,
    LOCATION_RECALL_TEST,
    RESULT,
    QUESTIONNAIRE,
    FINAL_SUMMARY
}

// Interface mode is the independent variable in this experiment.
enum class InterfaceMode {
    PAGINATION,
    INFINITE_SCROLL
}

// Location zones are used by the location recall test.
enum class LocationZone {
    TOP,
    MIDDLE,
    BOTTOM
}

data class LibraryRecord(
    val title: String,
    val creators: String,
    val year: Int,
    val type: String,
    val description: String
)

data class MemoryQuestion(
    val record: LibraryRecord,
    val correctZone: LocationZone
)

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var selectedMode by remember { mutableStateOf(InterfaceMode.PAGINATION) }
    var browsingStartTime by remember { mutableLongStateOf(0L) }
    var browsingDurationSeconds by remember { mutableLongStateOf(0L) }

    // Shared dataset and fixed recall targets for both interface conditions.
    val records = remember { createLibraryRecordDataset() }
    val memoryQuestions = remember { createMemoryQuestions(records) }
    val participantAnswers = remember { mutableStateListOf<LocationZone>() }

    // Post-task questionnaire state (5 Likert items, values 1..5).
    val questionnaireResponses = remember { mutableStateListOf<Int>() }

    val score = memoryQuestions.zip(participantAnswers).count { (question, answer) ->
        question.correctZone == answer
    }
    val accuracy = if (memoryQuestions.isNotEmpty()) {
        (score * 100) / memoryQuestions.size
    } else {
        0
    }
    val averageQuestionnaireScore = if (questionnaireResponses.isNotEmpty()) {
        questionnaireResponses.average()
    } else {
        0.0
    }

    when (currentScreen) {
        Screen.HOME -> HomeScreen(
            modifier = modifier,
            onPaginationClick = {
                selectedMode = InterfaceMode.PAGINATION
                browsingStartTime = System.currentTimeMillis()
                currentScreen = Screen.PAGINATION
            },
            onInfiniteScrollClick = {
                selectedMode = InterfaceMode.INFINITE_SCROLL
                browsingStartTime = System.currentTimeMillis()
                currentScreen = Screen.INFINITE_SCROLL
            }
        )

        Screen.PAGINATION -> PaginationScreen(
            modifier = modifier,
            records = records,
            onBackClick = { currentScreen = Screen.HOME },
            onFinishBrowsing = {
                browsingDurationSeconds = ((System.currentTimeMillis() - browsingStartTime) / 1000).coerceAtLeast(0L)
                participantAnswers.clear()
                questionnaireResponses.clear()
                currentScreen = Screen.LOCATION_RECALL_TEST
            }
        )

        Screen.INFINITE_SCROLL -> InfiniteScrollScreen(
            modifier = modifier,
            records = records,
            onBackClick = { currentScreen = Screen.HOME },
            onFinishBrowsing = {
                browsingDurationSeconds = ((System.currentTimeMillis() - browsingStartTime) / 1000).coerceAtLeast(0L)
                participantAnswers.clear()
                questionnaireResponses.clear()
                currentScreen = Screen.LOCATION_RECALL_TEST
            }
        )

        Screen.LOCATION_RECALL_TEST -> LocationRecallTestScreen(
            modifier = modifier,
            questions = memoryQuestions,
            onBackClick = {
                participantAnswers.clear()
                questionnaireResponses.clear()
                currentScreen = Screen.HOME
            },
            onAnswerSelected = { zone ->
                participantAnswers.add(zone)
                if (participantAnswers.size == memoryQuestions.size) {
                    currentScreen = Screen.RESULT
                }
            }
        )

        Screen.RESULT -> ResultScreen(
            modifier = modifier,
            interfaceMode = selectedMode,
            score = score,
            totalQuestions = memoryQuestions.size,
            accuracy = accuracy,
            browsingDurationSeconds = browsingDurationSeconds,
            onContinueToQuestionnaire = { currentScreen = Screen.QUESTIONNAIRE },
            onBackToHome = {
                participantAnswers.clear()
                questionnaireResponses.clear()
                currentScreen = Screen.HOME
            }
        )

        Screen.QUESTIONNAIRE -> QuestionnaireScreen(
            modifier = modifier,
            interfaceMode = selectedMode,
            onBackToHome = {
                participantAnswers.clear()
                questionnaireResponses.clear()
                currentScreen = Screen.HOME
            },
            onSubmit = { responses ->
                questionnaireResponses.clear()
                questionnaireResponses.addAll(responses)
                currentScreen = Screen.FINAL_SUMMARY
            }
        )

        Screen.FINAL_SUMMARY -> FinalSummaryScreen(
            modifier = modifier,
            interfaceMode = selectedMode,
            score = score,
            totalQuestions = memoryQuestions.size,
            accuracy = accuracy,
            browsingDurationSeconds = browsingDurationSeconds,
            averageQuestionnaireScore = averageQuestionnaireScore,
            onBackToHome = {
                participantAnswers.clear()
                questionnaireResponses.clear()
                currentScreen = Screen.HOME
            }
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPaginationClick: () -> Unit,
    onInfiniteScrollClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AY Mobile Digital Library",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Interface Experiment",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Explore the digital library collection as you would on a mobile device. After browsing, you will answer several follow-up questions.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )

        Button(
            onClick = onPaginationClick,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
        ) {
            Text("Pagination Interface")
        }

        Button(
            onClick = onInfiniteScrollClick,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .padding(top = 12.dp)
        ) {
            Text("Infinite Scroll Interface")
        }

        Text(
            text = "Researcher note: Use Group A for Pagination first and Group B for Infinite Scroll first.",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun PaginationScreen(
    modifier: Modifier = Modifier,
    records: List<LibraryRecord>,
    onBackClick: () -> Unit,
    onFinishBrowsing: () -> Unit
) {
    val pageSize = 10
    val totalPages = (records.size + pageSize - 1) / pageSize
    var currentPage by remember { mutableStateOf(0) }

    val startIndex = currentPage * pageSize
    val endIndex = minOf(startIndex + pageSize, records.size)
    val currentPageItems = records.subList(startIndex, endIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pagination Interface",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Task: Browse the collection and identify records that seem useful for a literature review about mobile digital library interfaces. When finished, tap Finish Browsing.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        Button(onClick = onBackClick, modifier = Modifier.padding(bottom = 8.dp)) {
            Text("Back")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(currentPageItems) { record ->
                LibraryRecordCard(record)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text("Previous")
            }

            Text(text = "Page ${currentPage + 1} of $totalPages", style = MaterialTheme.typography.bodyMedium)

            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text("Next")
            }
        }

        Button(
            onClick = onFinishBrowsing,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Finish Browsing")
        }
    }
}

@Composable
fun InfiniteScrollScreen(
    modifier: Modifier = Modifier,
    records: List<LibraryRecord>,
    onBackClick: () -> Unit,
    onFinishBrowsing: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Infinite Scroll Interface",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Task: Browse the collection and identify records that seem useful for a literature review about mobile digital library interfaces. When finished, tap Finish Browsing.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        Button(onClick = onBackClick, modifier = Modifier.padding(bottom = 8.dp)) {
            Text("Back")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(records) { record ->
                LibraryRecordCard(record)
            }
        }

        Button(
            onClick = onFinishBrowsing,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Finish Browsing")
        }
    }
}

// Location recall test uses fixed target indexes to balance top/middle/bottom zones.
@Composable
fun LocationRecallTestScreen(
    modifier: Modifier = Modifier,
    questions: List<MemoryQuestion>,
    onBackClick: () -> Unit,
    onAnswerSelected: (LocationZone) -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Location Recall Test",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(onClick = onBackClick) {
            Text("Cancel and Return Home")
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = currentQuestion.record.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Where was this record located?",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Button(
            onClick = {
                onAnswerSelected(LocationZone.TOP)
                if (currentQuestionIndex < questions.lastIndex) currentQuestionIndex++
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Top / Page 1 / Items 1-10") }

        Button(
            onClick = {
                onAnswerSelected(LocationZone.MIDDLE)
                if (currentQuestionIndex < questions.lastIndex) currentQuestionIndex++
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Middle / Page 2 / Items 11-20") }

        Button(
            onClick = {
                onAnswerSelected(LocationZone.BOTTOM)
                if (currentQuestionIndex < questions.lastIndex) currentQuestionIndex++
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Bottom / Page 3 / Items 21-30") }
    }
}

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    interfaceMode: InterfaceMode,
    score: Int,
    totalQuestions: Int,
    accuracy: Int,
    browsingDurationSeconds: Long,
    onContinueToQuestionnaire: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Result", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            text = "Interface mode: ${if (interfaceMode == InterfaceMode.PAGINATION) "Pagination" else "Infinite Scroll"}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(text = "Score: $score / $totalQuestions", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
        Text(text = "Accuracy: $accuracy%", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
        Text(
            text = "Browsing duration: $browsingDurationSeconds seconds",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Please record this result in the experiment spreadsheet.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(onClick = onContinueToQuestionnaire, modifier = Modifier.padding(top = 20.dp)) {
            Text("Continue to Questionnaire")
        }
        Button(onClick = onBackToHome, modifier = Modifier.padding(top = 8.dp)) {
            Text("Back to Home")
        }
    }
}

// Five post-task Likert questions (1 to 5) for subjective interface evaluation.
@Composable
fun QuestionnaireScreen(
    modifier: Modifier = Modifier,
    interfaceMode: InterfaceMode,
    onSubmit: (List<Int>) -> Unit,
    onBackToHome: () -> Unit
) {
    val questions = listOf(
        "I could easily remember where records were located.",
        "The interface helped me revisit previously seen records.",
        "The interface was easy to navigate.",
        "The interface felt efficient for browsing the collection.",
        "I would prefer this interface for mobile digital library use."
    )
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val responses = remember { mutableStateListOf<Int>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Post-Task Questionnaire", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            text = "Interface mode: ${if (interfaceMode == InterfaceMode.PAGINATION) "Pagination" else "Infinite Scroll"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(text = "Scale: 1 = Strongly disagree, 5 = Strongly agree", style = MaterialTheme.typography.bodySmall)
        Text(text = "Question ${currentQuestionIndex + 1} of ${questions.size}", style = MaterialTheme.typography.bodyMedium)

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = questions[currentQuestionIndex],
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            (1..5).forEach { value ->
                Button(onClick = {
                    responses.add(value)
                    if (responses.size == questions.size) {
                        onSubmit(responses.toList())
                    } else {
                        currentQuestionIndex++
                    }
                }) {
                    Text(value.toString())
                }
            }
        }

        Button(onClick = onBackToHome) {
            Text("Back to Home")
        }
    }
}

@Composable
fun FinalSummaryScreen(
    modifier: Modifier = Modifier,
    interfaceMode: InterfaceMode,
    score: Int,
    totalQuestions: Int,
    accuracy: Int,
    browsingDurationSeconds: Long,
    averageQuestionnaireScore: Double,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Final Summary", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = "Participant ID: Record manually", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 12.dp))
        Text(
            text = "Interface mode: ${if (interfaceMode == InterfaceMode.PAGINATION) "Pagination" else "Infinite Scroll"}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(text = "Location recall score: $score / $totalQuestions", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
        Text(text = "Accuracy: $accuracy%", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
        Text(text = "Browsing duration: $browsingDurationSeconds seconds", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
        Text(
            text = "Average questionnaire score: ${"%.2f".format(averageQuestionnaireScore)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Record all values in the experiment spreadsheet.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        Button(onClick = onBackToHome, modifier = Modifier.padding(top = 20.dp)) {
            Text("Back to Home")
        }
    }
}

// Same card UI for both conditions to preserve interface-comparison validity.
@Composable
fun LibraryRecordCard(record: LibraryRecord) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = record.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "${record.creators} (${record.year})", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
            Text(text = record.type, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 4.dp))
            Text(
                text = record.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Location zones: indexes 0-9 TOP, 10-19 MIDDLE, 20-29 BOTTOM.
fun getLocationZone(index: Int): LocationZone {
    return when (index) {
        in 0..9 -> LocationZone.TOP
        in 10..19 -> LocationZone.MIDDLE
        else -> LocationZone.BOTTOM
    }
}

fun createMemoryQuestions(records: List<LibraryRecord>): List<MemoryQuestion> {
    val targetIndexes = listOf(3, 8, 12, 17, 22, 27)
    return targetIndexes.map { index ->
        MemoryQuestion(
            record = records[index],
            correctZone = getLocationZone(index)
        )
    }
}

fun createLibraryRecordDataset(): List<LibraryRecord> {
    return listOf(
        LibraryRecord("Mobile Search Formulation in Academic Library Apps", "A. Rahman, L. Chen", 2019, "Journal Article", "Examines query reformulation behavior when students search scholarly collections on smartphones."),
        LibraryRecord("Design Patterns for Metadata Cards in Library Interfaces", "J. Patel, M. Gomez", 2021, "Journal Article", "Evaluates compact card layouts for displaying title, creator, and abstract snippets on small screens."),
        LibraryRecord("Comparing Vertical Navigation Strategies in Mobile Repositories", "S. Ibrahim, R. Holt", 2020, "Conference Paper", "Reports usability outcomes when users browse long result lists with different navigation controls."),
        LibraryRecord("Cognitive Offloading During Mobile Literature Scanning", "N. Park, T. Aziz", 2022, "Journal Article", "Analyzes how interface structure affects user reliance on positional cues while scanning records."),
        LibraryRecord("Practical Guide to Mobile Digital Library UX", "B. Kumar", 2018, "E-book", "Provides applied guidelines for creating readable and navigable mobile academic library experiences."),
        LibraryRecord("Interface Consistency in Experimental HCI Studies", "T. Mensah, O. Green", 2021, "Journal Article", "Discusses methods to keep non-target variables constant when comparing interface paradigms."),
        LibraryRecord("Proceedings of Mobile Information Retrieval 2020", "L. Freeman, D. Okafor (Eds.)", 2020, "E-book", "Collected papers on mobile retrieval models, browsing interaction, and relevance judgment behavior."),
        LibraryRecord("Re-finding Academic Records in App-Based Catalogs", "W. Grant, N. Aziz", 2023, "Conference Paper", "Presents findings on how users return to previously seen records in mobile discovery sessions."),
        LibraryRecord("Spatial Orientation in Continuous Result Feeds", "M. Alvi", 2024, "Thesis", "Doctoral work studying how continuous feeds influence orientation and position recall in academic browsing."),
        LibraryRecord("Mobile Library Access and Equity: Annual Evidence Review", "Center for Digital Scholarship", 2022, "Research Report", "Summarizes access barriers and interface factors affecting scholarly discovery on mobile devices."),

        LibraryRecord("Attention Patterns in Mid-List Academic Records", "H. Costa, L. Wang", 2023, "Journal Article", "Investigates attention drop-off as users browse deeper into mobile scholarly lists."),
        LibraryRecord("Evaluating Page Cues for Orientation in Digital Collections", "H. Singh, E. Ward", 2020, "Journal Article", "Tests whether page boundaries improve orientation in large-scale mobile collections."),
        LibraryRecord("Navigation Recovery After Interrupted Mobile Browsing", "E. Flores, H. Kim", 2021, "Conference Paper", "Explores how users regain context after interruptions during literature exploration."),
        LibraryRecord("Mobile Reading Session Length and Record Selection", "P. Evans, R. Choi", 2019, "Journal Article", "Quantifies short reading bursts and links them to record triage decisions."),
        LibraryRecord("Academic Discovery Systems for Handheld Devices", "R. Ahmed, C. Lim", 2017, "E-book", "Introduces lightweight discovery workflows optimized for handheld research activity."),
        LibraryRecord("Perceived Control in Scroll and Page Interfaces", "K. Hassan, J. Moore", 2024, "Journal Article", "Measures perceived navigational control across feed-like and paginated interfaces."),
        LibraryRecord("International Symposium on Library Interaction Design 2022", "I. Novak, B. Trent (Eds.)", 2022, "E-book", "Conference proceedings covering interaction design methods for mobile library platforms."),
        LibraryRecord("Backtracking Efficiency in Mobile Academic Search", "D. Pereira, M. Yu", 2021, "Conference Paper", "Compares user efficiency when revisiting previously viewed records across interface styles."),
        LibraryRecord("Positional Memory in Smartphone Research Tasks", "Y. Nakamura", 2023, "Thesis", "Masters thesis assessing recall of record location after relevance judgment tasks."),
        LibraryRecord("Institutional Report on Student Mobile Library Behavior", "University Learning Analytics Unit", 2024, "Research Report", "Institutional analysis of browsing behavior, query habits, and navigation friction in mobile library use."),

        LibraryRecord("Metadata Readability and Search Confidence", "G. Rossi, V. Anand", 2018, "Journal Article", "Examines how text density and spacing shape confidence in selecting relevant records."),
        LibraryRecord("Ranking Signals and Mobile Literature Discovery", "A. Diaz, N. Ibrahim", 2020, "Journal Article", "Analyzes how ranking quality interacts with snippets in mobile discovery tasks."),
        LibraryRecord("Workshop on Spatial Memory in Digital Navigation", "P. Osei, T. Morgan", 2025, "Conference Paper", "Reports pilot studies on memory anchors during app-based academic browsing."),
        LibraryRecord("Mobile Knowledge Organization for Academic Libraries", "A. Yilmaz", 2019, "E-book", "Covers organizational schemes for presenting scholarly records in constrained interfaces."),
        LibraryRecord("Task Success Predictors in Mobile Retrieval", "C. Johnson, E. Park", 2022, "Journal Article", "Identifies interface and content factors associated with successful mobile retrieval outcomes."),
        LibraryRecord("Proceedings of Applied Information Navigation 2021", "S. Li, F. Martin (Eds.)", 2021, "E-book", "Collection of empirical papers on information navigation, wayfinding, and browsing behavior."),
        LibraryRecord("Understanding Scroll Momentum and Record Skipping", "M. Rivera, J. Stone", 2024, "Journal Article", "Shows links between fast scrolling momentum and skipped records in academic collections."),
        LibraryRecord("Comparative Metrics for Interface Memory Experiments", "N. Idris", 2025, "Thesis", "Proposes measurement frameworks for recall accuracy and browsing efficiency in interface studies."),
        LibraryRecord("National Survey of Mobile Academic Search Practices", "Digital Library Research Consortium", 2023, "Research Report", "Presents multi-campus survey findings on mobile academic search and navigation preferences."),
        LibraryRecord("Search Persistence in Long Mobile Result Sets", "F. Silva, P. Noor", 2018, "Conference Paper", "Analyzes persistence patterns as users continue browsing deeper into mobile result sets.")
    )
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AYMobileDigitalLibraryTheme {
        AppContent()
    }
}
