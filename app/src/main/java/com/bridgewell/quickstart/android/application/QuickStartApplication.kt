package com.bridgewell.quickstart.android.application

import android.app.Application
import android.util.Log
import com.bridgewell.bwmobile.BWMobile
import com.bridgewell.bwmobile.listener.OnInitializationListener
import com.bridgewell.bwmobile.model.HostServer
import com.bridgewell.quickstart.android.data.ConfigData

class QuickStartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize and configure the Bridgewell Mobile SDK
        setupBWSDK()
    }

    private fun setupBWSDK() {
        // Get the singleton instance of BWMobile and configure it
        BWMobile.getInstance().apply {
            // Set the account ID for the SDK
            setAccountId(ConfigData.ACCOUNT_ID)

            // Set the custom host server for the SDK
            setHostServer(HostServer.Custom(ConfigData.HOST_SERVER))
        }

        // Initialize the BWMobile SDK with the application context and an initialization listener
        BWMobile.getInstance().initialize(
            this,
            object : OnInitializationListener {
                override fun onSuccess() {
                    // Called when the SDK is initialized successfully
                    Log.d("BWMobile initialization", "initializeSdk successfully")
                }

                override fun onFailed(msg: String) {
                    // Called if the SDK initialization fails
                    Log.d("BWMobile initialization" ,"onFailed: $msg")
                }
            }
        )
    }
}
