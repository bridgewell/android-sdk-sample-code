package com.bridgewell.quickstart.android.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.bridgewell.quickstart.android.activity.ui.AdAdapter
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
        binding.rvList.addItemDecoration(DividerItemDecoration(this, 0))
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Create the AdAdapter and handle item clicks
        adAdapter = AdAdapter { adType ->
            val intent = Intent(this, TabbedViewActivity::class.java)
            intent.putExtra("SELECTED_AD_TYPE", adType.ordinal)
            startActivity(intent)
        }

        binding.rvList.adapter = adAdapter
    }
}