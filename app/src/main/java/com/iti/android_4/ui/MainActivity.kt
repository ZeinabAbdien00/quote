package com.iti.android_4.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iti.android_4.R
import com.iti.android_4.adapter.viewpager.ViewPagerAdapter
import com.iti.android_4.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewPager()
        setupNavigation()
        onClicks()
        swipeBottomNavigationWhenViewPagerChanged()
        swipeViewPagerWhenBottomNavigationChanged()


        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height
            if (heightDiff > dpToPx(this@MainActivity)) { // 200dp threshold
                // Keyboard is showing, hide bottom navigation
                binding.navigationView.visibility = View.GONE
            } else {
                // Keyboard is not showing, show bottom navigation
                binding.navigationView.visibility = View.VISIBLE
            }
        }
    }


    private fun dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 200f, context.resources.displayMetrics
        ).toInt()
    }

    private fun swipeViewPagerWhenBottomNavigationChanged() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    binding.viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.yesterday -> {
                    binding.viewPager.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    binding.viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    binding.viewPager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }

    private fun swipeBottomNavigationWhenViewPagerChanged() {
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val menu: Menu = bottomNavigationView.menu
                when (position) {
                    0 -> menu.findItem(R.id.home).isChecked = true
                    1 -> menu.findItem(R.id.search).isChecked = true
                    2 -> menu.findItem(R.id.saved).isChecked = true
                    else -> menu.findItem(R.id.yesterday).isChecked = true
                }
            }
        })
    }

    private fun onClicks() {

        binding.viewPager.setOnClickListener {
            binding.viewPager.currentItem = binding.navigationView.itemActiveIndicatorHeight
        }
    }

    private fun setUpViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.navigation_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        val colorStateList = ColorStateList.valueOf(resources.getColor(R.color.white))
//        bottomNavigationView.itemIconTintList = colorStateList

        bottomNavigationView.itemIconTintList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(colorStateList.defaultColor, ContextCompat.getColor(this, R.color.gray))
        )


    }

}