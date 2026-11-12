package com.example.townpass.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.components.TPLegacyDivider
import com.example.townpass.ui.components.TPPrimaryButton
import com.example.townpass.ui.components.TPSettingTile
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray400
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPPrimaryTealLight
import com.example.townpass.ui.theme.TPWhite

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMessage: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit
) {
    val account by viewModel.account.collectAsState()

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "帳戶",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = TPGray700
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "設定",
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
            // Profile Card Header
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(TPPrimaryTealLight)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = TPPrimaryTeal,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = account.realName.ifBlank { "台北市民" },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TPGray950
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = account.phoneNo ?: "尚未設定門號",
                                    fontSize = 14.sp,
                                    color = TPGray700
                                )
                            }
                            IconButton(onClick = onNavigateToSettings) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "設定",
                                    tint = TPGray500
                                )
                            }
                        }
                    }
                }
            }

            // Menu Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column {
                        TPSettingTile(
                            title = "訊息",
                            leadingIcon = Icons.Default.Notifications,
                            onClick = onNavigateToMessage
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "歷程與授權管理",
                            leadingIcon = Icons.Default.LockReset,
                            onClick = {
                                onNavigateToWeb("https://id.taipei/tpcd/about/privacy", "歷程與授權管理")
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "功能教學與常見問題",
                            leadingIcon = Icons.Default.HelpOutline,
                            onClick = {
                                onNavigateToWeb("https://id.taipei/tpcd/about/faq", "功能教學")
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "意見回饋",
                            leadingIcon = Icons.Default.Feedback,
                            onClick = onNavigateToFeedback
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "版本",
                            content = viewModel.appVersion,
                            showChevron = false
                        )
                    }
                }
            }

            // Footer
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "臺北市政府 智慧城市 推動專案",
                        fontSize = 12.sp,
                        color = TPGray500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Copyright © Taipei City Government. All Rights Reserved.",
                        fontSize = 11.sp,
                        color = TPGray400
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
