package com.example.townpass.ui.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.townpass.ui.components.TPLegacyDivider
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray800
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.util.DateTimeUtils

@Composable
fun MessageDetailScreen(
    messageId: String,
    viewModel: MessageViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit
) {
    val message = viewModel.getMessage(messageId)

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "訊息內容",
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
        if (message == null) {
            Text(
                text = "找不到訊息",
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
                Text(
                    text = message.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray950
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = DateTimeUtils.formatDateTime(message.datetime),
                    fontSize = 13.sp,
                    color = TPGray500
                )
                Spacer(modifier = Modifier.height(16.dp))
                TPLegacyDivider()
                Spacer(modifier = Modifier.height(16.dp))

                if (!message.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                message.richText?.forEach { rich ->
                    if (!rich.text.isNullOrBlank()) {
                        Text(
                            text = rich.text,
                            fontSize = 15.sp,
                            color = TPGray800,
                            lineHeight = 24.sp
                        )
                    }
                    if (!rich.url.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = rich.url,
                            fontSize = 14.sp,
                            color = TPPrimaryTeal,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                onNavigateToWeb(rich.url, message.title)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
