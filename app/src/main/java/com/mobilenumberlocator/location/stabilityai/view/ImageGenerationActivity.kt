package com.mobilenumberlocator.location.stabilityai.view

import android.R
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobilenumberlocator.location.stabilityai.databinding.ActivityImageGenerationBinding
import com.mobilenumberlocator.location.stabilityai.model.ImageSizesModel
import com.mobilenumberlocator.location.stabilityai.model.ImageStylesModel
import com.mobilenumberlocator.location.stabilityai.model.StabilityEngine
import com.mobilenumberlocator.location.stabilityai.model.StabilityIMageRequestModel
import com.mobilenumberlocator.location.stabilityai.model.TextPrompt
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityEngineStates
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityImageStates
import com.mobilenumberlocator.location.stabilityai.view.adapter.ImageSizeAdapter
import com.mobilenumberlocator.location.stabilityai.view.adapter.ImageStyleAdapter
import com.mobilenumberlocator.location.stabilityai.view_model.StabilityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ImageGenerationActivity : AppCompatActivity() {
    private var modelsData: ArrayList<StabilityEngine> = arrayListOf()
    private lateinit var binding: ActivityImageGenerationBinding
    private var selectedModelName: String? = null
    private val viewModel: StabilityViewModel by viewModels()

    var imageHeight=512
    var imageWidth=512

    @Inject
    lateinit var adapterImageSize: ImageSizeAdapter
    @Inject
    lateinit var adapterImageStyle: ImageStyleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageGenerationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupImagesSizesAdapter()
        setupImagesStylesAdapter()
        handleClicks()
    }

    private fun initViews() {
        viewModel.getStabilityEngineListData()
        getStabilityEngineListData()
        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedModelName = modelsData[position].id
                Toast.makeText(
                    this@ImageGenerationActivity,
                    "Model Selected:" + " " +
                            "" + modelsData[position], Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }

        }
    }

    private fun handleClicks() {
        binding.button.setOnClickListener {
            val text = binding.etInput.text.toString()
            if (text.trim().isNotEmpty()) {
                val textList: List<TextPrompt> = listOf(TextPrompt(text, 1))
                val model = selectedModelName?.let { it1 ->
                    StabilityIMageRequestModel(
                        7,
                        it1, "FAST_BLUE", imageHeight, imageWidth, "K_DPM_2_ANCESTRAL", 1, 20, textList
                    )
                }
                if (model != null) {
                    viewModel.generateTextToImage(model)
                    getStabilityImagesData()
                }
            } else {
                Toast.makeText(this, "please enter text first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getStabilityEngineListData() {
        lifecycleScope.launch {
            viewModel.engineList.collect { state ->
                when (state) {
                    is StabilityEngineStates.Loading -> {
                        Log.d("TAG", "initViews: loading")
                        binding.progressBar.visibility = View.VISIBLE
                        binding.button.visibility = View.INVISIBLE
                        // Show loading UI
                    }

                    is StabilityEngineStates.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.button.visibility = View.VISIBLE
                        modelsData = state.data as ArrayList<StabilityEngine>
                        Log.d("TAG", "initViews: success ${modelsData.size}")
                        val list: ArrayList<String> = arrayListOf()
                        modelsData.forEach {
                            Log.d("TAG", "getStabilityEngineListData: $it")
                            list.add(it.id)
                        }

                        val adapter = ArrayAdapter(
                            this@ImageGenerationActivity,
                            R.layout.simple_spinner_item, list
                        )
                        binding.spinner.adapter = adapter
                        selectedModelName = list[0]
                    }

                    is StabilityEngineStates.Failure -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.button.visibility = View.VISIBLE
                        Log.d("TAG", "initViews: Failure")
                        // Show error UI with the error message
                    }

                    else -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.button.visibility = View.VISIBLE
                        Log.d("TAG", "initViews: Empty")

                    }
                }
            }
        }
    }

    private fun getStabilityImagesData() {
        lifecycleScope.launch {
            viewModel.imagesResponseList.collect { state ->
                when (state) {
                    is StabilityImageStates.Loading -> {
                        Log.d("TAG", "initViews: loading")
                        binding.progressBar.visibility = View.VISIBLE
                        binding.button.visibility = View.INVISIBLE
                        // Show loading UI
                    }

                    is StabilityImageStates.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.button.visibility = View.VISIBLE
                        val data = state.data

                        val decodedString: ByteArray = Base64.decode(data.base64, Base64.DEFAULT)
                        // Bitmap Image
                        // Bitmap Image
                        val bitmap =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        binding.ivImage.setImageBitmap(bitmap)

                    }

                    is StabilityImageStates.Failure -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.button.visibility = View.VISIBLE
                        Log.d("TAG", "initViews: Failure")
                        // Show error UI with the error message
                    }

                    else -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.button.visibility = View.VISIBLE
                        Log.d("TAG", "initViews: Empty")

                    }
                }
            }
        }
    }

    private fun setupImagesSizesAdapter(){
        binding.rvImageSizes.layoutManager=LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImageSizes.adapter=adapterImageSize
        viewModel.getImagesSizesData(this)
        var list:ArrayList<ImageSizesModel> = arrayListOf()
        viewModel.imagesData.observe(this){
            list=it
            adapterImageSize.updateData(it)
        }

        adapterImageSize.onSizeChanged { pos->
            list.forEach{
                if (it.name==list[pos].name){
                    list[pos].isSelected=true
                }else{
                    it.isSelected=false
                }
            }
            adapterImageSize.updateData(list)
            imageHeight=list[pos].height
            imageWidth=list[pos].width
        }
    }
    private fun setupImagesStylesAdapter(){
        binding.rvImageStyles.layoutManager=GridLayoutManager(this, 4)
        binding.rvImageStyles.adapter=adapterImageStyle
        viewModel.getImagesStyleData(this)
        var list:ArrayList<ImageStylesModel> = arrayListOf()
        viewModel.imagesStylesData.observe(this){
            list=it
            adapterImageStyle.updateData(it)
        }

        adapterImageStyle.onSizeChanged {
            list[it].isSelected=false
            adapterImageStyle.updateData(list)
        }
    }
}