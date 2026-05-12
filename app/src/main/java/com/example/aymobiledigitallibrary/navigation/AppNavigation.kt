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
        composable(Routes.SESSION_SETUP) { SessionSetupScreen(pid) { storage.saveParticipantInfo(it); nav.navigate(Routes.BROWSING_MODE_SETUP) } }
        composable(Routes.BROWSING_MODE_SETUP) { BrowsingModeSetupScreen { storage.saveMode(it); nav.navigate(Routes.LIBRARY_BROWSING) } }
        composable(Routes.LIBRARY_BROWSING) { LibraryBrowsingScreen(storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST, {}, {}) { nav.navigate(Routes.SHORT_ACTIVITY) } }
        composable(Routes.SHORT_ACTIVITY) { ShortActivityScreen(pid) { resultStorage.saveDistractorResult(it); nav.navigate(Routes.GLOBAL_LOCATION_RECALL) } }
        composable(Routes.GLOBAL_LOCATION_RECALL) { GlobalLocationRecallScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST) { resultStorage.saveGlobalRecallResults(it); nav.navigate(Routes.LOCAL_CONTEXT_RECALL) } }
        composable(Routes.LOCAL_CONTEXT_RECALL) { LocalContextRecallScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST) { resultStorage.saveLocalContextRecallResults(it); nav.navigate(Routes.REFINDING_TASK) } }
        composable(Routes.REFINDING_TASK) { RefindingTaskScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST, LibraryRepository.items) { resultStorage.saveRefindingResults(it); nav.navigate(Routes.COMPLETION) } }
        composable(Routes.COMPLETION) { CompletionScreen { nav.navigate(Routes.ADMIN_SUMMARY) } }
        composable(Routes.ADMIN_SUMMARY) { AdminSummaryScreen(pid, storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST, resultStorage, onReset = { storage.clearAll(); resultStorage.clearAll(); pid = ParticipantIdGenerator.generate().also { storage.saveParticipantId(it) }; nav.navigate(Routes.WELCOME) }) }
    }
}
