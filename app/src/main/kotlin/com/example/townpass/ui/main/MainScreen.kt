package com.example.townpass.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.townpass.ui.bill.BillScreen
import com.example.townpass.ui.bill.BillViewModel
import com.example.townpass.ui.components.MainTab
import com.example.townpass.ui.components.TPBottomNavigationBar
import com.example.townpass.ui.home.HomeScreen
import com.example.townpass.ui.home.HomeViewModel
import com.example.townpass.ui.perk.PerkScreen
import com.example.townpass.ui.perk.PerkViewModel
import com.example.townpass.ui.service.CityServiceScreen
import com.example.townpass.ui.a2ui.TaipeiOpenDataViewModel
import com.example.townpass.ui.service.ServiceViewModel
import com.example.townpass.ui.theme.TPGray50

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    serviceViewModel: ServiceViewModel,
    perkViewModel: PerkViewModel,
    billViewModel: BillViewModel,
    openDataViewModel: TaipeiOpenDataViewModel,
    onNavigateToAccount: () -> Unit,
    onNavigateToActivityList: () -> Unit,
    onNavigateToActivityDetail: (String) -> Unit,
    onNavigateToEditServices: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit,
    onNavigateToMessage: () -> Unit,
    onNavigateToOpenData: () -> Unit
) {
    val currentTab by mainViewModel.currentTab.collectAsState()

    Scaffold(
        bottomBar = {
            TPBottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { mainViewModel.selectTab(it) }
            )
        },
        containerColor = TPGray50
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.SERVICE -> CityServiceScreen(
                    viewModel = serviceViewModel,
                    onNavigateToAccount = onNavigateToAccount,
                    onNavigateToEditServices = onNavigateToEditServices,
                    onNavigateToWeb = onNavigateToWeb,
                    onNavigateToOpenData = onNavigateToOpenData
                )
                MainTab.HOME -> HomeScreen(
                    viewModel = homeViewModel,
                    openDataViewModel = openDataViewModel,
                    onNavigateToAccount = onNavigateToAccount,
                    onNavigateToActivityList = onNavigateToActivityList,
                    onNavigateToActivityDetail = onNavigateToActivityDetail,
                    onNavigateToWeb = onNavigateToWeb,
                    onNavigateToMessage = onNavigateToMessage,
                    onNavigateToOpenData = onNavigateToOpenData
                )
                MainTab.PERK -> PerkScreen(
                    viewModel = perkViewModel,
                    onNavigateToAccount = onNavigateToAccount,
                    onNavigateToWeb = onNavigateToWeb
                )
                MainTab.BILL -> BillScreen(
                    viewModel = billViewModel,
                    onNavigateToAccount = onNavigateToAccount,
                    onNavigateToWeb = onNavigateToWeb
                )
            }
        }
    }
}
