package com.wevx.dealershipmanagement.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.wevx.dealershipmanagement.databinding.ActivityMainBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase
import com.wevx.dealershipmanagement.presentation.adapter.DrawerItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: DrawerItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationDrawer()

    }

    private fun setNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.main

        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        adapter = DrawerItemAdapter(LocalDatabase.drawerItems)
        binding.drawerItemRecyclerView.adapter = adapter
    }
}