package com.bridgewell.quickstart.android.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.bridgewell.quickstart.android.activity.ui.AdAdapter
import com.bridgewell.quickstart.android.data.AdType
import com.bridgewell.quickstart.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adAdapter: AdAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val dividerItemDecoration = DividerItemDecoration(binding.rvList.context, DividerItemDecoration.VERTICAL)
        binding.rvList.addItemDecoration(dividerItemDecoration)
        setContentView(binding.root)
        enableEdgeToEdge()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adAdapter = AdAdapter { adType ->
            when (adType) {
                AdType.BANNER -> {
                    val intent = Intent(this, InAppBannerAdActivity::class.java)
                    startActivity(intent)
                }
                AdType.STICKY_BOTTOM_RIGHT -> {
                    val intent = Intent(this, InAppStickyBottomRightAdActivity::class.java)
                    startActivity(intent)
                }
                AdType.WEB_VIEW -> {
                    val intent = Intent(this, WebViewActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.rvList.adapter = adAdapter
    }
}