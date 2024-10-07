package com.bridgewell.quickstart.android.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bridgewell.bwmobile.ads.inapp.InAppApi
import com.bridgewell.bwmobile.ads.inapp.listener.BannerAdListener
import com.bridgewell.bwmobile.model.DisplayBannerModel
import com.bridgewell.quickstart.android.R
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.BannerView

class InAppStickyBottomRightAdActivity : AppCompatActivity() {

    companion object {
        // Constant for the ad configuration ID
        const val CONFIG_ID = "config_bw_app_staging_carousel2"
    }

    // Instance of the InAppApi class to handle ad operations
    private val inAppApi = InAppApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_sticky_bottom_right_ad)
        val buttonLoadAd = findViewById<Button>(R.id.button_load_ad)
        buttonLoadAd.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        inAppApi.createStickyBannerAd(
            this,
            model =
            DisplayBannerModel(
                configId = CONFIG_ID,
                width = 300,
                height = 300,
                refreshTimeSeconds = 3000,
            ),
            listener =
            object : BannerAdListener {
                override fun onAdStartLoad(bannerView: BannerView?) {
                    // Called when the ad starts loading
                }

                override fun onAdLoaded(bannerView: BannerView?) {
                    // Called when the ad is loaded successfully
                    Toast.makeText(this@InAppStickyBottomRightAdActivity, "Ad loaded", Toast.LENGTH_SHORT).show()
                }

                override fun onAdDisplayed(bannerView: BannerView?) {
                    // Called when the ad is displayed
                    Toast.makeText(this@InAppStickyBottomRightAdActivity, "Ad displayed", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                    // Called when the ad fails to load
                }

                override fun onAdClicked(bannerView: BannerView?) {
                    // Called when the ad is clicked
                }

                override fun onAdClosed(bannerView: BannerView?) {
                    // Called when the ad is closed
                }
            },)
    }
}