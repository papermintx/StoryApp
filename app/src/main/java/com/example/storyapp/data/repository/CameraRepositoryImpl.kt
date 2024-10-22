package com.example.storyapp.data.repository

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.example.storyapp.R
import com.example.storyapp.domain.repository.CameraRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CameraRepositoryImpl @Inject constructor(
    private val application: Application
) : CameraRepository {

    override suspend fun takePhoto(controller: LifecycleCameraController): Uri {
        return suspendCancellableCoroutine { continuation ->
            controller.takePicture(
                ContextCompat.getMainExecutor(application),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                // Rotate image and convert to bitmap
                                val matrix = android.graphics.Matrix().apply {
                                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                                }

                                val imageBitmap = Bitmap.createBitmap(
                                    image.toBitmap(),
                                    0, 0,
                                    image.width, image.height,
                                    matrix, true
                                )

                                // Simpan bitmap ke file dan kembalikan URI
                                val uri = savePhoto(imageBitmap)

                                if (uri != null) {
                                    continuation.resume(uri)
                                } else {
                                    continuation.resumeWithException(Exception("Failed to save photo"))
                                }

                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            } finally {
                                image.close()
                            }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        continuation.resumeWithException(exception)
                    }
                }
            )
        }
    }

    // Fungsi untuk menyimpan gambar ke MediaStore dan mengembalikan URI
    private suspend fun savePhoto(bitmap: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            val resolver: ContentResolver = application.contentResolver

            val imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            val appName = application.getString(R.string.app_name)
            val timeInMillis = System.currentTimeMillis()
            val imageContentValues: ContentValues = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "${timeInMillis}_image.jpg"
                )
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/$appName"
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.DATE_TAKEN, timeInMillis)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val imageMediaStoreUri: Uri? = resolver.insert(
                imageCollection, imageContentValues
            )

            imageMediaStoreUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)
                    uri
                } catch (e: Exception) {
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                    null
                }
            }
        }
    }
}
