package awm.dev.volume8d_vuvqnphuc.ui.language

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import awm.dev.volume8d_vuvqnphuc.AppMainViewModel
import awm.dev.volume8d_vuvqnphuc.component.ButtonCommonClick
import awm.dev.volume8d_vuvqnphuc.component.DialogLoadADS
import awm.dev.volume8d_vuvqnphuc.data.local.LANG
import awm.dev.volume8d_vuvqnphuc.data.local.Language
import awm.dev.volume8d_vuvqnphuc.remote_config.BannerADS
import awm.dev.volume8d_vuvqnphuc.remote_config.InterADS
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LanguageScreen(
    listLanguage: List<Language>,
    language: Language?,
    onChangeLanguage: (LANG?) -> Unit,
    appViewModel: AppMainViewModel = hiltViewModel(),
) {

    var selectLanguage by remember {
        mutableStateOf(language)
    }

    var isReadyNextScreen: Boolean by remember {
        mutableStateOf(false)
    }

    // Infinite pulse animation
    val scale by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = if (isReadyNextScreen) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()
    var isShowDialogLoadAds by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (appViewModel.isCheckADS()) {
            activity?.let {
                InterADS.loadInterstitialAd(it, appViewModel.getInterDoneLanguage())
            }
        }
        delay(0) //to do
        isReadyNextScreen = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFEE0979), Color(0xFFFF6A00)),
                    start = Offset(0f, 0f)
                )
            )
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 4.dp)
                    .weight(1f)
                    .basicMarquee(),
                text = when (selectLanguage?.code) {
                    LANG.AR -> "اللغة"                // Arabic
                    LANG.CS -> "Jazyk"                // Czech
                    LANG.DE -> "Sprache"              // German
                    LANG.ES -> "Idioma"                // Spanish
                    LANG.FR -> "Langue"               // French
                    LANG.HI -> "भाषा"                // Hindi
                    LANG.IN -> "Bahasa"               // Indonesian
                    LANG.IT -> "Lingua"               // Italian
                    LANG.JA -> "言語"                 // Japanese
                    LANG.KO -> "언어"                 // Korean
                    LANG.MS -> "Bahasa"               // Malay
                    LANG.PHI -> "Wika"                // Filipino
                    LANG.PL -> "Język"                // Polish
                    LANG.PT -> "Idioma"               // Portuguese
                    LANG.RU -> "Язык"                 // Russian
                    LANG.TR -> "Dil"                  // Turkish
                    LANG.BR -> "Idioma"               // Brazilian Portuguese
                    LANG.VI -> "Ngôn ngữ"             // Vietnamese
                    LANG.ZH -> "语言"                 // Chinese Simplified
                    LANG.TH -> "ภาษา"                // Thai
                    LANG.TW -> "語言"                 // Chinese Traditional
                    else -> "Language"                // English (default)
                },

                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            )

            ButtonCommonClick(
                paddingHorizontal = 8,
                paddingVertical = 4,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                onClick = {
                    if (isReadyNextScreen) {
                        if (appViewModel.isCheckADS()) {
                            activity?.let {
                                if (InterADS.isAdReady()) {
                                    InterADS.showInterstitialAd(it) {
                                        onChangeLanguage(selectLanguage?.code)
                                    }
                                } else {
                                    isShowDialogLoadAds = true
                                    scope.launch {
                                        delay(2000)
                                        isShowDialogLoadAds = false
                                        InterADS.showInterstitialAd(it) {
                                            onChangeLanguage(selectLanguage?.code)
                                        }
                                    }
                                }
                            } ?: onChangeLanguage(selectLanguage?.code)
                        } else {
                            onChangeLanguage(selectLanguage?.code)
                        }
                    }
                }
            ) {
                Text(
                    text = when (selectLanguage?.code) {
                        LANG.AR -> "تم"           // Arabic
                        LANG.CS -> "Hotovo"       // Czech
                        LANG.DE -> "Fertig"       // German
                        LANG.ES -> "Hecho"        // Spanish
                        LANG.FR -> "Terminé"      // French
                        LANG.HI -> "पूर्ण"         // Hindi
                        LANG.IN -> "Selesai"      // Indonesian
                        LANG.IT -> "Fatto"        // Italian
                        LANG.JA -> "完了"           // Japanese
                        LANG.KO -> "완료"           // Korean
                        LANG.MS -> "Selesai"      // Malay
                        LANG.PHI -> "Tapos"       // Filipino
                        LANG.PL -> "Gotowe"       // Polish
                        LANG.PT -> "Concluído"    // Portuguese
                        LANG.RU -> "Готово"       // Russian
                        LANG.TR -> "Bitti"        // Turkish
                        LANG.BR -> "Concluído"    // Brazilian Portuguese (same as PT)
                        LANG.VI -> "Xong"         // Vietnamese
                        LANG.ZH -> "完成"           // Chinese Simplified
                        LANG.TH -> "เสร็จสิ้น"      // Thai
                        LANG.TW -> "完成"           // Chinese Traditional
                        else -> "Done"            // English (default)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black,
                    )
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            items(
                count = listLanguage.size
            ) { index ->
                val item = listLanguage[index]

                LanguageItem(
                    modifier = Modifier.fillMaxWidth(),
                    language = item,
                    isSelected = selectLanguage?.code == item.code,
                    onClick = {
                        selectLanguage = item
                    }
                )
            }
            item {
                Spacer(Modifier.height(24.dp))
            }

        }
        val adUnitId = appViewModel.getBannerLanguage()
        Log.e("log1", "adUnitId:$adUnitId ")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .border(0.5.dp, color = Color.Gray)
                .padding(vertical = 10.dp)
        ) {
            BannerADS(
                adUnitId = adUnitId,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    BackHandler {
        if (isReadyNextScreen) {
            if (appViewModel.isCheckADS()) {
                activity?.let {
                    if (InterADS.isAdReady()) {
                        InterADS.showInterstitialAd(it) {
                            onChangeLanguage(selectLanguage?.code)
                        }
                    } else {
                        isShowDialogLoadAds = true
                        scope.launch {
                            delay(2000)
                            isShowDialogLoadAds = false
                            InterADS.showInterstitialAd(it) {
                                onChangeLanguage(selectLanguage?.code)
                            }
                        }
                    }
                } ?: onChangeLanguage(selectLanguage?.code)
            } else {
                onChangeLanguage(selectLanguage?.code)
            }
        }
    }

    if (isShowDialogLoadAds) {
        DialogLoadADS(onDismissRequest = { isShowDialogLoadAds = false })
    }
}

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderBrush = remember(isSelected) {
        if (isSelected) {
            Brush.linearGradient(listOf(Color.Green, Color.Green))
        } else {
            Brush.linearGradient(
                listOf(Color.Black, Color.Black)
            )
        }
    }

    val bgBrush = remember(isSelected) {
        if (isSelected) {
            Brush.linearGradient(listOf(Color.Black, Color.Black, Color(0xFFEE0979)))
        } else {
            Brush.linearGradient(listOf(Color.Black, Color.Black))
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = bgBrush,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier,
            text = language.icon,
            style = TextStyle(fontSize = 32.sp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            modifier = Modifier
                .weight(1f),
            text = language.name,
            style = TextStyle(
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                color = Color.White
            )
        )

    }

}