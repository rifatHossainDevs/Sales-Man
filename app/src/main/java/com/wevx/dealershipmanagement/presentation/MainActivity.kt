package com.wevx.dealershipmanagement.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import coil.load
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.databinding.ActivityMainBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase
import com.wevx.dealershipmanagement.presentation.adapter.DrawerItemAdapter
import com.wevx.dealershipmanagement.presentation.auth.logout.LogoutViewModel
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.utils.Constants.CHANGE_PASSWORD
import com.wevx.dealershipmanagement.utils.Constants.STOCK_AVAILABILITY
import com.wevx.dealershipmanagement.utils.TokenManager
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
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tokenManager = TokenManager(this)
        token = "Bearer ${tokenManager.getAccessToken()}"
        profileViewModel.getProfile(token)


        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment).navController
        setNavigationDrawer()

        setAllClickListener()

        allObserver()

    }

    private fun setAllClickListener() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logoutViewModel.logoutUser(token)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun allObserver() {
        profileObserver()
        logoutObserver()
    }

    private fun logoutObserver() {
        logoutViewModel.logoutState.collectInLifecycle(this) { logoutState ->
            if (logoutState.loading) return@collectInLifecycle
            logoutState.error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
            logoutState.data?.let { logoutModel ->
                if (logoutModel.success == true) {
                    Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun profileObserver() {
        profileViewModel.profileState.collectInLifecycle(this) { profileState ->
            if (profileState.loading) return@collectInLifecycle
            profileState.error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
            profileState.data?.let { profileModel ->

                binding.tvSellerName.text = profileModel.name
                binding.tvSellerEmail.text = profileModel.email
                binding.tvSellerPhone.text = profileModel.phone
                binding.ivSeller.load(profileModel.avatar)
            }
        }
    }

    private fun setNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.main

        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }



        adapter = DrawerItemAdapter(LocalDatabase.drawerItems) { selectedItem ->
            when (selectedItem.name) {
                /*EDIT_PROFILE -> {
                    navController.navigate(R.id.editProfileFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }*/

                STOCK_AVAILABILITY->{
                    navController.navigate(R.id.stockAvailabilityFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)

                }
                CHANGE_PASSWORD -> {
                    navController.navigate(R.id.changePasswordFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                else -> {
                    Toast.makeText(this, "Coming soon: ${selectedItem.name}", Toast.LENGTH_SHORT)
                        .show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }
        binding.drawerItemRecyclerView.adapter = adapter
    }
}