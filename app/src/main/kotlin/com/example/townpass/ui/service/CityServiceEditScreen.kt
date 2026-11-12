package com.example.townpass.ui.service

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.townpass.ui.components.TPLegacyDivider
import com.example.townpass.ui.components.TPSettingSwitchTile
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray700

@Composable
fun CityServiceEditScreen(
    viewModel: ServiceViewModel,
    onNavigateBack: () -> Unit
) {
    val allServices by viewModel.allServices.collectAsState()

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "編輯我的服務",
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
            items(allServices) { service ->
                TPSettingSwitchTile(
                    title = service.title,
                    checked = service.isPinned,
                    onCheckedChange = { viewModel.togglePin(service.id) }
                )
                TPLegacyDivider()
            }
        }
    }
}
