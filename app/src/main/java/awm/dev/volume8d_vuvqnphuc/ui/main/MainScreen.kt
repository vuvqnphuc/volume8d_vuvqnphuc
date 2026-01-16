package awm.dev.volume8d_vuvqnphuc.ui.main

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import awm.dev.volume8d_vuvqnphuc.AppMainViewModel
import awm.dev.volume8d_vuvqnphuc.remote_config.BannerADS
import awm.dev.volume8d_vuvqnphuc.remote_config.InterADS
import awm.dev.volume8d_vuvqnphuc.ui.bottom_navigator.BottomNav
import awm.dev.volume8d_vuvqnphuc.ui.bottom_navigator.BottomNavigator
import awm.dev.volume8d_vuvqnphuc.ui.main.list_music.ListMusicScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.music.MusicScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.setting.SettingScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.volume.VolumeScreen
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen(
    currentIndexTab: Int = 0,
    changeIndexTab: (Int) -> Unit,
    onNavigateToLanguage: () -> Unit = {},
    appViewModel: AppMainViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = currentIndexTab) { 4 }

    val selectedTab = when (currentIndexTab) {
        0 -> BottomNav.MUSIC
        1 -> BottomNav.LISTMUSIC
        2 -> BottomNav.VOLUME
        3 -> BottomNav.SETTING
        else -> BottomNav.MUSIC
    }

    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        if (appViewModel.isCheckADS()) {
            activity?.let {
                InterADS.loadInterstitialAd(it, appViewModel.getInterListMusicMenu())
            }
        }
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
                        changeIndexTab(1)
                    }
                )

                1 -> ListMusicScreen(
                    onNavigateToPlayer = {
                        changeIndexTab(0)
                    }
                )

                2 -> VolumeScreen()
                3 -> SettingScreen(
                    onLanguageClick = onNavigateToLanguage,
                    onShareClick = {
                        val sendIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(
                                android.content.Intent.EXTRA_TEXT,
                                "https://play.google.com/store/apps/details?id=${context.packageName}"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(
                            android.content.Intent.createChooser(
                                sendIntent,
                                null
                            )
                        )
                    },
                    onRateClick = {
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse("market://details?id=${context.packageName}")
                        )
                        try {
                            context.startActivity(intent)
                        } catch (e: android.content.ActivityNotFoundException) {
                            context.startActivity(
                                android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                                )
                            )
                        }
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .background(Color.Transparent)
        ) {
            BottomNavigator(
                modifier = Modifier.fillMaxWidth(),
                onClick = { tab ->
                    val targetPage = when (tab) {
                        BottomNav.MUSIC -> 0
                        BottomNav.LISTMUSIC -> 1
                        BottomNav.VOLUME -> 2
                        BottomNav.SETTING -> 3
                    }
                    if (tab == BottomNav.LISTMUSIC && appViewModel.isCheckADS()) {
                        activity?.let {
                            InterADS.showInterstitialAd(it) {
                                changeIndexTab(targetPage)
                                // Preload next
                                InterADS.loadInterstitialAd(
                                    it,
                                    appViewModel.getInterListMusicMenu()
                                )
                            }
                        } ?: changeIndexTab(targetPage)
                    } else {
                        changeIndexTab(targetPage)
                    }
                },
                typeSelected = selectedTab,
            )
            Spacer(modifier = Modifier.height(0.5.dp).fillMaxWidth().background(color = Color.Black))
            if (appViewModel.isCheckADS()) {
                val adUnitId = when (selectedTab) {
                    BottomNav.MUSIC -> appViewModel.getBannerMusic()
                    BottomNav.LISTMUSIC -> appViewModel.getBannerListMusic()
                    BottomNav.VOLUME -> appViewModel.getBannerVolume()
                    BottomNav.SETTING -> appViewModel.getBannerSetting()
                }
                if (adUnitId.isNotEmpty()) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                    ) {
                        BannerADS(
                            adUnitId = adUnitId,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Log.e("log1", "adUnitId:$adUnitId ")
                }
            }
        }
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
        onNavigateToLanguage = {}
    )
}