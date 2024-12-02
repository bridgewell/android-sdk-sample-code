package com.bridgewell.quickstart.android.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.bridgewell.bwmobile.ads.inapp.InAppApi
import com.bridgewell.bwmobile.ads.inapp.listener.BwsAdViewListener
import com.bridgewell.bwmobile.ads.inapp.model.BwsAdView
import com.bridgewell.bwmobile.utils.BannerViewUtils
import com.bridgewell.quickstart.android.R
import com.bridgewell.quickstart.android.utils.showToast
import org.prebid.mobile.api.exceptions.AdException
import timber.log.Timber

class InAppBannerAdActivity : AppCompatActivity() {

    companion object {
        // Constant for the ad configuration ID
        const val CONFIG_ID = "config_bw_app_staging_carousel2"
    }

    // Instance of the InAppApi class to handle ad operations
    private val inAppApi = InAppApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_banner_ad)
        val buttonLoadAd = findViewById<Button>(R.id.button_load_ad)
        buttonLoadAd.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        val adWrapper = findViewById<ViewGroup>(R.id.frameAdWrapper)

        // Use the inAppApi to create and load a banner ad
        inAppApi.createBwsBannerAd(
            this,
            configID = CONFIG_ID,
            width = adWrapper.width,
            height = adWrapper.height,
            viewContainer = adWrapper,
            listener =
            object : BwsAdViewListener {
                override fun onAdViewStartLoad(bannerView: BwsAdView?) {
                    Timber.d("onAdViewStartLoad Banner config = ${BannerViewUtils.getConfigIdFromBannerView(bannerView)}")
                    Timber.d("onAdViewStartLoad Banner size = ${bannerView?.size}")

                    showToast("onAdStartLoad")
                }

                override fun onAdViewLoaded(bannerView: BwsAdView?) {
                    showToast("onAdLoaded")
                }

                override fun onAdViewDisplayed(bannerView: BwsAdView?) {
                    showToast("onAdDisplayed")

                    var configIdValue = BannerViewUtils.getConfigIdFromBannerView(bannerView)
                    Timber.d("Banner 1st ID = $configIdValue")

                    BannerViewUtils.setConfigIdForBannerView(bannerView, "config_bw_c4test_app_800x380_d")
                    configIdValue = BannerViewUtils.getConfigIdFromBannerView(bannerView)
                    Timber.d("Banner 2nd ID = $configIdValue")
                }

                override fun onAdViewFailed(
                    bannerView: BwsAdView?,
                    exception: AdException?,
                ) {
                    showToast("onAdFailed ${exception?.message}")
                }

                override fun onAdViewClicked(bannerView: BwsAdView?) {
                    showToast("onAdViewClicked")
                }

                override fun onAdModalBrowserClosed(bannerView: BwsAdView?) {
                    showToast("onAdModalBrowserClosed")
                }

                override fun onAdViewClosed() {
                    showToast("onAdViewClosed")
                }
            },
        )
    }
}