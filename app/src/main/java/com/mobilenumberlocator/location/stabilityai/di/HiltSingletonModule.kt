package com.mobilenumberlocator.location.stabilityai.di

import android.content.Context
import com.mobilenumberlocator.location.stabilityai.repo.StabilityDataRepo
import com.mobilenumberlocator.location.stabilityai.view.adapter.ImageSizeAdapter
import com.mobilenumberlocator.location.stabilityai.view.adapter.ImageStyleAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HiltSingletonModule {

    @Provides
    @Singleton
    fun providesRepo(): StabilityDataRepo {
        return StabilityDataRepo()
    }

    @Provides
    @Singleton
    fun providesImagesSizeAdapter(@ApplicationContext context: Context):ImageSizeAdapter{
        return ImageSizeAdapter(context)
    }

    @Provides
    @Singleton
    fun providesImageStyleAdapter(@ApplicationContext context: Context):ImageStyleAdapter{
        return ImageStyleAdapter(context)
    }

}