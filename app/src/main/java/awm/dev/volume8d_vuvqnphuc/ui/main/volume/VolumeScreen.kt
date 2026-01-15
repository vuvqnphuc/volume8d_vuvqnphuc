package awm.dev.volume8d_vuvqnphuc.ui.main.volume

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import awm.dev.volume8d_vuvqnphuc.R

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import awm.dev.volume8d_vuvqnphuc.AppMainViewModel
import awm.dev.volume8d_vuvqnphuc.component.BannerAdView
import awm.dev.volume8d_vuvqnphuc.ui.main.MainViewModel

@Composable
fun VolumeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    appMainViewModel: AppMainViewModel=hiltViewModel()
) {
    val masterVolume by viewModel.masterVolume.collectAsState()
    val bassBoost by viewModel.bassStrength.collectAsState()
    val vocalBoost by viewModel.vocalStrength.collectAsState()
    val is8DEnabled by viewModel.is8DEnabled.collectAsState()
    val surroundSound by viewModel.surroundStrength.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val BannerVolume = appMainViewModel.getBannerVolume()
        if (appMainViewModel.isCheckADS() && BannerVolume.isNotEmpty()) {
            BannerAdView(
                adUnitId = BannerVolume,
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
                text = stringResource(R.string.audio_booster),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            MasterVolumeControl(
                volume = masterVolume,
                onVolumeChange = { viewModel.setMasterVolume(it) }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.sound_enhancements),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // âm trầm
            EffectSlider(
                label = stringResource(R.string.bass_boost),
                iconResId = R.drawable.ic_volume,
                value = bassBoost,
                onValueChange = { viewModel.setBassStrength(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // độ rõ âm thanh
            EffectSlider(
                label = stringResource(R.string.sound_transmission),
                iconResId = R.drawable.ic_music,
                value = vocalBoost,
                onValueChange = { viewModel.setVocalStrength(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Special 8D Audio Switch bật công tắc này thì âm thanh di chuyển chậm trái phải như âm thanh 8D như hiện nay
            Special8DControl(
                isEnabled = is8DEnabled,
                onToggle = { viewModel.set8DEnabled(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // chỉ sử dụng khi công tắc bật
            EffectSlider(
                label = "8D Surround",
                iconResId = R.drawable.ic_head_phones,
                value = surroundSound,
                onValueChange = { viewModel.setSurroundStrength(it) }
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun Special8DControl(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (isEnabled) Color(0xFFFFD700).copy(alpha = 0.2f) else Color.White.copy(
                                alpha = 0.1f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_head_phones),
                        contentDescription = "8D Audio",
                        tint = if (isEnabled) Color(0xFFFFD700) else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "8D Surround Mode",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isEnabled) "Active Experience" else "Immersive sound",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFEE0979),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
fun MasterVolumeControl(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    val percentage = (volume * 100).toInt()
    
    Card(
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Percentage Indicator
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$percentage%",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (percentage > 100) Color.Red else Color.White
                    )
                    Text(
                        text = if (percentage == 0) stringResource(R.string.mute) else if (percentage > 100) stringResource(
                            R.string.boost
                        ) else stringResource(R.string.volume),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Slider
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..2f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mute", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                Text("100%", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                Text("200%", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EffectSlider(
    label: String,
    iconResId: Int,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(value * 100).toInt()}%",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun VolumeScreenPreview() {
    VolumeScreen()
}