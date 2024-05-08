package com.mobilenumberlocator.location.stabilityai.view

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mobilenumberlocator.location.stabilityai.databinding.ActivityEditImageBinding
import com.mobilenumberlocator.location.stabilityai.view_model.StabilityViewModel
import dagger.hilt.android.AndroidEntryPoint
import getBitmapFromImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import saveBitmapToInternalStorage
import java.io.File

@AndroidEntryPoint
class EditImageActivity : AppCompatActivity() {
    private lateinit var client: OkHttpClient
    private lateinit var maskedImageFile: File
    private lateinit var progressDialog: ProgressDialog
    private val viewModel: StabilityViewModel by viewModels()
    var sourceUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.d("chkuris", "pickImage: $uri")
        if (uri != null) {
            CoroutineScope(Dispatchers.IO).launch {
                sourceUri = uri
                if (sourceUri != null) {
                    // Set the result image to ImageView using Glide
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(this@EditImageActivity)
                            .load(sourceUri)
                            .into(binding.orignalImg)
                    }
                }
            }
        }
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var binding: ActivityEditImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = OkHttpClient()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Generating masked image...")
        progressDialog.setCancelable(false)

        // Initialize the permissionLauncher
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    pickImage.launch("image/*")
                } else {
                    // Permission denied, handle accordingly
                }
            }

        binding.pickImg.setOnClickListener {
            checkPermissionsAndPickImage()
        }

        binding.maskImg.setOnClickListener {
            val text = binding.etInput.text.toString()
            if (text.trim().isNotEmpty()) {
                applyMask1(text)
            } else {
                Toast.makeText(this, "please enter text first", Toast.LENGTH_SHORT).show()
            }

        }

        binding.undo.setOnClickListener {
            binding.maskView.undo()
        }

        binding.redo.setOnClickListener {
            binding.maskView.redo()
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

    private fun applyMask1(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val sourceBitmap = binding.orignalImg.getBitmapFromImageView()
            val maskViewBitmap = binding.maskView.getBitmapFromImageView()
            val maskedBitmap = sourceBitmap?.let {b1->maskViewBitmap?.let {applyMask(b1, it)}}
            val originalUri = sourceBitmap?.let { saveBitmapToInternalStorage(it, "source.png") }
            val maskedUri = maskedBitmap?.let { saveBitmapToInternalStorage(it, "masked.png") }

            originalUri?.let { ou ->
                maskedUri?.let { mu ->

                }
            }
        }
    }

    private fun applyMask(originalBitmap: Bitmap, maskBitmap: Bitmap): Bitmap {
        val resultBitmap = Bitmap.createBitmap(
            originalBitmap.width,
            originalBitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(resultBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Draw the original image
        canvas.drawBitmap(originalBitmap, 0f, 0f, paint)

        // Make the mask area transparent by adjusting the alpha channel
        for (y in 0 until maskBitmap.height) {
            for (x in 0 until maskBitmap.width) {
                val maskPixel = maskBitmap.getPixel(x, y)
                val alpha = Color.alpha(maskPixel)

                // If the alpha is non-zero, set the corresponding pixel in the resultBitmap to transparent
                if (alpha != 0) {
                    resultBitmap.setPixel(x, y, Color.argb(0, 0, 0, 0))
                }
            }
        }

        return resultBitmap
    }


}

