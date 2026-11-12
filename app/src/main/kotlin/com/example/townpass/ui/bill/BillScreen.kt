package com.example.townpass.ui.bill

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.R
import com.example.townpass.data.model.PopularBillItem
import com.example.townpass.ui.components.TPTopAppBar
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPGray950
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPPrimaryTealLight
import com.example.townpass.ui.theme.TPWhite

@Composable
fun BillScreen(
    viewModel: BillViewModel,
    onNavigateToAccount: () -> Unit,
    onNavigateToWeb: (String, String) -> Unit
) {
    Scaffold(
        topBar = {
            TPTopAppBar(
                title = "帳務",
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
                        onNavigateToWeb("https://taipei-pass-service.vercel.app/fee-payment/", "繳費紀錄")
                    }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "紀錄",
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Scan / Pay Header Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNavigateToWeb("https://taipei-pass-service.vercel.app/fee-payment/", "智慧支付")
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.scan),
                            contentDescription = "掃描繳費",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "台北智慧支付 (pay.taipei)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TPGray950
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "掃描帳單三段式條碼或 QR Code 快速繳納",
                                fontSize = 13.sp,
                                color = TPGray700
                            )
                        }
                    }
                }
            }

            // Popular Bills Header
            item {
                Text(
                    text = "熱門查繳項目",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TPGray900
                )
            }

            // Grid of Bill items (4 per row)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TPWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        val chunked = viewModel.bills.chunked(4)
                        chunked.forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { bill ->
                                    BillGridItem(
                                        item = bill,
                                        onClick = {
                                            onNavigateToWeb(bill.uri, bill.name)
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
        }
    }
}

@Composable
private fun BillGridItem(
    item: PopularBillItem,
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
                imageVector = getBillIcon(item.id),
                contentDescription = item.name,
                tint = TPPrimaryTeal,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TPGray950,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

private fun getBillIcon(id: String): ImageVector {
    return when (id) {
        "fee-car" -> Icons.Default.DirectionsCar
        "fee-water" -> Icons.Default.WaterDrop
        "fee-tax" -> Icons.Default.AccountBalance
        "fee-school" -> Icons.Default.School
        "fee-housing" -> Icons.Default.Apartment
        "fee-hospital" -> Icons.Default.LocalHospital
        else -> Icons.Default.Receipt
    }
}
