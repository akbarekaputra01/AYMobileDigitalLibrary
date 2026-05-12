package com.example.aymobiledigitallibrary.navigation
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.aymobiledigitallibrary.data.*
import com.example.aymobiledigitallibrary.model.*
import com.example.aymobiledigitallibrary.storage.*
import com.example.aymobiledigitallibrary.ui.screens.*
import com.example.aymobiledigitallibrary.util.*

@Composable fun AppNavigation(storage: SessionStorage, resultStorage: ResultStorage) {
 val nav=rememberNavController(); var pid by remember{ mutableStateOf(storage.getParticipantId()?:ParticipantIdGenerator.generate().also{storage.saveParticipantId(it)}) }
 NavHost(nav, Routes.WELCOME){
  composable(Routes.WELCOME){WelcomeScreen{nav.navigate(Routes.SESSION_SETUP)}}
  composable(Routes.SESSION_SETUP){SessionSetupScreen(pid){storage.saveParticipantInfo(it);nav.navigate(Routes.BROWSING_MODE_SETUP)}}
  composable(Routes.BROWSING_MODE_SETUP){BrowsingModeSetupScreen{storage.saveMode(it);storage.setBrowsingStart(ExperimentTimer.now());resultStorage.browsingStartTime=ExperimentTimer.now();resultStorage.logEvent(InteractionEvent(pid,it,ExperimentPhase.BROWSING,"browsing_started"));nav.navigate(Routes.LIBRARY_BROWSING)}}
  composable(Routes.LIBRARY_BROWSING){val m=storage.getMode()?:BrowsingMode.CONTINUOUS_LIST;LibraryBrowsingScreen(m,{item->resultStorage.logEvent(InteractionEvent(pid,m,ExperimentPhase.BROWSING,"item_tapped",item))},{v->resultStorage.logEvent(InteractionEvent(pid,m,ExperimentPhase.BROWSING,if(v=="next")"page_next_clicked" else "page_previous_clicked"))}){resultStorage.browsingEndTime=ExperimentTimer.now();resultStorage.browsingDurationMs=resultStorage.browsingEndTime-storage.getBrowsingStart();resultStorage.logEvent(InteractionEvent(pid,m,ExperimentPhase.BROWSING,"browsing_finished"));nav.navigate(Routes.SHORT_ACTIVITY)}}
  composable(Routes.SHORT_ACTIVITY){ShortActivityScreen(TaskRepository.distractorNumbers.take(8)){it.forEach{a->resultStorage.addDistractor(a);resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.SHORT_ACTIVITY,"distractor_answered",value=a.participantAnswer))};nav.navigate(Routes.GLOBAL_LOCATION_RECALL)}}
  composable(Routes.GLOBAL_LOCATION_RECALL){val targets=TargetSelector.globalTargets(pid);GlobalLocationRecallScreen(storage.getMode()?:BrowsingMode.CONTINUOUS_LIST,targets){it.forEach{a->resultStorage.addRecall(a);resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.GLOBAL_LOCATION,"global_location_answered",a.itemId,a.selectedAnswer))};nav.navigate(Routes.LOCAL_REGION_RECALL)}}
  composable(Routes.LOCAL_REGION_RECALL){val targets=TargetSelector.localTargets(pid);LocalRegionRecallScreen(targets){it.forEach{a->resultStorage.addRecall(a);resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.LOCAL_REGION,"local_region_answered",a.itemId,a.selectedAnswer))};nav.navigate(Routes.RELATIVE_ORDER_RECALL)}}
  composable(Routes.RELATIVE_ORDER_RECALL){val pairs=TaskRepository.relativeOrderPairs.map{LibraryRepository.items[it.first-1] to LibraryRepository.items[it.second-1]};RelativeOrderRecallScreen(pairs){it.forEach{a->resultStorage.addRecall(a);resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.RELATIVE_ORDER,"relative_order_answered",value=a.selectedAnswer))};nav.navigate(Routes.REFINDING_TASK)}}
  composable(Routes.REFINDING_TASK){val t=TaskRepository.refindingTargetIndices.map{LibraryRepository.items[it-1]};RefindingTaskScreen(storage.getMode()?:BrowsingMode.CONTINUOUS_LIST,LibraryRepository.items,t){it.forEach{r->resultStorage.addRefinding(r);resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.REFINDING,"refinding_target_found",r.targetItemId))};nav.navigate(Routes.WORKLOAD_QUESTIONNAIRE)}}
  composable(Routes.WORKLOAD_QUESTIONNAIRE){QuestionnaireScreen("Session Feedback",TaskRepository.workloadItems,TaskType.WORKLOAD){it.forEach{q->resultStorage.addQuestionnaire(q);resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.WORKLOAD,"questionnaire_answered",value=q.rating.toString()))};nav.navigate(Routes.USABILITY_QUESTIONNAIRE)}}
  composable(Routes.USABILITY_QUESTIONNAIRE){QuestionnaireScreen("Session Feedback",TaskRepository.usabilityItems,TaskType.USABILITY){it.forEach{q->resultStorage.addQuestionnaire(q)};resultStorage.logEvent(InteractionEvent(pid,storage.getMode(),ExperimentPhase.SUMMARY,"session_completed"));resultStorage.persistSnapshot(pid,storage.getParticipantInfo(),storage.getMode());nav.navigate(Routes.ADMIN_SUMMARY)}}
  composable(Routes.ADMIN_SUMMARY){AdminSummaryScreen(pid,storage.getMode()?:BrowsingMode.CONTINUOUS_LIST,resultStorage.getResult(pid,storage.getParticipantInfo(),storage.getMode()),onReset={storage.clearAll();resultStorage.clearAll();pid=ParticipantIdGenerator.generate().also{storage.saveParticipantId(it)};nav.navigate(Routes.WELCOME){popUpTo(0)}})}
 }
}
