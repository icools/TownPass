package com.example.townpass.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.components.TPPrimaryButton
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPAccentRed
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPPrimaryTeal

@Composable
fun BasicInfoEditScreen(
    fieldKey: String,
    title: String,
    currentValue: String,
    viewModel: SettingViewModel,
    onNavigateBack: () -> Unit
) {
    var text by remember { mutableStateOf(currentValue) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val keyboardType = when (fieldKey) {
        "phoneNo" -> KeyboardType.Phone
        "email" -> KeyboardType.Email
        "birthday" -> KeyboardType.Number
        else -> KeyboardType.Text
    }

    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "編輯$title",
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
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    errorMessage = null
                },
                label = { Text(title) },
                singleLine = true,
                isError = errorMessage != null,
                supportingText = {
                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = TPAccentRed)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TPPrimaryTeal,
                    focusedLabelColor = TPPrimaryTeal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            TPPrimaryButton(
                text = "儲存變更",
                onClick = {
                    val success = viewModel.updateAccountField(fieldKey, text)
                    if (success) {
                        onNavigateBack()
                    } else {
                        errorMessage = when (fieldKey) {
                            "idNo" -> "身分證字號格式不正確"
                            "email" -> "電子信箱格式不正確"
                            "phoneNo" -> "手機號碼格式不正確 (需為 09 開頭 10 碼)"
                            else -> "輸入內容不符合規範"
                        }
                    }
                }
            )
        }
    }
}
