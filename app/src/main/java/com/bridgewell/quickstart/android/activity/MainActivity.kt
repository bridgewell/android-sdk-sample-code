package com.bridgewell.quickstart.android.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.bridgewell.bwmobile.BWMobile
import com.bridgewell.bwmobile.analytics.IBwAnalytics
import com.bridgewell.bwmobile.listener.OnInitializationListener
import com.bridgewell.bwmobile.model.HostServer
import com.bridgewell.bwmobile.utils.BWLogLevel
import com.bridgewell.quickstart.android.activity.ui.AdAdapter
import com.bridgewell.quickstart.android.data.AdType
import com.bridgewell.quickstart.android.data.ConfigData
import com.bridgewell.quickstart.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View Binding object for the activity's layout
    private lateinit var binding: ActivityMainBinding

    // Adapter for the RecyclerView
    private lateinit var adAdapter: AdAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val dividerItemDecoration =
            DividerItemDecoration(binding.rvList.context, DividerItemDecoration.VERTICAL)
        binding.rvList.addItemDecoration(dividerItemDecoration)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Create the AdAdapter and handle item clicks
        adAdapter = AdAdapter { adType ->
            when (adType) {
                AdType.POP_UP_AD -> {
                    val intent = Intent(this, TabbedViewActivity::class.java)
                    startActivity(intent)
                }

                AdType.STICKY_BOTTOM_RIGHT -> {
                    val intent = Intent(this, TabbedViewActivity::class.java)
                    startActivity(intent)
                }

                AdType.BANNER -> {
                    val intent = Intent(this, TabbedViewActivity::class.java)
                    startActivity(intent)
                }

                AdType.MOBILE_STICKY_BOTTOM -> {
                    val intent = Intent(this, TabbedViewActivity::class.java)
                    startActivity(intent)
                }



//                AdType.WEB_VIEW -> {
//                    val intent = Intent(this, WebViewActivity::class.java)
//                    startActivity(intent)
//                }
            }
        }
        binding.rvList.adapter = adAdapter
    }
}