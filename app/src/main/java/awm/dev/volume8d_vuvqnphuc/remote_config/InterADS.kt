package awm.dev.volume8d_vuvqnphuc.remote_config

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterADS {
    private var mInterstitialAd: InterstitialAd? = null
    private var isAdLoading = false

    fun isAdReady(): Boolean = mInterstitialAd != null

    fun loadInterstitialAd(activity: Activity, adUnitId: String) {
        if (adUnitId.isEmpty() || isAdLoading || mInterstitialAd != null) return

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(activity, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("InterstitialAdHelper", "Ad failed to load: ${adError.message}")
                mInterstitialAd = null
                isAdLoading = false
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("InterstitialAdHelper", "Ad was loaded.")
                mInterstitialAd = interstitialAd
                isAdLoading = false
            }
        })
    }

    fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("InterstitialAdHelper", "Ad was dismissed.")
                    mInterstitialAd = null
                    onAdClosed()
                    // Preload next ad? Maybe not here to avoid constant loading if not needed.
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d("InterstitialAdHelper", "Ad failed to show: ${adError.message}")
                    mInterstitialAd = null
                    onAdClosed()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("InterstitialAdHelper", "Ad showed fullscreen content.")
                }
            }
            mInterstitialAd?.show(activity)
        } else {
            Log.d("InterstitialAdHelper", "Ad was not ready yet.")
            onAdClosed()
        }
    }
}