package com.mobilenumberlocator.location.stabilityai.model

data class StabilityImageResponse(
    val base64: String,
    val finishReason: String,
    val seed: Int
)