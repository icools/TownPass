package com.example.townpass.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.components.TPPrimaryButton
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPPrimaryTealLight
import com.example.townpass.ui.theme.TPWhite

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel,
    onNavigateBack: () -> Unit
) {
    val categories = listOf("操作問題", "功能建議", "資料錯誤", "其他")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var contentText by remember { mutableStateOf("") }
    val isSubmitted by viewModel.isSubmitted.collectAsState()

    if (isSubmitted) {
        AlertDialog(
            onDismissRequest = {
                viewModel.reset()
                onNavigateBack()
            },
            title = { Text("感謝您的回饋") },
            text = { Text("我們已收到您的寶貴意見，將持續改進 TownPass 服務品質！") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.reset()
                    onNavigateBack()
                }) {
                    Text("確定", color = TPPrimaryTeal)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "意見回饋",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(
                text = "請選擇回饋類型",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TPGray950
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) TPPrimaryTeal else TPGray100)
                            .clickable { selectedCategory = cat }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TPWhite else TPGray700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "詳細說明",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TPGray950
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contentText,
                onValueChange = { contentText = it },
                placeholder = { Text("請輸入您遇到的問題或建議事項...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TPPrimaryTeal,
                    focusedLabelColor = TPPrimaryTeal
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            TPPrimaryButton(
                text = "送出回饋",
                onClick = {
                    viewModel.submitFeedback(selectedCategory, contentText)
                },
                enabled = contentText.isNotBlank()
            )
        }
    }
}
