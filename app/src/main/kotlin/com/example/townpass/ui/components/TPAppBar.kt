package com.example.townpass.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.R
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TPTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    showLogo: Boolean = false,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showLogo) {
                    Image(
                        painter = painterResource(id = R.drawable.app_bar_logo),
                        contentDescription = "TownPass Logo",
                        modifier = Modifier
                            .height(28.dp)
                            .padding(end = 8.dp)
                    )
                }
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TPGray950
                )
            }
        },
        navigationIcon = {
            navigationIcon?.invoke()
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TPWhite,
            titleContentColor = TPGray950
        ),
        modifier = modifier
    )
}
