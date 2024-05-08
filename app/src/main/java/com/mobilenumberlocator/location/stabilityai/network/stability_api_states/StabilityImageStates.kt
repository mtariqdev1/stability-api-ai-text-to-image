package com.mobilenumberlocator.location.stabilityai.network.stability_api_states

import com.mobilenumberlocator.location.stabilityai.model.Artifact
import com.mobilenumberlocator.location.stabilityai.model.ImageResponseModel

sealed class StabilityImageStates{
    object Loading : StabilityImageStates()
    class Failure(val message: String) : StabilityImageStates()
    class Success(val data: Artifact) : StabilityImageStates()
    object Empty : StabilityImageStates()
}
