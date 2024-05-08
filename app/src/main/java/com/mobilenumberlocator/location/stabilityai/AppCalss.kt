package com.mobilenumberlocator.location.stabilityai

import android.app.Application
import android.util.Log
import com.mobilenumberlocator.location.stabilityai.model.ArtTypesModel
import dagger.hilt.android.HiltAndroidApp
import readToObjectList

@HiltAndroidApp
class AppCalss : Application() {
    companion object {
        var artTypesData: ArrayList<ArtTypesModel> = arrayListOf()
    }

    override fun onCreate() {
        super.onCreate()
        artTypesData = "art_types.json".readToObjectList(this, ArtTypesModel::class.java) as ArrayList<ArtTypesModel>
        Log.d("ARTDATA", "onCreate: ${artTypesData.size}")
    }
}