package awm.dev.volume8d_vuvqnphuc.ui.Instructions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import awm.dev.volume8d_vuvqnphuc.R
import awm.dev.volume8d_vuvqnphuc.data.model.InstructionData
import awm.dev.volume8d_vuvqnphuc.utils.system.clickableOnce

@Composable
fun InstructionsScreen(
    onBack: () -> Unit = {}
) {
    BackHandler {
        onBack()
    }
    val instructions = listOf(
        InstructionData(
            title = stringResource(R.string.select_music_list),
            description = stringResource(R.string.to_use_the_app_select_list_music_to_access_audio_files_on_your_device_please_grant_the_necessary_permissions_for_the_app_to_function_smoothly),
            iconRes = R.drawable.ic_list_music
        ),
        InstructionData(
            title = stringResource(R.string.manage_delete_files),
            description = stringResource(R.string.in_the_music_list_you_can_delete_files_by_swiping_left_to_reveal_the_delete_icon_if_you_want_to_play_music_simply_click_on_the_item),
            iconRes = R.drawable.ic_delete
        ),
        InstructionData(
            title = stringResource(R.string.usage_experience),
            description = stringResource(R.string.select_the_music_tab_to_control_the_music_player_the_app_supports_looping_a_song_indefinitely_until_you_turn_it_off),
            iconRes = R.drawable.ic_music
        ),
        InstructionData(
            title = stringResource(R.string.premium_features),
            description = stringResource(R.string.the_app_supports_bass_boost_clear_voice_and_especially_8d_audio_mode_with_customizable_speed),
            iconRes = R.drawable.ic_volume
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFEE0979), Color(0xFFFF6A00)),
                    start = Offset(0f, 0f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .clickableOnce { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_screen),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.instructions),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Instructions List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
            ) {
                items(instructions) { item ->
                    InstructionCard(item)
                }
            }
        }
    }
}

@Composable
fun InstructionCard(data: InstructionData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(data.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = data.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = data.description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}
