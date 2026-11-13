package com.example.townpass.ui.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.filled.Widgets
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.R
import com.example.townpass.data.model.CityServiceItem
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
fun CityServiceScreen(
    viewModel: ServiceViewModel,
    onNavigateToAccount: () -> Unit,
    onNavigateToEditServices: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit,
    onNavigateToOpenData: () -> Unit = {},
    onNavigateToYouBike: () -> Unit = {}
) {
    val pinnedServices by viewModel.pinnedServices.collectAsState()

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "服務",
                showLogo = true,
                navigationIcon = {
                    IconButton(onClick = onNavigateToAccount) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "帳號",
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
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 我的服務 Header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "我的服務",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TPGray900
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onNavigateToEditServices() }
                    ) {
                        Text(
                            text = "更多",
                            fontSize = 14.sp,
                            color = TPGray700
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TPGray700,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Pinned Services Grid (4 items per row)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        val chunked = pinnedServices.chunked(4)
                        chunked.forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { service ->
                                    ServiceGridItem(
                                        service = service,
                                        onClick = {
                                            if (service.title.contains("YouBike", ignoreCase = true) || service.id.contains("youbike", ignoreCase = true)) {
                                                onNavigateToYouBike()
                                            } else {
                                                onNavigateToWeb(service.destinationUrl, service.title)
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                repeat(4 - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Official Service Card Banner
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "官方服務",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray900,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                OfficialServiceCard(
                    onClick = {
                        onNavigateToWeb("https://taipei-pass-service.vercel.app/1999", "1999網路電話")
                    }
                )
            }

            // Taipei Open Data Dashboard Banner
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onNavigateToOpenData() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TPPrimaryTeal.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = "Open Data",
                                tint = TPPrimaryTeal,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "台北即時 Open Data 儀表板",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TPGray900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                com.example.townpass.ui.a2ui.LivePulseBadge(text = "LIVE")
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "串接 12 項市政即時開放資料 (YouBike、捷運、停車、AQI)",
                                fontSize = 12.sp,
                                color = TPGray500,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "查看",
                            tint = TPGray400
                        )
                    }
                }
            }

            // Trending Services
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "熱門服務",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray900,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TrendingServiceCard(
                    title = "台北市立動物園",
                    subtitle = "線上購票入園、設施導覽及活動資訊",
                    onClick = {
                        onNavigateToWeb("https://www.zoo.gov.taipei", "台北市立動物園")
                    }
                )
                TrendingServiceCard(
                    title = "有話要說 (陳情系統)",
                    subtitle = "快速陳情反應市政大小事與環境通報",
                    onClick = {
                        onNavigateToWeb("https://taipei-pass-service.vercel.app/citizen-report/", "有話要說")
                    }
                )
            }
        }
    }
}

@Composable
private fun ServiceGridItem(
    service: CityServiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(TPPrimaryTealLight)
        ) {
            Icon(
                imageVector = getServiceIcon(service.id),
                contentDescription = service.title,
                tint = TPPrimaryTeal,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = service.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TPGray950,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

private fun getServiceIcon(id: String): ImageVector {
    return when (id) {
        "1999" -> Icons.Default.Phone
        "district" -> Icons.Default.Description
        "talk" -> Icons.Default.Feedback
        "call_num" -> Icons.Default.Call
        "voting" -> Icons.Default.HowToVote
        "zoo" -> Icons.Default.NaturePeople
        "library" -> Icons.Default.LocalLibrary
        "water" -> Icons.Default.WaterDamage
        else -> Icons.Default.Widgets
    }
}

@Composable
private fun OfficialServiceCard(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.line_1999),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "1999 網路通話",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray950
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "免付費網路電話，直通台北市民當家熱線",
                    fontSize = 13.sp,
                    color = TPGray700
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TPGray400
            )
        }
    }
}

@Composable
private fun TrendingServiceCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TPGray950
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = TPGray700
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TPGray400
            )
        }
    }
}
