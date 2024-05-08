package com.mobilenumberlocator.location.stabilityai.network

import com.google.gson.GsonBuilder
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityRetrofitInterface
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TIME_OUT: Long = 120
    private val gson = GsonBuilder().setLenient().create()

    private val okHttpClient2 = OkHttpClient.Builder()
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "") // Replace with your API key
                .build()
            val resp = chain.proceed(newRequest)
            resp
        }.build()
    val retrofitStability: StabilityRetrofitInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(AllApi.BASE_URL)
            .client(okHttpClient2)
            .build().create(StabilityRetrofitInterface::class.java)
    }

}
