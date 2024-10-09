package com.bridgewell.quickstart.android.data

import com.bridgewell.quickstart.android.R

enum class AdType(val titleStringRes: Int) {
    BANNER(R.string.in_app_banner_ad_implementation),
    STICKY_BOTTOM_RIGHT(R.string.in_app_sticky_bottom_right_ad_implementation),
    WEB_VIEW(R.string.web_view_api_usage)
}