package com.wevx.dealershipmanagement.presentation

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wevx.dealershipmanagement.databinding.ActivityIntroBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private var alertDialog: AlertDialog? = null
    private lateinit var connectivityManager: ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            runOnUiThread {
                alertDialog?.dismiss()
                goToAuth()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        lifecycleScope.launch {
            delay(2500) // Splash screen duration
            if (isInternetAvailable()) {
                goToAuth()
            } else {
                showNoInternetDialog()
                registerNetworkListener()
            }
        }
    }

    private fun showNoInternetDialog() {
        if (alertDialog?.isShowing == true) return
        alertDialog = AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Your device is not connected to the internet. Please connect to continue.")
            .setCancelable(false)
            .create()
        alertDialog?.show()
    }

    private fun goToAuth() {
        unregisterNetworkListener()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun registerNetworkListener() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun unregisterNetworkListener() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (_: Exception) { }
    }

    override fun onDestroy() {
        unregisterNetworkListener()
        super.onDestroy()
    }
}
