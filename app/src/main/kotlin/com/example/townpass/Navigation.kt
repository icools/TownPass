package com.example.townpass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.townpass.data.repository.TownPassRepository
import com.example.townpass.data.repository.TownPassRepositoryImpl
import com.example.townpass.ui.account.AccountScreen
import com.example.townpass.ui.account.AccountViewModel
import com.example.townpass.ui.activity.ActivityDetailScreen
import com.example.townpass.ui.activity.ActivityListScreen
import com.example.townpass.ui.activity.ActivityViewModel
import com.example.townpass.ui.bill.BillViewModel
import com.example.townpass.ui.feedback.FeedbackScreen
import com.example.townpass.ui.feedback.FeedbackViewModel
import com.example.townpass.ui.home.HomeViewModel
import com.example.townpass.ui.main.MainScreen
import com.example.townpass.ui.main.MainViewModel
import com.example.townpass.ui.message.MessageDetailScreen
import com.example.townpass.ui.message.MessageScreen
import com.example.townpass.ui.message.MessageViewModel
import com.example.townpass.ui.perk.PerkViewModel
import com.example.townpass.ui.service.CityServiceEditScreen
import com.example.townpass.ui.service.ServiceViewModel
import com.example.townpass.ui.setting.BasicInfoEditScreen
import com.example.townpass.ui.setting.BasicInfoScreen
import com.example.townpass.ui.setting.SettingScreen
import com.example.townpass.ui.setting.SettingViewModel
import com.example.townpass.ui.webview.TPWebViewScreen

@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val repository: TownPassRepository = remember { TownPassRepositoryImpl(context) }

    val mainViewModel = remember { MainViewModel() }
    val homeViewModel = remember { HomeViewModel(repository) }
    val serviceViewModel = remember { ServiceViewModel(repository) }
    val perkViewModel = remember { PerkViewModel() }
    val billViewModel = remember { BillViewModel(repository) }
    val accountViewModel = remember { AccountViewModel(repository) }
    val messageViewModel = remember { MessageViewModel(repository) }
    val activityViewModel = remember { ActivityViewModel(repository) }
    val settingViewModel = remember { SettingViewModel(repository) }
    val feedbackViewModel = remember { FeedbackViewModel() }

    val backStack = rememberNavBackStack(MainNavKey)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<MainNavKey> {
                MainScreen(
                    mainViewModel = mainViewModel,
                    homeViewModel = homeViewModel,
                    serviceViewModel = serviceViewModel,
                    perkViewModel = perkViewModel,
                    billViewModel = billViewModel,
                    onNavigateToAccount = { backStack.add(AccountNavKey) },
                    onNavigateToActivityList = { backStack.add(ActivityListNavKey) },
                    onNavigateToActivityDetail = { id -> backStack.add(ActivityDetailNavKey(id)) },
                    onNavigateToEditServices = { backStack.add(ServiceEditNavKey) },
                    onNavigateToWeb = { url, title -> backStack.add(WebViewNavKey(url, title)) },
                    onNavigateToMessage = { backStack.add(MessageNavKey) }
                )
            }
            entry<AccountNavKey> {
                AccountScreen(
                    viewModel = accountViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToSettings = { backStack.add(SettingNavKey) },
                    onNavigateToMessage = { backStack.add(MessageNavKey) },
                    onNavigateToFeedback = { backStack.add(FeedbackNavKey) },
                    onNavigateToWeb = { url, title -> backStack.add(WebViewNavKey(url, title)) }
                )
            }
            entry<SettingNavKey> {
                SettingScreen(
                    viewModel = settingViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToBasicInfo = { backStack.add(BasicInfoNavKey) }
                )
            }
            entry<BasicInfoNavKey> {
                BasicInfoScreen(
                    viewModel = settingViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToEdit = { key, title, currentVal ->
                        backStack.add(BasicInfoEditNavKey(key, title, currentVal))
                    }
                )
            }
            entry<BasicInfoEditNavKey> { navKey ->
                BasicInfoEditScreen(
                    fieldKey = navKey.fieldKey,
                    title = navKey.title,
                    currentValue = navKey.currentValue,
                    viewModel = settingViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            entry<MessageNavKey> {
                MessageScreen(
                    viewModel = messageViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToDetail = { id -> backStack.add(MessageDetailNavKey(id)) }
                )
            }
            entry<MessageDetailNavKey> { navKey ->
                MessageDetailScreen(
                    messageId = navKey.messageId,
                    viewModel = messageViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToWeb = { url, title -> backStack.add(WebViewNavKey(url, title)) }
                )
            }
            entry<ActivityListNavKey> {
                ActivityListScreen(
                    viewModel = activityViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToDetail = { id -> backStack.add(ActivityDetailNavKey(id)) }
                )
            }
            entry<ActivityDetailNavKey> { navKey ->
                ActivityDetailScreen(
                    activityId = navKey.activityId,
                    viewModel = activityViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToWeb = { url, title -> backStack.add(WebViewNavKey(url, title)) }
                )
            }
            entry<ServiceEditNavKey> {
                CityServiceEditScreen(
                    viewModel = serviceViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            entry<FeedbackNavKey> {
                FeedbackScreen(
                    viewModel = feedbackViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            entry<WebViewNavKey> { navKey ->
                TPWebViewScreen(
                    url = navKey.url,
                    title = navKey.title,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
