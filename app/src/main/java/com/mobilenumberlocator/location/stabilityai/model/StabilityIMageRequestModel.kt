package com.mobilenumberlocator.location.stabilityai.model

data class StabilityIMageRequestModel(
    val cfg_scale: Int,
    val engineId: String,
    val clip_guidance_preset: String,
    val height: Int,
    val width: Int,
    val sampler: String,
    val samples: Int,
    val steps: Int,
    val text_prompts: List<TextPrompt>
) {}