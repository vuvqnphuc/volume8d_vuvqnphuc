package awm.dev.volume8d_vuvqnphuc.ui.main.setting

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import awm.dev.volume8d_vuvqnphuc.AppMainViewModel
import awm.dev.volume8d_vuvqnphuc.R
import awm.dev.volume8d_vuvqnphuc.component.BannerAdView

@Composable
fun SettingScreen(
    onLanguageClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onRateClick: () -> Unit = {},
    appMainViewModel: AppMainViewModel= hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val BannerSetting = appMainViewModel.getBannerSetting()
        if (appMainViewModel.isCheckADS() && BannerSetting.isNotEmpty()) {
            BannerAdView(
                adUnitId = BannerSetting,
                modifier = Modifier.fillMaxWidth()
            )
        }
        // Upper Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.settings),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Group: Preferences
                Text(
                    text = stringResource(R.string.preferences),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                )
                
                SettingCard {
                    SettingItem(
                        icon = Icons.Default.Menu,
                        title = stringResource(R.string.language),
                        subtitle = stringResource(R.string.select_your_preferred_language),
                        onClick = onLanguageClick
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Group: Support & Community
                Text(
                    text = stringResource(R.string.support_community),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                )

                SettingCard {
                    Column {
                        SettingItem(
                            icon = Icons.Default.Share,
                            title = stringResource(R.string.share_app),
                            subtitle = stringResource(R.string.invite_your_friends_to_use),
                            onClick = onShareClick
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.White.copy(alpha = 0.1f)
                        )
                        SettingItem(
                            icon = Icons.Default.Star,
                            title = stringResource(R.string.rate_app),
                            subtitle = stringResource(R.string.support_us_with_5_stars),
                            onClick = onRateClick
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                
                // App Version Info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                    // hiển thị đúng phiên bản của ứng dụng
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                    val versionName = packageInfo.versionName
                    Text(
                        text = "Version $versionName",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun SettingCard(content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Background
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        // Arrow
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEE0979)
@Composable
fun SettingScreenPreview() {
    SettingScreen()
}