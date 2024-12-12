package com.bridgewell.quickstart.android.activity.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bridgewell.quickstart.android.activity.ui.FilesFragment
import com.bridgewell.quickstart.android.activity.ui.HomeFragment
import com.bridgewell.quickstart.android.activity.ui.NewsFragment
import com.bridgewell.quickstart.android.activity.ui.UserProfileFragment

class TabbedViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> NewsFragment()
            2 -> FilesFragment()
            3 -> UserProfileFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }
}