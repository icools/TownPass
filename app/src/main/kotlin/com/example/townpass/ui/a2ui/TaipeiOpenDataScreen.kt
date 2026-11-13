package com.example.townpass.ui.a2ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncDisabled
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray400
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray800
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaipeiOpenDataScreen(
    viewModel: TaipeiOpenDataViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit,
    onNavigateToYouBike: () -> Unit = {}
) {
    val cards by viewModel.filteredCards.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isAutoRefresh by viewModel.isAutoRefreshEnabled.collectAsState()
    val lastRefreshedTime by viewModel.lastRefreshedTime.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // Spin animation when manually refreshing
    val infiniteTransition = rememberInfiniteTransition(label = "refreshSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "台北即時 Open Data",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TPGray900
                        )
                        Text(
                            text = "A2UI 動態即時儀表板 (${cards.size} 項來源)",
                            fontSize = 11.sp,
                            color = TPGray500
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = TPGray800
                        )
                    }
                },
                actions = {
                    // Auto-refresh toggle icon
                    IconButton(onClick = { viewModel.toggleAutoRefresh() }) {
                        Icon(
                            imageVector = if (isAutoRefresh) Icons.Default.Sync else Icons.Default.SyncDisabled,
                            contentDescription = "切換自動更新",
                            tint = if (isAutoRefresh) TPPrimaryTeal else TPGray400
                        )
                    }

                    // Manual refresh button
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "立即更新",
                            modifier = if (isRefreshing) Modifier.rotate(rotation) else Modifier,
                            tint = TPPrimaryTeal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TPWhite)
            )
        },
        containerColor = TPGray50
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Telemetry Broadcast Status Bar
            Surface(
                color = TPPrimaryTeal.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LivePulseBadge(text = if (isAutoRefresh) "即時串接中" else "已暫停")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAutoRefresh) "每 6 秒自動更新" else "手動更新模式",
                            fontSize = 11.sp,
                            color = TPGray700
                        )
                    }
                    Text(
                        text = "更新時間: $lastRefreshedTime",
                        fontSize = 11.sp,
                        color = TPGray500
                    )
                }
            }

            // 2. Search Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "搜尋 YouBike / 捷運 / 空氣 / 停車場 / 氣象...",
                            fontSize = 13.sp,
                            color = TPGray400
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜尋",
                            tint = TPGray500,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "清除搜尋",
                                    tint = TPGray500,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TPWhite,
                        unfocusedContainerColor = TPWhite,
                        focusedBorderColor = TPPrimaryTeal,
                        unfocusedBorderColor = TPGray100
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 3. Category Filter Chips (Horizontal Scrollable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                A2UICategory.entries.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCategory(category) },
                        label = {
                            Text(
                                text = category.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TPPrimaryTeal,
                            selectedLabelColor = TPWhite,
                            containerColor = TPWhite,
                            labelColor = TPGray700
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) TPPrimaryTeal else TPGray100
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4. Dynamic A2UI Cards List
            if (cards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "查無符合的開放資料項目",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TPGray700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "請嘗試更換搜尋關鍵字或分類",
                            fontSize = 12.sp,
                            color = TPGray400
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = cards,
                        key = { it.id }
                    ) { cardData ->
                        A2UICard(
                            card = cardData,
                            initialExpanded = false,
                            onActionClick = { action ->
                                if (cardData.sourceKey == "TAIPEI_YOUBIKE" || action.actionType == "OPEN_YOUBIKE_MAP") {
                                    onNavigateToYouBike()
                                } else if (action.url != null) {
                                    onNavigateToWeb(action.url, cardData.title)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
