package awm.dev.volume8d_vuvqnphuc.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import awm.dev.volume8d_vuvqnphuc.R

@Composable
fun DialogCheckInternet(
    onDismissRequest: () -> Unit,
    onTryAgain: () -> Unit,
    onGoToSetting: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFEE0979), Color(0xFFFF6A00)),
                        start = Offset(0f, 0f)
                    )
                )
                .border(
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    RoundedCornerShape(28.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_internet_connection),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.please_check_your_internet_connection_and_try_again),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonCommonCan(
                    paddingHorizontal = 0,
                    paddingVertical = 12,
                    modifier = Modifier.weight(1f),
                    onClick = onGoToSetting
                ) {
                    Text(
                        text = stringResource(R.string.go_to_settings),
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFEE0979)
fun DialogCheckInternetP() {
    DialogCheckInternet(
        onDismissRequest = {},
        onTryAgain = {},
        onGoToSetting = {}
    )
}