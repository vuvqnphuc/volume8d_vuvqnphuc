package awm.dev.volume8d_vuvqnphuc.ui.bottom_navigator

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import awm.dev.volume8d_vuvqnphuc.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BottomNavigator(
    modifier: Modifier = Modifier,
    typeSelected: BottomNav = BottomNav.MUSIC,
    onClick: (BottomNav) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(5f)
            .background(color = Color(0xFFFF6A00))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                ItemBottomNav(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = if (typeSelected == BottomNav.MUSIC) R.drawable.ic_select_music else R.drawable.ic_music,
                    title = "Music",
                    selected = typeSelected == BottomNav.MUSIC,
                    onClick = {
                        onClick(BottomNav.MUSIC)
                    }
                )

                ItemBottomNav(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = if (typeSelected == BottomNav.LISTMUSIC) R.drawable.ic_select_list_music else R.drawable.ic_list_music,
                    title = "List Music",
                    selected = typeSelected == BottomNav.LISTMUSIC,
                    onClick = {
                        onClick(BottomNav.LISTMUSIC)
                    }
                )

                ItemBottomNav(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = if (typeSelected == BottomNav.VOLUME) R.drawable.ic_select_volume else R.drawable.ic_volume,
                    title = "Volume",
                    selected = typeSelected == BottomNav.VOLUME,
                    onClick = {
                        onClick(BottomNav.VOLUME)
                    }
                )

                ItemBottomNav(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = if (typeSelected == BottomNav.SETTING) R.drawable.ic_select_setting else R.drawable.ic_setting,
                    title = "Setting",
                    selected = typeSelected == BottomNav.SETTING,
                    onClick = {
                        onClick(BottomNav.SETTING)
                    }
                )
            }
        }
    }
}


@Composable
@Preview
fun BottomNavP() {
    BottomNavigator(
        typeSelected = BottomNav.MUSIC,
        onClick = {}
    )
}

@Keep
enum class BottomNav {
    MUSIC, LISTMUSIC, VOLUME, SETTING
}