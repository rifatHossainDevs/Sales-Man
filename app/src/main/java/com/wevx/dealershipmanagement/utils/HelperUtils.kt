package com.wevx.dealershipmanagement.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun handleMultiplePermissionsResult(
    context: Context,
    activity: Activity,
    requestCode: Int,
    expectedRequestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    onGranted: () -> Unit,
    onDeniedRetry: () -> Unit,
    rationaleMessage: String = "All permissions are required for this app"
) {
    if (requestCode != expectedRequestCode) return

    if (grantResults.isNotEmpty()) {
        val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

        if (allGranted) {
            onGranted()
        } else {
            val permanentlyDenied = permissions.any { permission ->
                ActivityCompat.checkSelfPermission(
                    activity,
                    permission
                ) == PackageManager.PERMISSION_DENIED &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            }

            if (permanentlyDenied) {
                openAppSettingsDialog(context)
            } else {
                warningPermissionDialog(context, rationaleMessage) { _, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        onDeniedRetry()
                    }
                }
            }
        }
    }
}


fun openAppSettingsDialog(context: Context) {
    AlertDialog.Builder(context)
        .setMessage("Permission was permanently denied. Please enable it from app settings.")
        .setPositiveButton("Go to Settings") { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        .show()
}

fun warningPermissionDialog(
    context: Context,
    message: String,
    listener: DialogInterface.OnClickListener
) {
    MaterialAlertDialogBuilder(context)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK", listener)
        .create()
        .show()
}