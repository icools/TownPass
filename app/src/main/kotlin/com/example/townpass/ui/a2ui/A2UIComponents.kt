package com.example.townpass.ui.a2ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.townpass.ui.theme.TPGray100
import com.example.townpass.ui.theme.TPGray400
import com.example.townpass.ui.theme.TPGray500
import com.example.townpass.ui.theme.TPGray700
import com.example.townpass.ui.theme.TPGray800
import com.example.townpass.ui.theme.TPGray900
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite

/**
 * Animated pulsing dot representing real-time telemetry streaming in A2UI.
 */
@Composable
fun LivePulseBadge(
    modifier: Modifier = Modifier,
    text: String = "LIVE 即時"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        color = Color(0xFFE6F4EA),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size((7 * scale).dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F9D58).copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                color = Color(0xFF0D652D),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Color-coded status badge for A2UI cards.
 */
@Composable
fun A2UIStatusBadge(
    status: A2UIStatus,
    label: String,
    modifier: Modifier = Modifier
) {
    val baseColor = Color(status.colorHex)
    val bgColor = baseColor.copy(alpha = 0.12f)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            color = baseColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * Dynamic metrics grid displayed inside A2UI cards.
 */
@Composable
fun A2UIMetricRow(
    metrics: List<A2UIMetric>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(TPGray100.copy(alpha = 0.6f))
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        metrics.take(4).forEach { metric ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = metric.label,
                    fontSize = 11.sp,
                    color = TPGray500,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = metric.value,
                    fontSize = 13.sp,
                    color = Color(metric.status.colorHex),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Dynamic progress bar for capacities, quotas, and saturation in A2UI cards.
 */
@Composable
fun A2UIProgressBarView(
    progress: A2UIProgress,
    color: Color = TPPrimaryTeal,
    modifier: Modifier = Modifier
) {
    val fraction = (progress.current / progress.max.coerceAtLeast(1f)).coerceIn(0f, 1f)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = progress.label,
                fontSize = 11.sp,
                color = TPGray500,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${progress.displayPercent}%",
                fontSize = 12.sp,
                color = TPGray700,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = TPGray100
        )
    }
}

/**
 * Detail rows list inside expandable A2UI cards.
 */
@Composable
fun A2UIDetailList(
    details: List<A2UIDetailRow>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(TPWhite)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        details.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = row.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TPGray900
                        )
                        if (row.tag != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = TPGray100,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = row.tag,
                                    fontSize = 10.sp,
                                    color = TPGray700,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                    if (row.subtitle.isNotBlank()) {
                        Text(
                            text = row.subtitle,
                            fontSize = 11.sp,
                            color = TPGray500
                        )
                    }
                }

                Text(
                    text = row.value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(row.status.colorHex)
                )
            }
        }
    }
}
