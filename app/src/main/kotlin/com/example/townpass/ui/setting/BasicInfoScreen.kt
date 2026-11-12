package com.example.townpass.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.townpass.ui.components.TPLegacyDivider
import com.example.townpass.ui.components.TPSettingTile
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPWhite

@Composable
fun BasicInfoScreen(
    viewModel: SettingViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String, String, String) -> Unit
) {
    val account by viewModel.account.collectAsState()

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "基本資料",
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
                .padding(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        TPSettingTile(
                            title = "姓名",
                            content = account.realName,
                            onClick = {
                                onNavigateToEdit("realName", "姓名", account.realName)
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "身分證",
                            content = account.idNo,
                            onClick = {
                                onNavigateToEdit("idNo", "身分證", account.idNo)
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "生日",
                            content = account.birthday ?: "未設定",
                            onClick = {
                                onNavigateToEdit("birthday", "生日", account.birthday ?: "")
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "電子信箱",
                            content = account.email ?: "未設定",
                            onClick = {
                                onNavigateToEdit("email", "電子信箱", account.email ?: "")
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "手機門號",
                            content = account.phoneNo ?: "未設定",
                            onClick = {
                                onNavigateToEdit("phoneNo", "手機門號", account.phoneNo ?: "")
                            }
                        )
                        TPLegacyDivider()
                        TPSettingTile(
                            title = "通訊地址",
                            content = account.residentAddress ?: "未設定",
                            onClick = {
                                onNavigateToEdit("residentAddress", "通訊地址", account.residentAddress ?: "")
                            }
                        )
                    }
                }
            }
        }
    }
}
