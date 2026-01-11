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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import awm.dev.volume8d_vuvqnphuc.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    typeSelected: BottomNav = BottomNav.MUSIC,
    onClick: (BottomNav) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3.87f)
            .background(color = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5.5f)
                .align(Alignment.BottomCenter)
                .background(color = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                ItemBottomNav(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = if (typeSelected == BottomNav.MUSIC) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_background,
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
                    icon = if (typeSelected == BottomNav.LISTMUSIC) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_background,
                    title = "List Music",
                    selected = typeSelected == BottomNav.LISTMUSIC,
                    onClick = {
                        onClick(BottomNav.LISTMUSIC)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                ItemBottomNav(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = if (typeSelected == BottomNav.VOLUME) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_background,
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
                    icon = if (typeSelected == BottomNav.SETTING) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_background,
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
    BottomNav(
        typeSelected = BottomNav.MUSIC,
        onClick = {}
    )
}

@Keep
enum class BottomNav {
    MUSIC, LISTMUSIC, CAMERA, VOLUME, SETTING
}