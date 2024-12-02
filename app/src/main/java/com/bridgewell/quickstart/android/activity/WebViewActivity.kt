package com.bridgewell.quickstart.android.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bridgewell.bwmobile.BWMobile
import com.bridgewell.quickstart.android.R
import com.tbruyelle.rxpermissions3.RxPermissions
import im.delight.android.webview.AdvancedWebView

class WebViewActivity : AppCompatActivity() {
    // Lazily initialize the AdvancedWebView
    private val webView by lazy { AdvancedWebView(this) }

    companion object {
        // Constant for the URL to load in the WebView
        const val URL = "https://img.scupio.com/html/sdk-webview-test.html"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        // Initialize and configure the WebView
        getPermissions()
        setupWebView()

        // Get references to the WebView wrapper and the "Inject" button
        val webViewWrapper = findViewById<ViewGroup>(R.id.frameWebViewWrapper)
        val buttonLoadAd = findViewById<Button>(R.id.button_inject)

        buttonLoadAd.setOnClickListener {
            webViewWrapper.addView(webView)
            webView.loadUrl(URL)
        }
    }

    @SuppressLint("NewApi")
    private fun setupWebView() {
        // Register the WebView with the BWMobile SDK
        BWMobile.getInstance().registerContentWebViewWithAdInfo(webView)

        // Enable geolocation sharing for the WebView
        BWMobile.getInstance().setShareGeoLocation(true)

        val isShareGeoLocation = BWMobile.getInstance().getShareGeoLocation()
    }

    @SuppressLint("CheckResult")
    private fun getPermissions() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
        )
        // Check if device is running Android 13 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        RxPermissions(this@WebViewActivity).requestEachCombined(
            *permissionsToRequest.toTypedArray()
        ).subscribe { permission ->
            if (permission.granted) {
                Log.d("WebView API", "Permissions granted!")
            } else if (permission.shouldShowRequestPermissionRationale) {
                Toast.makeText(this@WebViewActivity, "Need Permissions! Navigate to setting", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@WebViewActivity, "Need Permissions! Navigate to setting", Toast.LENGTH_SHORT).show()
            }
        }
    }
}