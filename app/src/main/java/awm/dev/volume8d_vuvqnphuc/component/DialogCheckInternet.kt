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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

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
                .background(Color.White.copy(alpha = 0.15f))
                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), RoundedCornerShape(28.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFFFCC00),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Internet Connection",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please check your internet connection and try again.",
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
                    onClick = onTryAgain
                ) {
                    Text(
                        text = "Try Again",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                ButtonCommonContent(
                    paddingHorizontal = 0,
                    paddingVertical = 12,
                    modifier = Modifier.weight(1f),
                    onClick = onGoToSetting
                ) {
                    Text(
                        text = "Go to Settings",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
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