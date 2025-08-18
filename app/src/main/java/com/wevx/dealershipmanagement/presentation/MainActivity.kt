package com.wevx.dealershipmanagement.presentation

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.databinding.ActivityMainBinding
import com.wevx.dealershipmanagement.databinding.ChangeProfileImageBottomSheetBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase
import com.wevx.dealershipmanagement.presentation.adapter.DrawerItemAdapter
import com.wevx.dealershipmanagement.presentation.auth.changeProfileImage.ChangeProfileImageViewModel
import com.wevx.dealershipmanagement.presentation.auth.logout.LogoutViewModel
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.utils.Constants.CHANGE_PASSWORD
import com.wevx.dealershipmanagement.utils.Constants.EDIT_PROFILE
import com.wevx.dealershipmanagement.utils.Constants.HOME
import com.wevx.dealershipmanagement.utils.Constants.STOCK_AVAILABILITY
import com.wevx.dealershipmanagement.utils.Constants.TODAYS_DELIVERY
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.areAllPermissionGranted
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.requestPermission
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: DrawerItemAdapter
    private lateinit var navController: NavController
    private val profileViewModel: GetProfileViewModel by viewModels()

    private val logoutViewModel: LogoutViewModel by viewModels()
    private lateinit var token: String

    lateinit var uri: Uri
    private lateinit var tokenManager: TokenManager

    private val changeProfileImageViewModel: ChangeProfileImageViewModel by viewModels()
    private lateinit var bottomSheetBinding: ChangeProfileImageBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

    private lateinit var loading: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = ProgressDialog(this)

        tokenManager = TokenManager(this)
        token = "Bearer ${tokenManager.getAccessToken()}"
        profileViewModel.getProfile(token)


        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment).navController
        setNavigationDrawer()

        setAllClickListener()

        allObserver()

        bottomSheetClickListener()
        allButtonClickListener()
        uploadButtonClickListener()
        permissionRequest = getPermissionRequest()

    }

    private fun allObserver() {
        profileObserver()
        logoutObserver()
        changeProfileImageObserver()
    }

    fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream")
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }

    private fun changeProfileImageObserver() {
        changeProfileImageViewModel.changeProfileImageState.collectInLifecycle(this) { changeProfileImageState ->
            if (changeProfileImageState.loading) {
                loading.show()
                return@collectInLifecycle
            }

            changeProfileImageState.error?.let {
                loading.dismiss()

            }

            changeProfileImageState.data?.let {
                loading.dismiss()
                if (it.success == true) {
                    bottomSheetDialog.dismiss()

                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    Glide.with(this@MainActivity)
                        .load(it.data?.avatar?.replace("http://", "https://"))
                        .placeholder(R.drawable.ic_profile_image_24)
                        .error(R.drawable.ic_edit_profile)
                        .into(binding.ivSeller)


                    Glide.with(this@MainActivity)
                        .load(it.data?.avatar?.replace("http://", "https://"))
                        .placeholder(R.drawable.ic_profile_image_24)
                        .error(R.drawable.ic_edit_profile)
                        .into(bottomSheetBinding.ivUser)
                }
            }
        }
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


    private fun allButtonClickListener() {

    }


    private fun setAllClickListener() {
        binding.ivSeller.setOnClickListener {
            bottomSheetDialog.show()
        }

    }

    private fun bottomSheetClickListener() {
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetBinding = ChangeProfileImageBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
        }

        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnContinue.setOnClickListener {
            val imageFile = uri.let { uriToFile(it, this) }
            val bearerToken = token

            changeProfileImageViewModel.changeProfileImage(imageFile, token = bearerToken)
        }

    }

    private fun uploadButtonClickListener() {
        bottomSheetBinding.btnUploadImage.setOnClickListener {
            requestPermission(permissionRequest, permissionList)
        }
    }

    private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (areAllPermissionGranted(permissionList)) {
                ImagePicker.with(this).cropSquare().compress(1024).maxResultSize(
                    512, 512
                ).createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                if (fileUri.toString() != "") {
                    uri = fileUri
                    bottomSheetBinding.ivUser.setImageURI(fileUri)
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
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
                    tokenManager.clearTokens()
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
                Log.d("profileImage", "profileObserver: ${profileModel.avatar}")
                Glide.with(this@MainActivity)
                    .load(profileModel.avatar.replace("http://", "https://"))
                    .placeholder(R.drawable.ic_profile_image_24)
                    .error(R.drawable.ic_edit_profile)
                    .into(binding.ivSeller)
                

                Glide.with(this@MainActivity)
                    .load(profileModel.avatar.replace("http://", "https://"))
                    .placeholder(R.drawable.ic_profile_image_24)
                    .error(R.drawable.ic_edit_profile)
                    .into(bottomSheetBinding.ivUser)
            }
        }
    }

    companion object {

        private val permissionList = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
    }

    private fun setNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.main

        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }



        adapter = DrawerItemAdapter(LocalDatabase.drawerItems) { selectedItem ->
            when (selectedItem.name) {

                HOME -> {
                    navController.navigate(R.id.homeFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                STOCK_AVAILABILITY -> {
                    navController.navigate(R.id.stockAvailabilityFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)

                }

                CHANGE_PASSWORD -> {
                    navController.navigate(R.id.changePasswordFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                EDIT_PROFILE -> {
                    navController.navigate(R.id.editProfileFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                TODAYS_DELIVERY -> {
                    navController.navigate(R.id.todaysDeliveryFragment)
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