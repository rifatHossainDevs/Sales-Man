package com.wevx.dealershipmanagement

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.requestPermission(
    request: ActivityResultLauncher<Array<String>>,
    permission: Array<String>,
) {
    request.launch(permission)
}

fun Fragment.areAllPermissionGranted(permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
}