package com.example.storyapp.utils

import android.graphics.BitmapFactory
import java.io.File
import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

fun File.reduceFileImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path)
    val compressedFile = File.createTempFile("compressed_", ".jpg", this.parentFile)

    var quality = 100
    val byteArrayOutputStream = ByteArrayOutputStream()


    do {
        byteArrayOutputStream.reset() // Reset output stream
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        quality -= 10
        Log.d("reduceFileImage", "Quality: $quality, Size: ${byteArrayOutputStream.size() / 1024} KB")
    } while (byteArrayOutputStream.size() > 1 * 1024 * 1024 && quality > 0)


    FileOutputStream(compressedFile).use { outputStream ->
        outputStream.write(byteArrayOutputStream.toByteArray())
        outputStream.flush()
    }

    Log.d("reduceFileImage", "Final compressed file size: ${compressedFile.length() / 1024} KB")
    return compressedFile
}
