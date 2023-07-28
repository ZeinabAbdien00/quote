package com.iti.android_4.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        setUpViewPAger()
        setupNavigation()
        onClicks()
        swipeBottomNavigationWhenViewPagerChanged()
        swipeViewPagerWhenBottomNavigationChanged()

    }

    private fun swipeViewPagerWhenBottomNavigationChanged() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                com.iti.android_4.R.id.home -> {
                    binding.viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                com.iti.android_4.R.id.yesterday -> {
                    binding.viewPager.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
                com.iti.android_4.R.id.search -> {
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
                    0 -> menu.findItem(com.iti.android_4.R.id.home).isChecked = true
                    1 -> menu.findItem(com.iti.android_4.R.id.search).isChecked = true
                    2 -> menu.findItem(com.iti.android_4.R.id.saved).isChecked = true
                    else -> menu.findItem(com.iti.android_4.R.id.yesterday).isChecked = true
                }
            }
        })
    }

    private fun onClicks() {

        binding.viewPager.setOnClickListener {
            binding.viewPager.currentItem = binding.navigationView.itemActiveIndicatorHeight
        }
    }

    private fun setUpViewPAger() {
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(com.iti.android_4.R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(com.iti.android_4.R.id.navigation_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

}