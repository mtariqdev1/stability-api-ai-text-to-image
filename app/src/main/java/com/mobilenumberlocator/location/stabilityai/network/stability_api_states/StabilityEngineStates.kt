package com.mobilenumberlocator.location.stabilityai.network.stability_api_states

import com.mobilenumberlocator.location.stabilityai.model.StabilityEngine

sealed class StabilityEngineStates{
    object Loading : StabilityEngineStates()
    class Failure(val message: String) : StabilityEngineStates()
    class Success(val data: List<StabilityEngine>) : StabilityEngineStates()
    object Empty : StabilityEngineStates()
}
