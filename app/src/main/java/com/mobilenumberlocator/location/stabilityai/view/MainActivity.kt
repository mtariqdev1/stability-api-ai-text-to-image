package com.mobilenumberlocator.location.stabilityai.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobilenumberlocator.location.stabilityai.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditImage.setOnClickListener {
            startActivity(Intent(this,EditImageActivity::class.java))
        }
        binding.btnImageGeneration.setOnClickListener {
            startActivity(Intent(this,ImageGenerationActivity::class.java))
        }
        binding.btnTextChat.setOnClickListener {
            startActivity(Intent(this,ImageVariationActivity::class.java))
        }
    }

}