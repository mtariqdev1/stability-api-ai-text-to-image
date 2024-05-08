package com.mobilenumberlocator.location.stabilityai.repo

import android.content.Context
import android.util.Log
import com.mobilenumberlocator.location.stabilityai.R
import com.mobilenumberlocator.location.stabilityai.model.ImageSizesModel
import com.mobilenumberlocator.location.stabilityai.model.ImageStylesModel
import com.mobilenumberlocator.location.stabilityai.model.StabilityIMageRequestModel
import com.mobilenumberlocator.location.stabilityai.network.RetrofitClient
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityEngineStates
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityImageStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class StabilityDataRepo {
    fun fetchEngineListFromStability(): Flow<StabilityEngineStates> = flow {
        Log.d("TAG", "fetchEngineListFromStability: ")
        emit(StabilityEngineStates.Loading)
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.retrofitStability.getEnginesList().execute()
            }
            if (response.isSuccessful) {
                val data = response.body() // Get the List<StabilityEngine> data
                emit(StabilityEngineStates.Success(data ?: emptyList()))
            } else {
                Log.d(
                    "TAG",
                    " failing fetchEngineListFromStability:${response.errorBody()?.string()} "
                )
                emit(
                    StabilityEngineStates.Failure(
                        "Non-200 response: ${
                            response.errorBody()?.string()
                        }"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("TAG", "fetchDataByCityName exception: ${e}")
            emit(StabilityEngineStates.Failure(e.message ?: "Unknown error occurred"))
        }
    }


    suspend fun generateTextToImage(
        model: StabilityIMageRequestModel,
    ): Flow<StabilityImageStates> = flow {
        emit(StabilityImageStates.Loading)
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.retrofitStability.generateTextToImage(
                    model.engineId,
                    "application/json",
                    "application/json",
                    model
                )
            }
            Log.e("TAG", "generateTextToImage:----------- $response")
            if (response.isSuccessful) {
                Log.d("TAG", "generateTextToImage: $response")
                val data = response.body()?.artifacts
                Log.e("TAG", "generateTextToImage:Success $data")
                data?.let { StabilityImageStates.Success(it[0]) }?.let { emit(it) }
            } else {
                Log.e("TAG", "generateTextToImage: 13${response.errorBody()?.string()}")
                emit(
                    StabilityImageStates.Failure(
                        "Non-200 response: ${
                            response.errorBody()?.string()
                        }"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("TAG", "generateTextToImage: 44$e")
            emit(StabilityImageStates.Failure(e.message ?: "Unknown error occurred"))
        }
    }


    fun imagesSizesData(context: Context): ArrayList<ImageSizesModel> {
        val list: ArrayList<ImageSizesModel> = arrayListOf()
        list.add(
            ImageSizesModel(
                " 1:1",
                512,
                512,
                context.resources.getDrawable(R.drawable.display_size),
                true
            )
        )
        list.add(
            ImageSizesModel(
                " 2:1",
                1024,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 1.85:1",
                947,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 16:9",
                910,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 7:4",
                896,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                "3:2",
                768,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 4:3",
                683,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 5:4",
                640,
                512,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 4:5",
                512,
                640,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 3:4",
                512,
                683,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 2:3",
                512,
                768,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 4:7",
                512,
                896,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 9:16",
                512,
                910,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 1:1.85",
                512,
                947,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageSizesModel(
                " 1:2",
                512,
                1024,
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        return list
    }


    fun imagesStyle(context: Context): ArrayList<ImageStylesModel> {
        val list: ArrayList<ImageStylesModel> = arrayListOf()
        list.add(
            ImageStylesModel(
                "3d-model",
                context.resources.getDrawable(R.drawable.display_size),
                true
            )
        )
        list.add(
            ImageStylesModel(
                "analog-film",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(ImageStylesModel("anime", context.resources.getDrawable(R.drawable.display_size)))
        list.add(
            ImageStylesModel(
                "cinematic",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "comic-book",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "digital-art",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "enhance",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "fantasy-art",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "isometric",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "line-art",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "low-poly",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "modeling-compound",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "neon-punk",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "origami",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "photographic",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "pixel-art",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        list.add(
            ImageStylesModel(
                "tile-texture",
                context.resources.getDrawable(R.drawable.display_size)
            )
        )
        return list
    }

}