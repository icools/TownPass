package com.example.townpass.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite

enum class MainTab(
    val index: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    SERVICE(0, "服務", Icons.Filled.Widgets, Icons.Outlined.Widgets),
    HOME(1, "首頁", Icons.Filled.Home, Icons.Outlined.Home),
    PERK(2, "優惠", Icons.Filled.ConfirmationNumber, Icons.Outlined.ConfirmationNumber),
    BILL(3, "帳務", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet);

    companion object {
        fun fromIndex(index: Int): MainTab {
            return entries.firstOrNull { it.index == index } ?: HOME
        }
    }
}

@Composable
fun TPBottomNavigationBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = TPWhite,
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        MainTab.entries.forEach { tab ->
            val isSelected = tab == currentTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TPPrimaryTeal,
                    selectedTextColor = TPPrimaryTeal,
                    unselectedIconColor = TPGray500,
                    unselectedTextColor = TPGray500,
                    indicatorColor = TPWhite
                )
            )
        }
    }
}
