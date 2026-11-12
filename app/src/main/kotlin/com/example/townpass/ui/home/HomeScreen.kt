package com.example.townpass.ui.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.townpass.R
import com.example.townpass.data.model.ActivityItem
import com.example.townpass.data.model.BannerItem
import com.example.townpass.data.model.SubscriptionItem
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPAccentOrange
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray400
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray800
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite
import com.example.townpass.util.DateTimeUtils

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAccount: () -> Unit,
    onNavigateToActivityList: () -> Unit,
    onNavigateToActivityDetail: (String) -> Unit,
    onNavigateToWeb: (String, String) -> Unit,
    onNavigateToMessage: () -> Unit
) {
    val banners by viewModel.banners.collectAsState()
    val activities by viewModel.activities.collectAsState()
    val subscriptions by viewModel.subscriptions.collectAsState()

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "首頁",
                showLogo = true,
                navigationIcon = {
                    IconButton(onClick = onNavigateToAccount) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "帳號",
                            tint = TPGray700
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToMessage) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "通知訊息",
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
            // 1. News Banner Pager
            if (banners.isNotEmpty()) {
                item {
                    BannerCarousel(
                        banners = banners,
                        onBannerClick = { banner ->
                            onNavigateToWeb(banner.webUrl, banner.title)
                        }
                    )
                }
            }

            // 2. Activity Info Section
            item {
                SectionHeader(
                    title = "活動訊息",
                    actionText = "更多",
                    onActionClick = onNavigateToActivityList
                )
            }

            item {
                ActivityCarousel(
                    activities = activities,
                    onActivityClick = { act -> onNavigateToActivityDetail(act.id) }
                )
            }

            // 3. City News Banner Card
            item {
                SectionHeader(title = "市政快訊")
                CityNewsCard(
                    onClick = onNavigateToMessage
                )
            }

            // 4. Subscriptions Section
            if (subscriptions.isNotEmpty()) {
                item {
                    SectionHeader(title = "我的訂閱")
                }

                items(subscriptions) { sub ->
                    SubscriptionCard(
                        subscription = sub,
                        onClick = {
                            if (!sub.webUrl.isNullOrBlank()) {
                                onNavigateToWeb(sub.webUrl, sub.title)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BannerCarousel(
    banners: List<BannerItem>,
    onBannerClick: (BannerItem) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) { page ->
            val banner = banners[page]
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick(banner) }
            ) {
                AsyncImage(
                    model = banner.imageUrl,
                    contentDescription = banner.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (banners.size > 1) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(banners.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) TPPrimaryTeal else TPGray400
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TPGray900
        )
        Spacer(modifier = Modifier.weight(1f))
        if (actionText != null && onActionClick != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onActionClick() }
            ) {
                Text(
                    text = actionText,
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
}

@Composable
private fun ActivityCarousel(
    activities: List<ActivityItem>,
    onActivityClick: (ActivityItem) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(activities) { act ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = TPWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .width(220.dp)
                    .clickable { onActivityClick(act) }
            ) {
                Column {
                    AsyncImage(
                        model = act.imageUrl,
                        contentDescription = act.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    )
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = act.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TPGray900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = DateTimeUtils.formatRange(act.startDateTime, act.endDateTime),
                            fontSize = 12.sp,
                            color = TPGray500
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CityNewsCard(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.city_news),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "台北市節慶商品物價變動",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TPGray950
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "為反映台北市端午節前節慶商品物價情況，訪查零售價格發布新聞稿供各界參考。",
                    fontSize = 13.sp,
                    color = TPGray700,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
private fun SubscriptionCard(
    subscription: SubscriptionItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(TPAccentOrange.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = subscription.title,
                        color = TPAccentOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = DateTimeUtils.formatDate(subscription.datetime),
                    fontSize = 12.sp,
                    color = TPGray500
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subscription.content,
                fontSize = 14.sp,
                color = TPGray800,
                lineHeight = 20.sp
            )
        }
    }
}
