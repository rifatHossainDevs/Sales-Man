package com.wevx.dealershipmanagement.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wevx.dealershipmanagement.databinding.ActivityIntroBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@IntroActivity, AuthActivity::class.java))
            finish()
        }

    }
}