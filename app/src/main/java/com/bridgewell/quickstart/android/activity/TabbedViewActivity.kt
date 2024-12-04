package com.bridgewell.quickstart.android.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.viewpager2.widget.ViewPager2
import com.bridgewell.bwmobile.ads.inapp.InAppApi
import com.bridgewell.bwmobile.ads.inapp.listener.BwsAdViewListener
import com.bridgewell.bwmobile.ads.inapp.model.BwsAdView
import com.bridgewell.bwmobile.utils.BannerViewUtils
import com.bridgewell.quickstart.android.R
import com.bridgewell.quickstart.android.activity.ui.adapter.TabbedViewAdapter
import com.bridgewell.quickstart.android.data.AdType
import com.bridgewell.quickstart.android.utils.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.prebid.mobile.api.exceptions.AdException
import timber.log.Timber

class TabbedViewActivity : AppCompatActivity() {

    companion object {
        const val CONFIG_ID = "dev-bws-banner-ad"
    }

    private val inAppApi = InAppApi()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed_view)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val adapter = TabbedViewAdapter(this)
        viewPager.adapter = adapter

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Home"
                    tab.setIcon(R.drawable.ic_home) // Set icon for Home tab
                }
                1 -> {
                    tab.text = "News"
                    tab.setIcon(R.drawable.ic_news) // Set icon for News tab
                }
                2 -> {
                    tab.text = "Files"
                    tab.setIcon(R.drawable.ic_files) // Set icon for Files tab
                }
                3 -> {
                    tab.text = "User"
                    tab.setIcon(R.drawable.ic_user) // Set icon for User tab
                }
            }
        }.attach()

        // Set default selected tab
        viewPager.setCurrentItem(0, false)

        // Get the selected tab index from the intent
        val selectedTabIndex = intent.getIntExtra("SELECTED_AD_TYPE", 0)

        // Parse the index back to the AdType enum
        val selectedAdType = AdType.entries[selectedTabIndex]

        // Use selectedAdType in your when statement
        when (selectedAdType) {
            AdType.POP_UP_AD -> {
                // Handle PopUpAd
            }
            AdType.STICKY_BOTTOM_RIGHT -> {
                // Handle StickyBottomRight
            }
            AdType.BANNER -> {
                // Handle Banner
                createBannerAd()
            }
            AdType.MOBILE_STICKY_BOTTOM -> {
                // Handle MobileStickyBottom
            }
        }
    }

    private fun createMobileStickyBottomAd() {
    }

    private fun createPopUpAd() {
    }

    private fun createStickyBottomRightAd() {
    }

    private fun createBannerAd() {
        val adWrapperView = findViewById<FrameLayout>(R.id.frameAdWrapper)
        inAppApi.createBwsBannerAd(
            this,
            configID = CONFIG_ID,
            width = adWrapperView.width,
            height = adWrapperView.height,
            viewContainer = adWrapperView,
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
                }

                override fun onAdViewFailed(bannerView: BwsAdView?, exception: AdException?) {
                    showToast("onAdFailed ${exception?.message}")
                    Timber.e("onAdFailed ${exception?.message}")
                }

                override fun onAdViewClicked(bannerView: BwsAdView?) {
                    showToast("onAdClicked")
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


    override fun onDestroy() {
        super.onDestroy()
        inAppApi.destroy()
    }
}