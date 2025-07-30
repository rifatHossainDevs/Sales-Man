package com.wevx.dealershipmanagement.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> StateFlow<T>.collectInLifecycle(
    lifecycleOwner: LifecycleOwner,
    collector: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectInLifecycle.collect { collector(it) }
        }
    }
}