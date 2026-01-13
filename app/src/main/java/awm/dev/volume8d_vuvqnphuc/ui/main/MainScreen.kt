package awm.dev.volume8d_vuvqnphuc.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import awm.dev.volume8d_vuvqnphuc.ui.bottom_navigator.BottomNav
import awm.dev.volume8d_vuvqnphuc.ui.bottom_navigator.BottomNavigator
import awm.dev.volume8d_vuvqnphuc.ui.main.list_music.ListMusicScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.music.MusicScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.setting.SettingScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.volume.VolumeScreen
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen(
    currentIndexTab: Int = 0,
    changeIndexTab: (Int) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = currentIndexTab) { 4 }

    val selectedTab = when (currentIndexTab) {
        0 -> BottomNav.MUSIC
        1 -> BottomNav.LISTMUSIC
        2 -> BottomNav.VOLUME
        3 -> BottomNav.SETTING
        else -> BottomNav.MUSIC
    }

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
        // Nội dung các tab
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> MusicScreen(
                     onNavigateToList = {
                         // Switch to List Tab (Index 1)
                         // But MainScreen doesn't expose a way to change tab internally via state directly 
                         // unless we use the scope and pagerState or callback.
                         // Actually `changeIndexTab` is passed to MainScreen but not to the content.
                         // Wait, `HorizontalPager` is controlled by `pagerState`.
                         // We need to launch coroutine to scroll.
                         // Or better: pass a callback up OR use pagerState.scrollToPage
                         // Ideally MainScreen should handle this.
                         // BUT `changeIndexTab` is a callback from parent. MainScreen controls pagerState via LaunchedEffect.
                         // So we should call `changeIndexTab(1)`.
                         changeIndexTab(1)
                     }
                )
                1 -> ListMusicScreen(
                    onNavigateToPlayer = {
                        changeIndexTab(0)
                    }
                )
                2 -> VolumeScreen()
                3 -> SettingScreen()
            }
        }
        BottomNavigator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.systemBars),
            onClick = { tab ->
                val targetPage = when (tab) {
                    BottomNav.MUSIC -> 0
                    BottomNav.LISTMUSIC -> 1
                    BottomNav.VOLUME -> 2
                    BottomNav.SETTING -> 3
                }
                changeIndexTab(targetPage)
            },
            typeSelected = selectedTab,
        )
    }

    LaunchedEffect(currentIndexTab) {
        pagerState.animateScrollToPage(currentIndexTab)
    }
}

@Composable
@Preview(showSystemUi = true)
fun MainScreenP() {
    MainScreen(
        currentIndexTab = 0,
        changeIndexTab = {},
    )
}