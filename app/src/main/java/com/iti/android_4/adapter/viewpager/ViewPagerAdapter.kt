package com.iti.android_4.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iti.android_4.ui.saved.SavedQuotesFragment
import com.iti.android_4.ui.search.SearchQuoteFragment
import com.iti.android_4.ui.today.TodayQuoteFragment
import com.iti.android_4.ui.setting.SettingFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return  4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayQuoteFragment()
            1 -> SearchQuoteFragment()
            2 -> SavedQuotesFragment()
            else -> SettingFragment()
        }
    }
}