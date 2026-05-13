package com.example.aymobiledigitallibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.storage.ResultStorage
import com.example.aymobiledigitallibrary.storage.SessionStorage
import com.example.aymobiledigitallibrary.ui.screens.*
import com.example.aymobiledigitallibrary.util.ParticipantIdGenerator

@Composable
fun AppNavigation(storage: SessionStorage, resultStorage: ResultStorage) {
    val nav = rememberNavController()
    var pid by remember { mutableStateOf(storage.getParticipantId() ?: ParticipantIdGenerator.generate().also { storage.saveParticipantId(it) }) }

    NavHost(navController = nav, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) { WelcomeScreen { nav.navigate(Routes.SESSION_SETUP) } }
        composable(Routes.SESSION_SETUP) { SessionSetupScreen(pid) { storage.saveParticipantInfo(it); resultStorage.logEvent(pid, storage.getMode(), "setup", "participant_info_submitted"); nav.navigate(Routes.BROWSING_MODE_SETUP) } }
        composable(Routes.BROWSING_MODE_SETUP) { BrowsingModeSetupScreen { storage.saveMode(it); resultStorage.logEvent(pid, it, "setup", "browsing_mode_selected"); nav.navigate(Routes.LIBRARY_BROWSING) } }
        composable(Routes.LIBRARY_BROWSING) { LibraryBrowsingScreen(storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST, { resultStorage.logEvent(pid, storage.getMode(), "browsing", "browsing_started") }, { resultStorage.logEvent(pid, storage.getMode(), "browsing", "browsing_finished") }) { nav.navigate(Routes.SHORT_ACTIVITY) } }
        composable(Routes.SHORT_ACTIVITY) { ShortActivityScreen(pid) { resultStorage.saveDistractorResult(it); resultStorage.logEvent(pid, storage.getMode(), "short_activity", "short_activity_finished"); nav.navigate(Routes.GLOBAL_LOCATION_RECALL) } }
        composable(Routes.GLOBAL_LOCATION_RECALL) { GlobalLocationRecallScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST) { resultStorage.saveGlobalRecallResults(it); it.forEach { r -> resultStorage.logEvent(pid, storage.getMode(), "global_recall", "global_recall_answer_submitted", r.targetItemId) }; nav.navigate(Routes.LOCAL_CONTEXT_RECALL) } }
        composable(Routes.LOCAL_CONTEXT_RECALL) { LocalContextRecallScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST) { resultStorage.saveLocalContextRecallResults(it); it.forEach { r -> resultStorage.logEvent(pid, storage.getMode(), "local_context", "local_context_answer_submitted", r.targetItemId) }; nav.navigate(Routes.REFINDING_TASK) } }
        composable(Routes.REFINDING_TASK) { RefindingTaskScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST, LibraryRepository.items) { resultStorage.saveRefindingResults(it); it.forEach { r -> if (r.wrongClickCount > 0) resultStorage.logEvent(pid, storage.getMode(), "refinding", "refinding_wrong_click", r.targetItemId, r.wrongClickCount.toString()); if (r.success) resultStorage.logEvent(pid, storage.getMode(), "refinding", "refinding_success", r.targetItemId) }; nav.navigate(Routes.QUESTIONNAIRE) } }
        composable(Routes.QUESTIONNAIRE) {
            QuestionnaireScreen(listOf("It was easy to complete the tasks.", "I felt confident finding materials.", "The browsing method felt natural.")) {
                resultStorage.saveQuestionnaireResponses(it); resultStorage.logEvent(pid, storage.getMode(), "questionnaire", "questionnaire_submitted")
                nav.navigate(Routes.THANK_YOU)
            }
        }
        composable(Routes.THANK_YOU) { ThankYouScreen { nav.navigate(Routes.ADMIN_SUMMARY) } }
        composable(Routes.ADMIN_SUMMARY) { AdminSummaryScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST, resultStorage, storage.getParticipantInfo(), onReset = { storage.clearAll(); resultStorage.clearAll(); pid = ParticipantIdGenerator.generate().also { storage.saveParticipantId(it) }; nav.navigate(Routes.WELCOME) }) }
    }
}
