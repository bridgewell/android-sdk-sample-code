package com.bridgewell.quickstart.android.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bridgewell.quickstart.android.R
import com.bridgewell.quickstart.android.activity.ui.adapter.TabbedViewAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabbedViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed_view)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val adapter = TabbedViewAdapter(this)
        viewPager.adapter = adapter

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
    }
}