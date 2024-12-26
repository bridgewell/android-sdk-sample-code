package com.bridgewell.quickstart.android.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bridgewell.bwmobile.ads.inapp.InAppApi
import com.bridgewell.bwmobile.ads.inapp.listener.BwsAdViewListener
import com.bridgewell.bwmobile.ads.inapp.model.BwsAdView
import com.bridgewell.quickstart.android.R
import com.bridgewell.quickstart.android.activity.ui.adapter.TabbedViewAdapter
import com.bridgewell.quickstart.android.data.AdType
import com.bridgewell.quickstart.android.utils.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import koleton.api.hideSkeleton
import org.prebid.mobile.api.exceptions.AdException
import timber.log.Timber

class TabbedViewActivity : AppCompatActivity() {

    companion object {
        const val CONFIG_ID_BANNER = "dev-bws-banner-ad"
        const val CONFIG_ID_POP_UP = "dev-bws-popup-ad"
        const val CONFIG_ID_RIGHT_SIDE_STICKY = "dev-bws-right-side-sticky-ad"
        const val CONFIG_ID_MOBILE_STICKY = "dev-bws-mobile-sticky-ad"
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

        for (i in 1..17) {
            val imageViewId = resources.getIdentifier("imageView$i", "id", packageName)
            val imageView = findViewById<ImageView>(imageViewId)
            val skeletonColor = when (i) {
                1, 2, 5, 6, 11, 10 -> R.color.skeleton_gray_1
                else -> R.color.skeleton_gray_2
            }

            imageView.setBackgroundColor(ContextCompat.getColor(this, skeletonColor))
        }

        createAd(selectedAdType)
    }

    private fun createAd(adType: AdType) {
        if (adType == AdType.BANNER) {
            val imageViewBanner = findViewById<ImageView>(R.id.imageView1)
            imageViewBanner.hideSkeleton()
        }

        val listener = object : BwsAdViewListener { // Common listener
            override fun onAdViewStartLoad(bannerView: BwsAdView?) {
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
                Timber.d("onAdFailed ${exception?.message}")
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
        }

        when (adType) {
            AdType.POP_UP_AD -> {
                inAppApi.createBwsPopupAd(
                    this,
                    configID = CONFIG_ID_POP_UP,
                    refreshTimeSeconds = 0,
                    listener = listener
                )
            }
            AdType.STICKY_BOTTOM_RIGHT -> {
                inAppApi.createBwsRightSideStickyAd(
                    this,
                    configID = CONFIG_ID_RIGHT_SIDE_STICKY,
                    bottomMargin = 77,
                    refreshTimeSeconds = 0,
                    listener = listener
                )
            }
            AdType.MOBILE_STICKY_BOTTOM -> {
                inAppApi.createBwsMobileStickyAd(
                    this,
                    configID = CONFIG_ID_MOBILE_STICKY,
                    bottomMargin = 65,
                    refreshTimeSeconds = 0,
                    listener = listener
                )
            }
            AdType.BANNER -> {
                val skeletonContainer = findViewById<LinearLayout>(R.id.topSkeletonContainer)
                skeletonContainer.visibility = View.VISIBLE
                for (i in 1..6) {
                    val imageViewId = resources.getIdentifier("topImageView$i", "id", packageName)
                    val imageView = findViewById<ImageView>(imageViewId)
                    val skeletonColor = when (i) {
                        1, 2 -> R.color.skeleton_gray_1
                        else -> R.color.skeleton_gray_2
                    }
                    imageView.setBackgroundColor(ContextCompat.getColor(this, skeletonColor))
                }
                val adWrapperView = findViewById<FrameLayout>(R.id.frameAdWrapper)
                inAppApi.createBwsBannerAd(
                    this,
                    configID = CONFIG_ID_BANNER,
                    width = adWrapperView.width,
                    height = adWrapperView.height,
                    viewContainer = adWrapperView,
                    listener = listener
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppApi.destroy()
    }
}