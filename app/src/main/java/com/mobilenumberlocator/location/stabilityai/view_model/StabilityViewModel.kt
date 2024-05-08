package com.mobilenumberlocator.location.stabilityai.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilenumberlocator.location.stabilityai.model.ImageSizesModel
import com.mobilenumberlocator.location.stabilityai.model.ImageStylesModel
import com.mobilenumberlocator.location.stabilityai.model.StabilityIMageRequestModel
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityEngineStates
import com.mobilenumberlocator.location.stabilityai.network.stability_api_states.StabilityImageStates
import com.mobilenumberlocator.location.stabilityai.repo.StabilityDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StabilityViewModel @Inject constructor(private var repository: StabilityDataRepo) : ViewModel() {

    private val _engineList = MutableStateFlow<StabilityEngineStates>(StabilityEngineStates.Loading)
    val engineList: StateFlow<StabilityEngineStates> get() = _engineList

    fun getStabilityEngineListData() {
        viewModelScope.launch {
            repository.fetchEngineListFromStability()
                .catch { e ->
                    _engineList.emit(
                        StabilityEngineStates.Failure(
                            e.message ?: "Unknown error occurred"
                        )
                    )
                }
                .collect { users ->
                    _engineList.emit(users)
                }
        }

    }


    private val _imagesResponseList = MutableStateFlow<StabilityImageStates>(StabilityImageStates.Loading)
    val imagesResponseList: StateFlow<StabilityImageStates> get() = _imagesResponseList
    fun generateTextToImage(stabilityIMageRequestModel: StabilityIMageRequestModel) {
        viewModelScope.launch {
            repository.generateTextToImage(stabilityIMageRequestModel)
                .catch { e ->
                    _imagesResponseList.emit(
                        StabilityImageStates.Failure(
                            e.message ?: "Unknown error occurred"
                        )
                    )
                }
                .collect { users ->
                    _imagesResponseList.emit(users)
                }
        }

    }


     var imagesData: MutableLiveData<ArrayList<ImageSizesModel>> = MutableLiveData()
    fun getImagesSizesData(context:Context
    ){
        imagesData.value=repository.imagesSizesData(context )
    }


     var imagesStylesData: MutableLiveData<ArrayList<ImageStylesModel>> = MutableLiveData()
    fun getImagesStyleData(context:Context
    ){
        imagesStylesData.value=repository.imagesStyle(context )
    }
}