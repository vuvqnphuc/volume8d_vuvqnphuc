package awm.dev.volume8d_vuvqnphuc.remote_config

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

object OpenADS {
    private var mAppOpenAd: AppOpenAd? = null
    private var isAdLoading = false

    fun loadOpenAd(activity: Activity, adUnitId: String, onAdLoaded: () -> Unit = {}, onAdFailed: () -> Unit = {}) {
        if (adUnitId.isEmpty() || isAdLoading || mAppOpenAd != null) {
            if (mAppOpenAd != null) onAdLoaded()
            return
        }

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()

        AppOpenAd.load(activity, adUnitId, adRequest, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("OpenADS", "Ad failed to load: ${adError.message}")
                mAppOpenAd = null
                isAdLoading = false
                onAdFailed()
            }

            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                Log.d("OpenADS", "Ad was loaded.")
                mAppOpenAd = appOpenAd
                isAdLoading = false
                onAdLoaded()
            }
        })
    }

    fun showOpenAd(activity: Activity, onAdClosed: () -> Unit) {
        if (mAppOpenAd != null) {
            mAppOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("OpenADS", "Ad was dismissed.")
                    mAppOpenAd = null
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d("OpenADS", "Ad failed to show: ${adError.message}")
                    mAppOpenAd = null
                    onAdClosed()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("OpenADS", "Ad showed fullscreen content.")
                }
            }
            mAppOpenAd?.show(activity)
        } else {
            Log.d("OpenADS", "Ad was not ready yet.")
            onAdClosed()
        }
    }
}
