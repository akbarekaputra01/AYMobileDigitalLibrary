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

// Simple app-level screens for beginner-friendly navigation in one file.
enum class Screen {
    HOME,
    PAGINATION,
    INFINITE_SCROLL,
    MEMORY_TEST,
    RESULT
}

enum class InterfaceMode {
    PAGINATION,
    INFINITE_SCROLL
}

enum class LocationZone {
    TOP,
    MIDDLE,
    BOTTOM
}

// Data model for one article item.
data class Article(
    val title: String,
    val authors: String,
    val year: Int,
    val abstract: String
)

data class MemoryQuestion(
    val article: Article,
    val correctZone: LocationZone
)

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var selectedMode by remember { mutableStateOf(InterfaceMode.PAGINATION) }
    var browsingStartTime by remember { mutableLongStateOf(0L) }
    var browsingDurationSeconds by remember { mutableLongStateOf(0L) }

    // Fixed dataset of 30 articles, reused by BOTH interfaces.
    val articles = remember { createArticleDataset() }
    val memoryQuestions = remember { createMemoryQuestions(articles) }
    val participantAnswers = remember { mutableStateListOf<LocationZone>() }

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
            articles = articles,
            onBackClick = { currentScreen = Screen.HOME },
            onFinishBrowsing = {
                browsingDurationSeconds = ((System.currentTimeMillis() - browsingStartTime) / 1000).coerceAtLeast(0L)
                participantAnswers.clear()
                currentScreen = Screen.MEMORY_TEST
            }
        )

        Screen.INFINITE_SCROLL -> InfiniteScrollScreen(
            modifier = modifier,
            articles = articles,
            onBackClick = { currentScreen = Screen.HOME },
            onFinishBrowsing = {
                browsingDurationSeconds = ((System.currentTimeMillis() - browsingStartTime) / 1000).coerceAtLeast(0L)
                participantAnswers.clear()
                currentScreen = Screen.MEMORY_TEST
            }
        )

        Screen.MEMORY_TEST -> SpatialMemoryTestScreen(
            modifier = modifier,
            questions = memoryQuestions,
            onBackClick = {
                participantAnswers.clear()
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
            questions = memoryQuestions,
            answers = participantAnswers.toList(),
            browsingDurationSeconds = browsingDurationSeconds,
            onBackToHome = {
                participantAnswers.clear()
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
            text = "Browse the article list, then answer memory questions about article locations.",
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
    }
}

// Research condition 1: page-based navigation with page cues.
@Composable
fun PaginationScreen(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onBackClick: () -> Unit,
    onFinishBrowsing: () -> Unit
) {
    val pageSize = 10
    val totalPages = (articles.size + pageSize - 1) / pageSize
    var currentPage by remember { mutableStateOf(0) }

    val startIndex = currentPage * pageSize
    val endIndex = minOf(startIndex + pageSize, articles.size)
    val currentPageItems = articles.subList(startIndex, endIndex)

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

        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text("Back")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(currentPageItems) { article ->
                ArticleCard(article = article)
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

            Text(
                text = "Page ${currentPage + 1} of $totalPages",
                style = MaterialTheme.typography.bodyMedium
            )

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

// Research condition 2: continuous scrolling without page cues.
@Composable
fun InfiniteScrollScreen(
    modifier: Modifier = Modifier,
    articles: List<Article>,
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

        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text("Back")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(articles) { article ->
                ArticleCard(article = article)
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

// Post-browsing task to measure spatial memory using equivalent location zones.
@Composable
fun SpatialMemoryTestScreen(
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
            text = "Spatial Memory Test",
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
                    text = currentQuestion.article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Where was this article located?",
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
        ) {
            Text("Top / Page 1 / Items 1-10")
        }

        Button(
            onClick = {
                onAnswerSelected(LocationZone.MIDDLE)
                if (currentQuestionIndex < questions.lastIndex) currentQuestionIndex++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Middle / Page 2 / Items 11-20")
        }

        Button(
            onClick = {
                onAnswerSelected(LocationZone.BOTTOM)
                if (currentQuestionIndex < questions.lastIndex) currentQuestionIndex++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bottom / Page 3 / Items 21-30")
        }
    }
}

// Displays experiment outcome using the selected interface condition.
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    interfaceMode: InterfaceMode,
    questions: List<MemoryQuestion>,
    answers: List<LocationZone>,
    browsingDurationSeconds: Long,
    onBackToHome: () -> Unit
) {
    val score = questions.zip(answers).count { (question, answer) -> question.correctZone == answer }
    val accuracy = if (questions.isNotEmpty()) (score * 100) / questions.size else 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Experiment Result",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Interface mode: ${if (interfaceMode == InterfaceMode.PAGINATION) "Pagination" else "Infinite Scroll"}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "Score: $score / ${questions.size}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Accuracy: $accuracy%",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Browsing duration: $browsingDurationSeconds seconds",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Higher score indicates better recall of article location.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onBackToHome,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Back to Home")
        }
    }
}

fun getLocationZone(index: Int): LocationZone {
    return when (index) {
        in 0..9 -> LocationZone.TOP
        in 10..19 -> LocationZone.MIDDLE
        else -> LocationZone.BOTTOM
    }
}

fun createMemoryQuestions(articles: List<Article>): List<MemoryQuestion> {
    val targetIndexes = listOf(4, 8, 13, 22, 27)
    return targetIndexes.map { index ->
        MemoryQuestion(
            article = articles[index],
            correctZone = getLocationZone(index)
        )
    }
}

// Shared card design used by BOTH interfaces to preserve experimental validity.
@Composable
fun ArticleCard(article: Article) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${article.authors} (${article.year})",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = article.abstract,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Fixed 30-article dataset (identical for pagination and infinite scroll).
fun createArticleDataset(): List<Article> {
    return listOf(
        Article("Mobile Search Behavior in Academic Libraries", "A. Rahman, L. Chen", 2016, "This study investigates how students perform quick searches on mobile library platforms and identifies common navigation patterns during short research sessions."),
        Article("Designing Readable Metadata Cards", "J. Patel, M. Gomez", 2017, "The paper evaluates card-based metadata presentation and its impact on scan speed, helping interface designers reduce cognitive load in dense result lists."),
        Article("Touch Interaction and Result Selection", "S. Ibrahim, R. Holt", 2018, "Researchers compare tap accuracy and dwell time across different list densities to understand how touch ergonomics affect article selection on smartphones."),
        Article("User Recall Across Scrolling Lists", "N. Park, T. Aziz", 2019, "An experiment explores how continuous scrolling influences memory of item position, showing trade-offs between speed and precise spatial recall."),
        Article("Pagination Cues in Digital Catalogs", "H. Singh, E. Ward", 2020, "This article reports that numbered page boundaries can provide structural cues that improve user orientation in large collections."),
        Article("Abstract Length and Comprehension", "F. Silva, P. Noor", 2015, "The authors test short versus long abstract snippets and conclude that concise previews improve triage decisions without reducing topic understanding."),
        Article("Information Scent in Library Apps", "C. Bennett, Y. Tan", 2021, "The work analyzes titles, author strings, and snippets as information scent signals that guide users toward relevant academic material."),
        Article("Comparing Feed and Page Navigation", "R. Donovan, K. Lee", 2022, "A controlled usability trial compares feed-style browsing with page-by-page navigation and measures effort, confidence, and task completion time."),
        Article("Cognitive Mapping on Small Screens", "M. Alvi, D. Brooks", 2018, "This study links interface landmarks to stronger cognitive maps, suggesting repeated visual anchors help users remember where items were found."),
        Article("Digital Library UX for Novice Researchers", "B. Kumar, S. Hart", 2019, "The paper identifies common novice mistakes and recommends straightforward interface patterns to support first-time literature exploration."),
        Article("Scrolling Fatigue in Long Lists", "L. Freeman, Q. Zhou", 2020, "Researchers document fatigue effects in lengthy mobile lists and discuss breakpoints that can reduce repetitive gesture burden."),
        Article("Evaluating Backtracking Strategies", "D. Okafor, I. Mills", 2017, "The authors measure how quickly users relocate previously viewed records, emphasizing the role of interface structure in backtracking tasks."),
        Article("Visual Density and Decision Quality", "G. Rossi, V. Anand", 2016, "An experiment on visual density finds moderate spacing improves decision quality by balancing information richness and readability."),
        Article("Perceived Control in Navigation Systems", "K. Hassan, J. Moore", 2023, "This article explores user perception of control in different navigation models and correlates it with satisfaction during exploratory search."),
        Article("Temporal Patterns of Mobile Reading", "P. Evans, R. Choi", 2015, "The study tracks when and how long users read abstracts on phones, revealing frequent micro-sessions between other daily activities."),
        Article("Signal Detection in Result Ranking", "A. Diaz, N. Ibrahim", 2018, "This paper examines how ranking quality interacts with snippet clarity, influencing whether users continue browsing deeper into results."),
        Article("Interface Consistency in Experiments", "T. Mensah, O. Green", 2021, "Researchers argue that strict visual consistency is essential when comparing navigation paradigms to avoid confounding variables."),
        Article("Microinteractions in Research Apps", "E. Flores, H. Kim", 2019, "The article studies subtle motion and feedback cues and how they influence confidence when users save, open, or revisit records."),
        Article("Screen Position Memory Effects", "Y. Nakamura, A. Bello", 2022, "A lab experiment evaluates how accurately participants remember vertical item positions after completing relevance judgment tasks."),
        Article("Mobile Bibliographic Discovery Models", "R. Ahmed, C. Lim", 2017, "The authors propose a lightweight discovery model tailored for handheld devices where rapid filtering and minimal context switching are critical."),
        Article("Adaptive Snippet Presentation", "M. Rivera, J. Stone", 2020, "This study presents adaptive snippet lengths based on user behavior and shows potential gains in engagement and click precision."),
        Article("Human Factors in Academic Search", "S. Li, F. Martin", 2016, "The paper synthesizes human-factor constraints in mobile academic search, including attention limits, thumb reach, and interruption frequency."),
        Article("Re-finding Literature on Smartphones", "W. Grant, N. Aziz", 2021, "The research focuses on re-finding previously encountered papers and compares cue effectiveness across titles, authors, and positional memory."),
        Article("Navigation Landmarks for Long Lists", "D. Pereira, M. Yu", 2019, "Authors evaluate explicit landmarks in long lists and report improvements in orientation and reduced disorientation events."),
        Article("Empirical Methods for UX Comparison", "I. Novak, B. Trent", 2018, "This methodological paper outlines balanced protocols for comparing interface variants while maintaining reliable behavioral measurements."),
        Article("Scrolling Momentum and Attention", "H. Costa, L. Wang", 2023, "The study investigates momentum scrolling and notes that higher speed can reduce attention to mid-list items during exploratory browsing."),
        Article("Page Boundaries as Memory Anchors", "P. Osei, T. Morgan", 2024, "Findings suggest page boundaries may act as memory anchors that help users reconstruct where relevant records appeared."),
        Article("Minimalist Controls in Library UI", "A. Yilmaz, R. Dean", 2017, "The paper discusses minimalist control sets and their influence on learnability for users unfamiliar with advanced search systems."),
        Article("Task Success in Mobile Retrieval", "C. Johnson, E. Park", 2022, "A comparative study links clearer result previews and stable layout structure to higher task success in mobile retrieval tasks."),
        Article("Spatial Memory Metrics in HCI", "N. Idris, V. Clarke", 2025, "This article reviews practical metrics for spatial memory experiments and recommends combining recall accuracy with completion efficiency."),
    )
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AYMobileDigitalLibraryTheme {
        AppContent()
    }
}
