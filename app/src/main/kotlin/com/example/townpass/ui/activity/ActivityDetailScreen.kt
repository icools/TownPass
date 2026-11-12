package com.example.townpass.ui.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.townpass.ui.components.TPLegacyDivider
import com.example.townpass.ui.components.TPPrimaryButton
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray800
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite
import com.example.townpass.util.DateTimeUtils

@Composable
fun ActivityDetailScreen(
    activityId: String,
    viewModel: ActivityViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit
) {
    val activity = viewModel.getActivity(activityId)

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "活動詳情",
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
        bottomBar = {
            if (!activity?.webUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TPPrimaryButton(
                        text = "前往活動網頁",
                        onClick = {
                            onNavigateToWeb(activity?.webUrl ?: "", activity?.title ?: "活動網頁")
                        }
                    )
                }
            }
        },
        containerColor = TPGray50
    ) { innerPadding ->
        if (activity == null) {
            Text(
                text = "找不到活動",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = activity.imageUrl,
                    contentDescription = activity.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = activity.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray950,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = TPPrimaryTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = DateTimeUtils.formatRange(activity.startDateTime, activity.endDateTime),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TPPrimaryTeal
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TPLegacyDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = activity.content,
                    fontSize = 15.sp,
                    color = TPGray800,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
