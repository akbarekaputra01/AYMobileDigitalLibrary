package com.example.aymobiledigitallibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.*
import com.example.aymobiledigitallibrary.model.BrowsingMode
import com.example.aymobiledigitallibrary.storage.SessionStorage
import com.example.aymobiledigitallibrary.ui.screens.*
import com.example.aymobiledigitallibrary.util.ParticipantIdGenerator

@Composable
fun AppNavigation(storage: SessionStorage) {
    val nav = rememberNavController()
    val generatedId = remember { storage.getParticipantId() ?: ParticipantIdGenerator.generate().also { storage.saveParticipantId(it) } }
    NavHost(navController = nav, startDestination = Routes.Welcome) {
        composable(Routes.Welcome) { WelcomeScreen { nav.navigate(Routes.Session) } }
        composable(Routes.Session) { SessionSetupScreen(generatedId) { storage.saveParticipantInfo(it); nav.navigate(Routes.Mode) } }
        composable(Routes.Mode) { BrowsingModeSetupScreen { storage.saveMode(it); nav.navigate(Routes.Browse) } }
        composable(Routes.Browse) { LibraryBrowsingScreen(storage.getMode() ?: BrowsingMode.CONTINUOUS_LIST) { nav.navigate(Routes.Admin) } }
        composable(Routes.Admin) { AdminPlaceholderScreen(storage.getParticipantId(), storage.getMode()) }
    }
}
