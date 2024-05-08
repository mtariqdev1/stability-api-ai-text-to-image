package com.mobilenumberlocator.location.stabilityai.model

import android.graphics.drawable.Drawable

data class ImageSizesModel(
   var name: String,
   var width: Int,
   var  height: Int,
   var  drawable: Drawable,
   var  isSelected: Boolean=false
) {
}