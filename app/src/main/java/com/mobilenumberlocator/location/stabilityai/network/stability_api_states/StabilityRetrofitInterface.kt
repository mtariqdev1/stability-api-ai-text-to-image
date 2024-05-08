package com.mobilenumberlocator.location.stabilityai.network.stability_api_states

import com.mobilenumberlocator.location.stabilityai.model.ImageResponseModel
import com.mobilenumberlocator.location.stabilityai.model.StabilityEngine
import com.mobilenumberlocator.location.stabilityai.model.StabilityIMageRequestModel
import com.mobilenumberlocator.location.stabilityai.model.StabilityImageResponse
import com.mobilenumberlocator.location.stabilityai.network.AllApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StabilityRetrofitInterface {


    @GET(AllApi.DATA_LIST)
    fun getEnginesList(): Call<List<StabilityEngine>>

    @POST("/v1/generation/{engineId}/text-to-image")
    suspend fun generateTextToImage(
        @Path("engineId") engineId: String,
        @Header("Accept") accept: String,
        @Header("Content-Type") content_type: String,
        @Body requestBody: StabilityIMageRequestModel
    ): Response<ImageResponseModel>

}