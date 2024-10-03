package com.bridgewell.quickstart.android.data

import android.app.Activity
import androidx.annotation.StringRes

data class AdModel (
    @StringRes val titleStringRes: Int,
    val activity: Class<out Activity>
)