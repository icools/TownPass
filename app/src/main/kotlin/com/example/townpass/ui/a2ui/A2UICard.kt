package com.example.townpass.ui.a2ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray400
import com.example.townpass.ui.theme.TPGray50
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray800
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite

/**
 * A2UICard: Declarative Composable renderer for dynamic Taipei Open Data cards.
 * Renders dynamic metrics, pulsing live status, progress gauges, and expandable telemetry breakdowns.
 */
@Composable
fun A2UICard(
    card: A2UICardData,
    modifier: Modifier = Modifier,
    initialExpanded: Boolean = false,
    onActionClick: ((A2UIAction) -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TPWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, TPGray100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Header: Category + Live Pulse Badge + Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = TPPrimaryTeal.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = card.category.title,
                            color = TPPrimaryTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = card.department,
                        fontSize = 11.sp,
                        color = TPGray500,
                        maxLines = 1
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (card.isRealTime) {
                        LivePulseBadge(text = "即時")
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = card.lastUpdated,
                        fontSize = 11.sp,
                        color = TPGray400
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Title & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TPGray900
                    )
                    Text(
                        text = card.subtitle,
                        fontSize = 12.sp,
                        color = TPGray500,
                        lineHeight = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                A2UIStatusBadge(status = card.status, label = card.badgeText)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Highlighted Primary Metric Hero Box
            Surface(
                color = TPGray50,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, TPGray100.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = card.primaryLabel,
                            fontSize = 12.sp,
                            color = TPGray500,
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = card.primaryValue,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(card.status.colorHex)
                            )
                            if (card.primaryUnit.isNotBlank()) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = card.primaryUnit,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TPGray700,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                    }

                    // Status Indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(card.status.colorHex).copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = card.status.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(card.status.colorHex)
                        )
                    }
                }
            }

            // 4. Secondary Metrics Row
            if (card.metrics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                A2UIMetricRow(metrics = card.metrics)
            }

            // 5. Progress Bar
            if (card.progress != null) {
                Spacer(modifier = Modifier.height(10.dp))
                A2UIProgressBarView(
                    progress = card.progress,
                    color = Color(card.status.colorHex)
                )
            }

            // 6. Expandable Detail Section
            if (card.details.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { isExpanded = !isExpanded }
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isExpanded) "收起詳細測站/站點數據" else "查看即時明細 (${card.details.size} 站點)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TPPrimaryTeal
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "切換明細",
                        tint = TPPrimaryTeal
                    )
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(6.dp))
                        A2UIDetailList(details = card.details)
                    }
                }
            }

            // 7. Action Button
            if (card.action != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = TPGray100.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onActionClick?.invoke(card.action) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 9.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = card.action.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TPGray800
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "開啟連結",
                            modifier = Modifier.padding(top = 1.dp),
                            tint = TPGray700
                        )
                    }
                }
            }
        }
    }
}
