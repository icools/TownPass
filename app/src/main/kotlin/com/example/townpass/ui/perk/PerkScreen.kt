package com.example.townpass.ui.perk

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPAccentOrange
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPPrimaryTealLight
import com.example.townpass.ui.theme.TPWhite

@Composable
fun PerkScreen(
    viewModel: PerkViewModel,
    onNavigateToAccount: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit
) {
    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "優惠",
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
                    IconButton(onClick = {
                        onNavigateToWeb("https://taipei-pass-service.vercel.app/ticket-wallet", "票夾")
                    }) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = "票夾",
                            tint = TPPrimaryTeal
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ticket Wallet Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPPrimaryTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToWeb("https://taipei-pass-service.vercel.app/ticket-wallet", "票夾")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalActivity,
                            contentDescription = null,
                            tint = TPWhite,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column {
                            Text(
                                text = "我的票夾與優惠券",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TPWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "點擊查看已收藏與兌換之票券",
                                fontSize = 13.sp,
                                color = TPWhite.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "推薦優惠與好康",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray900,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(viewModel.coupons) { coupon ->
                CouponCard(
                    coupon = coupon,
                    onClick = {
                        onNavigateToWeb("https://taipei-pass-service.vercel.app/coupon/", coupon.title)
                    }
                )
            }
        }
    }
}

@Composable
private fun CouponCard(
    coupon: CouponItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = coupon.storeName,
                    fontSize = 13.sp,
                    color = TPGray500
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(TPAccentOrange.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = coupon.discount,
                        color = TPAccentOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = coupon.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TPGray950
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = coupon.description,
                fontSize = 13.sp,
                color = TPGray700
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "有效期限至：${coupon.expireDate}",
                fontSize = 12.sp,
                color = TPGray500
            )
        }
    }
}
