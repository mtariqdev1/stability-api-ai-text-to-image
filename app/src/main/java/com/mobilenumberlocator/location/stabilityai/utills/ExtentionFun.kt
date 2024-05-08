import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun <T> String.readToObjectList(context: Context, type: Class<T>): MutableList<T> {
    val json = this.readFile(context)
    val jsonArray = JSONArray(json)
    val list = mutableListOf<T>()
    for (i in 0 until jsonArray.length()) {
        val objStr = jsonArray.getJSONObject(i).toString()
        val t = objStr.fromJson(type)
        list.add(t)
    }
    return list
}
fun String.readFile(context: Context): String {
    val builder = StringBuilder()
    context.assets.open(this.toLowerCase()).bufferedReader().useLines { lines ->
        lines.forEach {
            builder.append(it)
        }
    }
    return builder.toString()
}

//String Extension
fun <T> String.fromJson(type: Class<T>): T {
    return Gson().fromJson(this, type)

}




// Load image from URI, crop it to square, save it as PNG to internal storage, and return the Uri of the saved image
fun loadCropAndSaveImageAsPNGAndGetUri(context: Context, imageUri: Uri): Uri? {
    try {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri)
        val originalBitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

        // Crop the image to make it square
        val size = minOf(originalBitmap.width, originalBitmap.height)
        val croppedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, size, size)

        // Save cropped image as PNG to internal storage
        val directory: File = context.filesDir // Internal storage directory
        val fileName = "cropped_image.png"
        val file = File(directory, fileName)

        val fileOutputStream = FileOutputStream(file)
        croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        // The cropped image is now saved in the internal storage directory as a PNG file
        return Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null // Return null if there's an error
}


fun View.getBitmapFromImageView(): Bitmap? {
    this.isDrawingCacheEnabled = true
    this.buildDrawingCache(true)
    val bitmap = Bitmap.createBitmap(this.drawingCache)
    this.isDrawingCacheEnabled = false
    return bitmap
}

fun Context.saveBitmapToInternalStorage(
    bitmap: Bitmap,
    fileName: String
): Uri? {
    val directory: File = filesDir // Internal storage directory
    val file = File(directory, fileName)

    // Delete the existing file if it exists
    if (file.exists()) {
        file.delete()
    }

    return try {
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
        Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}