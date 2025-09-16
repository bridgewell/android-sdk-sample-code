package com.bridgewell.quickstart.android.data

import com.bridgewell.quickstart.android.R

enum class AdType(val titleStringRes: Int) {
    POP_UP_AD(R.string.in_app_pop_up_ad_implementation),
    STICKY_BOTTOM_RIGHT(R.string.in_app_sticky_bottom_right_ad_implementation),
    MOBILE_STICKY_BOTTOM(R.string.in_app_mobile_bottom_ad_implementation),
    BANNER(R.string.in_app_banner_ad_implementation),
    VIDEO43(R.string.video_43),
    VIDEO169(R.string.video_169),
    POPUP_VIDEO43(R.string.popup_video_43),
    POPUP_VIDEO169(R.string.popup_video_169)
}