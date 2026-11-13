package com.example.townpass.ui.youbike

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.data.model.YouBikeFilterMode
import com.example.townpass.data.model.YouBikeStation
import com.example.townpass.data.model.YouBikeStats
import com.example.townpass.ui.a2ui.LivePulseBadge
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
fun YouBikeScreen(
    viewModel: YouBikeViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val stations by viewModel.filteredStations.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val districts by viewModel.districts.collectAsState()
    val selectedDistrict by viewModel.selectedDistrict.collectAsState()
    val filterMode by viewModel.filterMode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStation by viewModel.selectedStation.collectAsState()
    val isMapView by viewModel.isMapView.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lastUpdatedTime by viewModel.lastUpdatedTime.collectAsState()

    // Spin animation for refresh button
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "YouBike 2.0 即時動態",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TPGray900
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            LivePulseBadge(text = "LIVE")
                        }
                        Text(
                            text = "台北市開放資料 • 即時數量與車位地圖",
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
                    // View Mode Switch (Map vs List)
                    IconButton(onClick = { viewModel.toggleViewMode() }) {
                        Icon(
                            imageVector = if (isMapView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.Map,
                            contentDescription = if (isMapView) "切換清單" else "切換地圖",
                            tint = TPPrimaryTeal
                        )
                    }

                    // Refresh Button
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "重新載入",
                            modifier = if (isLoading) Modifier.rotate(rotation) else Modifier,
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
            // 1. Stats Banner: 數量 & 剩餘指標
            YouBikeStatsOverview(
                stats = stats,
                lastUpdatedTime = lastUpdatedTime
            )

            // 2. Search & Filter Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TPWhite)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "搜尋站點名稱或路名 (例: 捷運科技大樓, 市政府...)",
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
                                    contentDescription = "清除",
                                    tint = TPGray500,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TPGray50,
                        unfocusedContainerColor = TPGray50,
                        focusedBorderColor = TPPrimaryTeal,
                        unfocusedBorderColor = TPGray100
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mode Filters (全部 / 可借車優先 / 可還位優先)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    YouBikeFilterMode.entries.forEach { mode ->
                        val isSelected = filterMode == mode
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setFilterMode(mode) },
                            label = {
                                Text(
                                    text = mode.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TPPrimaryTeal,
                                selectedLabelColor = TPWhite,
                                containerColor = TPGray100,
                                labelColor = TPGray700
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) TPPrimaryTeal else TPGray100
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // District Filters Horizontal Scroll
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    districts.forEach { dist ->
                        val isSelected = selectedDistrict == dist
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setDistrict(dist) },
                            label = {
                                Text(
                                    text = dist,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TPGray800,
                                selectedLabelColor = TPWhite,
                                containerColor = TPGray50,
                                labelColor = TPGray700
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) TPGray800 else TPGray100
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }

            // 3. Main Content: Map View or List View
            Box(modifier = Modifier.fillMaxSize()) {
                if (isMapView) {
                    // Map View
                    YouBikeMapView(
                        stations = stations,
                        selectedStation = selectedStation,
                        onStationClick = { st -> viewModel.selectStation(st) },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Floating Card for Selected Station
                    androidx.compose.animation.AnimatedVisibility(
                        visible = selectedStation != null,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        selectedStation?.let { station ->
                            YouBikeFloatingStationCard(
                                station = station,
                                onToggleFavorite = { viewModel.toggleFavorite(station.sno) },
                                onNavigateMap = {
                                    val uri = Uri.parse("geo:${station.latitude},${station.longitude}?q=${station.latitude},${station.longitude}(${station.name})")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                } else {
                    // List View
                    if (stations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "查無符合條件的 YouBike 站點",
                                fontSize = 14.sp,
                                color = TPGray500
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                Text(
                                    text = "共找到 ${stations.size} 個站點",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TPGray500,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            items(items = stations, key = { it.sno }) { station ->
                                YouBikeListItemCard(
                                    station = station,
                                    onToggleFavorite = { viewModel.toggleFavorite(station.sno) },
                                    onClick = {
                                        viewModel.selectStation(station)
                                        viewModel.toggleViewMode()
                                    },
                                    onNavigateMap = {
                                        val uri = Uri.parse("geo:${station.latitude},${station.longitude}?q=${station.latitude},${station.longitude}(${station.name})")
                                        val intent = Intent(Intent.ACTION_VIEW, uri)
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 數量 & 剩餘統計橫幅卡片
 */
@Composable
private fun YouBikeStatsOverview(
    stats: YouBikeStats,
    lastUpdatedTime: String
) {
    Surface(
        color = TPWhite,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, TPGray100)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "全台北即時借還數據",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray900
                )
                Text(
                    text = "更新時間: $lastUpdatedTime",
                    fontSize = 11.sp,
                    color = TPGray400
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TPGray50)
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. 可借車輛 (剩餘數量)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🚲 可借車輛", fontSize = 11.sp, color = TPGray500)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.totalAvailableBikes}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF059669)
                    )
                }

                // 2. 可還車位 (剩餘空格)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🅿️ 可還空位", fontSize = 11.sp, color = TPGray500)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.totalEmptySpaces}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TPPrimaryTeal
                    )
                }

                // 3. 營運站點總數
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📍 營運站點", fontSize = 11.sp, color = TPGray500)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.totalStations}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TPGray900
                    )
                }

                // 4. 正常營運率
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "⚡ 營運率", fontSize = 11.sp, color = TPGray500)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stats.activeRate,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2563EB)
                    )
                }
            }
        }
    }
}

/**
 * 地圖模式中浮動顯示的選定站點卡片
 */
@Composable
private fun YouBikeFloatingStationCard(
    station: YouBikeStation,
    onToggleFavorite: () -> Unit,
    onNavigateMap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, TPGray100),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Name + District + Favorite Star
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = TPPrimaryTeal.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = station.district,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TPPrimaryTeal,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = station.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TPGray900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = station.address,
                        fontSize = 12.sp,
                        color = TPGray500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (station.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "收藏",
                        tint = if (station.isFavorite) Color(0xFFF59E0B) else TPGray400
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 數量 & 剩餘指標盒
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 可借
                Surface(
                    color = Color(0xFFE6F4EA),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "可借車輛", fontSize = 11.sp, color = Color(0xFF0D652D))
                        Text(
                            text = "${station.availableBikes} 輛",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F9D58)
                        )
                    }
                }

                // 可還
                Surface(
                    color = Color(0xFFE0F2FE),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "可還空位", fontSize = 11.sp, color = Color(0xFF0369A1))
                        Text(
                            text = "${station.emptySpaces} 格",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }
                }

                // 總車格
                Surface(
                    color = TPGray100,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "總車格數", fontSize = 11.sp, color = TPGray700)
                        Text(
                            text = "${station.totalSpaces} 格",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TPGray900
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { station.bikeRate },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF0F9D58),
                trackColor = Color(0xFF0284C7).copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Button
            Button(
                onClick = onNavigateMap,
                colors = ButtonDefaults.buttonColors(containerColor = TPPrimaryTeal),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "以地圖導航至此站",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * 清單模式中的站點卡片
 */
@Composable
private fun YouBikeListItemCard(
    station: YouBikeStation,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    onNavigateMap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, TPGray100),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = TPPrimaryTeal.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = station.district,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TPPrimaryTeal,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = station.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TPGray900,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (station.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "收藏",
                            tint = if (station.isFavorite) Color(0xFFF59E0B) else TPGray400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onNavigateMap,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "導航",
                            tint = TPGray500,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = station.address,
                fontSize = 12.sp,
                color = TPGray500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics row: 可借 vs 可還 vs 總數
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(TPGray50)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "可借: ", fontSize = 12.sp, color = TPGray500)
                    Text(
                        text = "${station.availableBikes} 輛",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F9D58)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "可還: ", fontSize = 12.sp, color = TPGray500)
                    Text(
                        text = "${station.emptySpaces} 格",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "總格數: ", fontSize = 12.sp, color = TPGray500)
                    Text(
                        text = "${station.totalSpaces}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TPGray700
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { station.bikeRate },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color(0xFF0F9D58),
                trackColor = Color(0xFF0284C7).copy(alpha = 0.3f)
            )
        }
    }
}
