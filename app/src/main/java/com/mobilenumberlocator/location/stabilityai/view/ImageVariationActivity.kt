package com.mobilenumberlocator.location.stabilityai.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mobilenumberlocator.location.stabilityai.databinding.ActivityImageVariationBinding
import com.mobilenumberlocator.location.stabilityai.view_model.StabilityViewModel
import dagger.hilt.android.AndroidEntryPoint
import getBitmapFromImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saveBitmapToInternalStorage

@AndroidEntryPoint
class ImageVariationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageVariationBinding
    var sourceUri: Uri? = null

    private val viewModel: StabilityViewModel by viewModels()
    // Initialize the permissionLauncher
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickImage.launch("image/*")
            } else {
                // Permission denied, handle accordingly
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImageVariationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleClicks()
    }


    private fun handleClicks(){
        binding.pickImg.setOnClickListener {
            checkPermissionsAndPickImage()
        }

        binding.done.setOnClickListener {
            generateImages()
        }
    }

    private fun checkPermissionsAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Handle permissions using the Permission API (Android 12+)
            checkPermissionWithPermissionApi()
        } else {
            // Handle permissions using the legacy method (pre-Android 12)
            checkPermissionLegacy()
        }
    }
    private fun checkPermissionWithPermissionApi() {
        val permission = Manifest.permission.READ_MEDIA_IMAGES
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage.launch("image/*")
        } else {
            permissionLauncher.launch(permission)
        }
    }

    private fun checkPermissionLegacy() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage.launch("image/*")
        } else {
            permissionLauncher.launch(permission)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage.launch("image/*")
        }
    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.d("chkuris", "pickImage: $uri")
        if (uri != null) {
            CoroutineScope(Dispatchers.IO).launch {
                sourceUri = uri
                if (sourceUri != null) {
                    // Set the result image to ImageView using Glide
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(this@ImageVariationActivity)
                            .load(sourceUri)
                            .into(binding.orignalImg)
                    }
                }
            }
        }
    }

    private fun generateImages() {
        CoroutineScope(Dispatchers.IO).launch {
            val sourceBitmap = binding.orignalImg.getBitmapFromImageView()
            val originalUri = sourceBitmap?.let { saveBitmapToInternalStorage(it, "source.png") }

            originalUri?.let { ou ->

            }
        }
    }

}