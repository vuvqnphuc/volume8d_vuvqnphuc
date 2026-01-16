package awm.dev.volume8d_vuvqnphuc.ui.splash

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import awm.dev.volume8d_vuvqnphuc.AppMainViewModel
import awm.dev.volume8d_vuvqnphuc.R
import awm.dev.volume8d_vuvqnphuc.remote_config.OpenADS
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SplashScreen(
    nextScreen: () -> Unit = {},
) {
    val viewModel: AppMainViewModel = hiltViewModel()
    val activity = LocalActivity.current
    val remoteConfigManager = viewModel.remoteConfigManager
    LaunchedEffect(Unit) {
        if (activity == null) {
            delay(3000)
            nextScreen()
            return@LaunchedEffect
        }
        if (remoteConfigManager.isCheckADS()) {
            val adUnitId = remoteConfigManager.setOpenApp()
            if (adUnitId.isNotEmpty()) {
                var adShown = false
                OpenADS.loadOpenAd(activity, adUnitId, onAdLoaded = {
                    if (!adShown) {
                        adShown = true
                        OpenADS.showOpenAd(activity) {
                            nextScreen()
                        }
                    }
                }, onAdFailed = {
                    nextScreen()
                })
                // Timeout to ensure we don't get stuck
                delay(8000)
                if (!adShown) {
                    nextScreen()
                }
            } else {
                delay(3000)
                nextScreen()
            }
        } else {
            delay(3000)
            nextScreen()
        }
    }
    val animLoad by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_load)
    )

    val preloadAnimLoad by animateLottieCompositionAsState(
        animLoad,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFEE0979), Color(0xFFFF6A00)),
                    start = Offset(0f, 0f)
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = animLoad,
            progress = { preloadAnimLoad },
            modifier = Modifier.size(150.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = R.drawable.logo_app,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(100))
            )
            Text(
                stringResource(R.string.app_name),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(R.string.initialized_ads),
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SplashScreenP() {
    SplashScreen()
}