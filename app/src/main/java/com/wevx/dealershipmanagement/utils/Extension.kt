package com.wevx.dealershipmanagement.utils

import android.content.pm.PackageManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun EditText.extract(): String{
    return text.toString().trim()
}

fun requestPermission(
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