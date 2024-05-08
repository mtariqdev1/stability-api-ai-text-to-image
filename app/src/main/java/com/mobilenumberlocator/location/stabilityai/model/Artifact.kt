package com.mobilenumberlocator.location.stabilityai.model

data class Artifact(
    val base64: String,
    val finishReason: String,
    val seed: Long
)