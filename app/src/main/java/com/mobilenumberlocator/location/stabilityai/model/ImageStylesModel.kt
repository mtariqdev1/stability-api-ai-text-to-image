package com.mobilenumberlocator.location.stabilityai.model

import android.graphics.drawable.Drawable

data class ImageStylesModel(
    var name: String,
    var drawable: Drawable,
    var isSelected: Boolean = false
) {
}