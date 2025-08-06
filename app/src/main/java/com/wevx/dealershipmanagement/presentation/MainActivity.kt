package com.wevx.dealershipmanagement.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.databinding.ActivityMainBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase
import com.wevx.dealershipmanagement.presentation.adapter.DrawerItemAdapter
import com.wevx.dealershipmanagement.presentation.auth.logout.LogoutViewModel
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.utils.Constants
import com.wevx.dealershipmanagement.utils.Constants.SEE_PROFILE
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: DrawerItemAdapter
    private lateinit var navController: NavController
    private val profileViewModel: GetProfileViewModel by viewModels()

    private val logoutViewModel: LogoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment).navController
        setNavigationDrawer()

        setAllClickListener()

        allObserver()

    }

    private fun setAllClickListener() {

    }

    private fun allObserver() {
        profileObserver()
        logoutObserver()
    }

    private fun logoutObserver() {

    }

    private fun profileObserver() {
        /*profileViewModel.profileState.collectInLifecycle() { categoryState ->

        }*/
    }

    private fun setNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.main

        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }



        adapter = DrawerItemAdapter(LocalDatabase.drawerItems){selectedItem->
            when(selectedItem.name){
                /*SEE_PROFILE->{
                    navController.navigate(R.id.profileFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }*/
                Constants.CHANGE_PASSWORD->{
                    navController.navigate(R.id.changePasswordFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else -> {
                    Toast.makeText(this, "Coming soon: ${selectedItem.name}", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }
        binding.drawerItemRecyclerView.adapter = adapter
    }
}