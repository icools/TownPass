package com.example.townpass.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.components.TPLegacyDivider
import com.example.townpass.ui.components.TPPrimaryButton
import com.example.townpass.ui.components.TPSettingSectionHeader
import com.example.townpass.ui.components.TPSettingSwitchTile
import com.example.townpass.ui.components.TPSettingTile
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBasicInfo: () -> Unit
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val quickLoginEnabled by viewModel.quickLoginEnabled.collectAsState()

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "設定",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = TPGray700
                        )
                    }
                }
            )
        },
        containerColor = TPGray50
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 個人資料
            item {
                TPSettingSectionHeader(title = "個人資料")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column {
                        TPSettingTile(
                            title = "基本資料",
                            onClick = onNavigateToBasicInfo
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "語言",
                            content = "繁體中文",
                            showChevron = false
                        )
                    }
                }
            }

            // 偏好設定
            item {
                Spacer(modifier = Modifier.height(10.dp))
                TPSettingSectionHeader(title = "偏好設定")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column {
                        TPSettingTile(
                            title = "APP起始頁面",
                            content = "首頁",
                            showChevron = false
                        )
                    }
                }
            }

            // 通知
            item {
                Spacer(modifier = Modifier.height(10.dp))
                TPSettingSectionHeader(title = "通知設定")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column {
                        TPSettingSwitchTile(
                            title = "訊息通知提醒",
                            checked = notificationsEnabled,
                            onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                        )
                    }
                }
            }

            // 安全性
            item {
                Spacer(modifier = Modifier.height(10.dp))
                TPSettingSectionHeader(title = "安全性")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column {
                        TPSettingSwitchTile(
                            title = "快速登入 (生物辨識)",
                            checked = quickLoginEnabled,
                            onCheckedChange = { viewModel.setQuickLoginEnabled(it) }
                        )
                    }
                }
            }

            // Footer / Logout
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "最後登入時間：${viewModel.lastLoginTime}",
                        fontSize = 13.sp,
                        color = TPPrimaryTeal
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TPPrimaryButton(
                        text = "登出帳號",
                        onClick = onNavigateBack
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
